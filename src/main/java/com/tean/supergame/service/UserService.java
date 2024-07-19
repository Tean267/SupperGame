package com.tean.supergame.service;

import com.tean.supergame.model.dto.ResponseDto;
import com.tean.supergame.model.dto.request.CheckInRequest;
import com.tean.supergame.model.dto.request.LoginRequest;
import com.tean.supergame.model.dto.response.AttendanceStatusResponse;
import com.tean.supergame.model.dto.response.LoginResponse;
import com.tean.supergame.model.dto.response.PointTransactionResponse;
import com.tean.supergame.model.dto.response.UserResponse;
import com.tean.supergame.model.entity.PointTransaction;
import com.tean.supergame.model.entity.UserModel;
import com.tean.supergame.model.enums.AttendanceStatusEnum;
import com.tean.supergame.model.enums.StatusCodeEnum;
import com.tean.supergame.repository.PointTransactionRepository;
import com.tean.supergame.repository.UserRepository;
import com.tean.supergame.security.UserDetailsImpl;
import com.tean.supergame.until.ResponseBuilder;
import com.tean.supergame.until.Util;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final JedisPool jedisPool;

    private final PointTransactionRepository pointTransactionRepository;

    public UserService(UserRepository userRepository,
                       ModelMapper mapper,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       JedisPool jedisPool,
                       PointTransactionRepository pointTransactionRepository) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.jedisPool = jedisPool;
        this.pointTransactionRepository = pointTransactionRepository;
    }

    public ResponseEntity<ResponseDto<LoginResponse>> signUp(LoginRequest loginRequest) {
        if (userRepository.existsUserModelByUsername(loginRequest.getUsername())) {
            return ResponseBuilder.okResponse(
                    "username already exists",
                    StatusCodeEnum.USER0205
            );
        }
        UserModel user = mapper.map(loginRequest, UserModel.class);

        try (Jedis jedis = jedisPool.getResource()) {
            int idInt = Integer.parseInt(jedis.get("LAST_ID")) + 1;
            user.setId(String.format("%05d", idInt));

            jedis.set("LAST_ID", String.valueOf(idInt));
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    "Server Error",
                    StatusCodeEnum.EXCEPTION500
            );
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPoint(1);

        String jwt = jwtService.generateJwtTokenByUserId(user.getId());

        try {
            userRepository.save(user);
            UserResponse userResponse = mapper.map(user, UserResponse.class);

            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setAccessToken(jwt);
            loginResponse.setUserInfo(userResponse);

            return ResponseBuilder.okResponse(
                    "Sign Up successfully",
                    loginResponse,
                    StatusCodeEnum.USER1200
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    "Sign up failed",
                    StatusCodeEnum.USER1200
            );
        }
    }

    public ResponseEntity<ResponseDto<LoginResponse>> signIn(LoginRequest loginRequest) {
        Optional<UserModel> userModel = userRepository.findByUsername(loginRequest.getUsername());
        if (userModel.isEmpty()) {
            return ResponseBuilder.badRequestResponse(
                    "user not exist",
                    StatusCodeEnum.USER0201
            );
        }
        if (!verifyPassword(loginRequest.getPassword(), userModel.get().getPassword())) {
            return ResponseBuilder.badRequestResponse(
                    "login password is incorrect",
                    StatusCodeEnum.USER0202
            );
        }
        UserResponse userResponse = mapper.map(userModel.get(), UserResponse.class);
        String jwt = jwtService.generateJwtTokenByUserId(userModel.get().getId());

        LoginResponse loginResponse = new LoginResponse();

        loginResponse.setAccessToken(jwt);
        loginResponse.setUserInfo(userResponse);

        return ResponseBuilder.okResponse(
                "login successfully",
                loginResponse,
                StatusCodeEnum.USER1201
        );

    }

    private boolean verifyPassword(String password, String hashPassword) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        return bCryptPasswordEncoder.matches(password, hashPassword);
    }

    public UserModel getUserFromContext() {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            System.out.println("User details by authentication: " + userDetails);

            return userDetails.getUser();
        } catch (Exception e) {
            System.out.println("Get user from context failed " + e.getMessage());

            return null;
        }
    }

    public ResponseEntity<ResponseDto<UserResponse>> getProfile() {
        UserModel userModel = getUserFromContext();
        UserResponse userResponse = mapper.map(userModel, UserResponse.class);

        return ResponseBuilder.okResponse(
                "get profile successfully",
                userResponse,
                StatusCodeEnum.USER1203
        );
    }

    public ResponseEntity<ResponseDto<AttendanceStatusResponse>> checkAttendanceStatus(CheckInRequest checkInRequest) {
        ZonedDateTime userTime = ZonedDateTime.parse(checkInRequest.getTimestamp(), DateTimeFormatter.ISO_DATE_TIME);
        UserModel userModel = getUserFromContext();

        AttendanceStatusResponse attendanceStatusResponse = new AttendanceStatusResponse();
        attendanceStatusResponse.setUsername(userModel.getUsername());

        try (Jedis jedis = jedisPool.getResource()) {
            if (jedis.exists(userModel.getId())) {

                attendanceStatusResponse.setAttendanceStatusEnum(AttendanceStatusEnum.LOCK);

                return ResponseBuilder.okResponse(
                        "It's not time to check in yet",
                        attendanceStatusResponse,
                        StatusCodeEnum.USER0204
                );
            }
        } catch (Exception e) {
            System.out.println("Error by: " + e.getMessage());

            return ResponseBuilder.badRequestResponse(
                    "Server Error",
                    StatusCodeEnum.EXCEPTION500
            );
        }

        if (!Util.isWithinCheckinTime(userTime.toLocalTime())) {
            attendanceStatusResponse.setAttendanceStatusEnum(AttendanceStatusEnum.LOCK);

            return ResponseBuilder.okResponse(
                    "It's not time to check in yet",
                    attendanceStatusResponse,
                    StatusCodeEnum.USER0204
            );
        }
        attendanceStatusResponse.setAttendanceStatusEnum(AttendanceStatusEnum.ACTIVE);

        return ResponseBuilder.okResponse(
                "Can take attendance",
                attendanceStatusResponse,
                StatusCodeEnum.USER0204
        );
    }

    public ResponseEntity<ResponseDto<List<PointTransactionResponse>>> pointTransaction() {
        UserModel userModel = getUserFromContext();
        List<PointTransaction> pointTransactionList = pointTransactionRepository.findAllByUserId(userModel.getId());

        List<PointTransactionResponse> pointTransactionResponses = pointTransactionList
                .stream()
                .map(p -> mapper.map(p, PointTransactionResponse.class))
                .collect(Collectors.toList());
        return ResponseBuilder.okResponse(
                "get point transaction successfully",
                pointTransactionResponses,
                StatusCodeEnum.USER1205
        );
    }
}