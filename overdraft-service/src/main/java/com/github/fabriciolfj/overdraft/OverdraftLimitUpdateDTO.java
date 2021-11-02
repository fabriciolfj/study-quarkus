package com.github.fabriciolfj.overdraft;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OverdraftLimitUpdateDTO {
    public Long accountNumber;
    public BigDecimal newOverdraftLimit;
}
