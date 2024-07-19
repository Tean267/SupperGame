package com.tean.supergame.service;

import com.tean.supergame.model.dto.ResponseDto;
import com.tean.supergame.model.dto.request.CheckInRequest;
import com.tean.supergame.model.dto.response.CheckInResponse;
import com.tean.supergame.model.entity.UserModel;
import com.tean.supergame.model.enums.StatusCodeEnum;
import com.tean.supergame.repository.UserRepository;
import com.tean.supergame.until.ResponseBuilder;
import com.tean.supergame.until.Util;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisException;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CheckInService {
    private JedisPool jedisPool;

    private UserService userService;

    private UserRepository userRepository;

    public CheckInService(JedisPool jedisPool, UserService userService, UserRepository userRepository) {
        this.jedisPool = jedisPool;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ResponseEntity<ResponseDto<CheckInResponse>> checkIn(CheckInRequest checkInRequest) {
        ZonedDateTime userTime = ZonedDateTime.parse(checkInRequest.getTimestamp(), DateTimeFormatter.ISO_DATE_TIME);
        UserModel userModel = userService.getUserFromContext();
        Transaction transaction = null;
        try (Jedis jedis = jedisPool.getResource()) {
            boolean isCheckIn = jedis.exists(userModel.getId());
            CheckInResponse checkInResponse = new CheckInResponse();
            if (isCheckIn) {
                String timeCheckIn = jedis.get(userModel.getId());

                checkInResponse.setUsername(userModel.getUsername());
                checkInResponse.setMessage("user checked in at " + timeCheckIn);

                return ResponseBuilder.okResponse(
                        "The user has checked in before",
                        checkInResponse,
                        StatusCodeEnum.USER0203
                );
            }
            if (!Util.isWithinCheckinTime(userTime.toLocalTime())) {
                return ResponseBuilder.okResponse(
                        "It's not time to check in yet",
                        StatusCodeEnum.USER0204
                );
            }
            transaction = jedis.multi();
            userModel.setPoint(userModel.getPoint() + 10);
            userRepository.save(userModel);

            transaction.set(userModel.getId(), userTime.toLocalTime().toString());
            transaction.expire(userModel.getId(), Util.getSecondsUntilEnd(userTime.toLocalTime()));

            List<Object> result = transaction.exec();

            if (result == null || result.isEmpty()) {
                throw new JedisException("Transaction failed, rolling back.");
            }
            checkInResponse.setUsername(userModel.getUsername());
            checkInResponse.setMessage("The user has successfully checked in at " + userTime.toLocalTime());

            return ResponseBuilder.okResponse(
                    "The user has successfully checked in",
                    checkInResponse,
                    StatusCodeEnum.USER1202
            );
        } catch (Exception e) {
            transaction.discard();
            throw e;

        }
    }
}