package com.github.fabriciolfj.account.application.out;

import com.github.fabriciolfj.account.domain.Account;

public interface AccountUpdate {

    void update(final Account account, final Long accountNumber);
}
