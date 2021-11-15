package com.github.fabriciolfj.account.adapter.out.events.dto;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class OverdraftLimitUpdateDTO {
    public Long accountNumber;
    public BigDecimal newOverdraftLimit;
}
