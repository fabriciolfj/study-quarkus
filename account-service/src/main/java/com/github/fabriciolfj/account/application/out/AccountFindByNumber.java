package com.github.fabriciolfj.account.application.out;

import com.github.fabriciolfj.account.domain.Account;

public interface AccountFindByNumber {

    Account findByNumber(final Long accountNumber);
}
