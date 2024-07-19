package com.tean.supergame.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointTransactionResponse {
    private String userId;

    private Integer amount;

    private Integer balance;
}