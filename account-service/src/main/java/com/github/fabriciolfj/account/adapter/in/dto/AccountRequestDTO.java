package com.github.fabriciolfj.account.adapter.in.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountRequestDTO {

    private Long accountNumber;
    private Long customerNumber;
    private String customerName;
    private BigDecimal balance;
    private BigDecimal overdraftLimit;
}
