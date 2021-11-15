package com.github.fabriciolfj.account.application.in;

import java.math.BigDecimal;

public interface FindBalance {

    BigDecimal getBalance(final Long accountNumber);
}
