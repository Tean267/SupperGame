package com.tean.supergame.until;

import com.tean.supergame.model.dto.ResponseDto;
import com.tean.supergame.model.enums.StatusCodeEnum;
import jakarta.annotation.Nonnull;
import lombok.experimental.UtilityClass;
import org.springframework.http.ResponseEntity;

@UtilityClass
public class ResponseBuilder {
    @Nonnull
    public static <T> ResponseEntity<ResponseDto<T>> okResponse(String message, StatusCodeEnum statusCode) {
        final ResponseDto<T> dto = ResponseDto.<T>
                        builder()
                .success(true)
                .message(message)
                .statusCode(statusCode.toString())
                .build();
        return ResponseEntity.ok(dto);
    }

    public static <T> ResponseEntity<ResponseDto<T>> okResponse(String message, @Nonnull T body, StatusCodeEnum statusCode) {
        final ResponseDto<T> dto = ResponseDto.<T>
                        builder()
                .success(true)
                .message(message)
                .data(body)
                .statusCode(statusCode.toString())
                .build();
        return ResponseEntity.ok(dto);
    }

    public static <T> ResponseEntity<ResponseDto<T>> badRequestResponse(String message, StatusCodeEnum statusCode) {
        final ResponseDto<T> dto = ResponseDto.<T>
                        builder()
                .success(false)
                .message(message)
                .statusCode(statusCode.toString())
                .build();
        return ResponseEntity.ok(dto);
    }

    public static <T> ResponseEntity<ResponseDto<T>> badRequestResponse(String message, @Nonnull T body, StatusCodeEnum statusCode) {
        final ResponseDto<T> dto = ResponseDto.<T>
                        builder()
                .success(false)
                .message(message)
                .data(body)
                .statusCode(statusCode.toString())
                .build();
        return ResponseEntity.ok(dto);
    }
}
