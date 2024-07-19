package com.tean.supergame.controller;

import com.tean.supergame.model.dto.ResponseDto;
import com.tean.supergame.model.dto.request.CheckInRequest;
import com.tean.supergame.model.dto.request.LoginRequest;
import com.tean.supergame.model.dto.response.*;
import com.tean.supergame.service.CheckInService;
import com.tean.supergame.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    private CheckInService checkInService;

    public UserController(UserService userService, CheckInService checkInService) {
        this.userService = userService;
        this.checkInService = checkInService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<ResponseDto<LoginResponse>> signUp(@RequestBody LoginRequest loginRequest) {
        return userService.signUp(loginRequest);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<ResponseDto<LoginResponse>> signIn(@RequestBody LoginRequest loginRequest) {
        return userService.signIn(loginRequest);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/check-in")
    public ResponseEntity<ResponseDto<CheckInResponse>> checkIn(@RequestBody CheckInRequest checkInRequest) {
        return checkInService.checkIn(checkInRequest);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public ResponseEntity<ResponseDto<UserResponse>> getProfile() {
        return userService.getProfile();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/attendance-status")
    public ResponseEntity<ResponseDto<AttendanceStatusResponse>> checkAttendanceStatus(@RequestBody CheckInRequest checkInRequest) {
        return userService.checkAttendanceStatus(checkInRequest);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/point-transaction")
    public ResponseEntity<ResponseDto<List<PointTransactionResponse>>> pointTransaction() {
        return userService.pointTransaction();
    }
}