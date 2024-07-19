package com.tean.supergame.service;

import com.tean.supergame.until.Util;
import io.jsonwebtoken.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.util.Date;

@Data
@Service
@EnableCaching
@RequiredArgsConstructor
public class JwtService {

    @Value("${spring.jwt.secret}")
    private String jwtSecret;

    @Value("${spring.jwt.expire}")
    private int jwtExpirationMs;

    public String generateJwtTokenByUserId(String userId) {
        Date now = new Date();
        Date expDate = new Date(now.getTime() + jwtExpirationMs);
        String tokenGenerate = Jwts.builder()
                .subject(userId)
                .issuedAt(now)
                .expiration(expDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
        return tokenGenerate;
    }

    public String getUserIdFromJwtToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            // Token đã hết hạn
            throw new RuntimeException("JWT token đã hết hạn", e);
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            // Token không hợp lệ
            throw new RuntimeException("JWT token không hợp lệ", e);
        }
    }

    public boolean validateToken(String authToken) {
        if (Util.isNullOrEmpty(authToken) || Util.isBlank(authToken)) {
            System.out.println("Token is null or empty");
            return false;
        }
        try {
            Claims claims = Jwts.parser().setSigningKey(jwtSecret).build().parseSignedClaims(authToken).getPayload();

            return true;
        } catch (Exception e) {
            System.out.println("validateToken failed by :" + e);
            return false;
        }
    }
}