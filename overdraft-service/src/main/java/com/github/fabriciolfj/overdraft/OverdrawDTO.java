package com.github.fabriciolfj.overdraft;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OverdrawDTO {

    private Long account;
    private Long customer;
    private BigDecimal balance;
    private BigDecimal overdraftLimit;
}
