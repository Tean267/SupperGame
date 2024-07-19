package com.tean.supergame.model.dto.request;

import lombok.Data;

@Data
public class LoginRequest {

    private String username;

    private String password;
}