package com.github.fabriciolfj.account.application.out;

import com.github.fabriciolfj.account.domain.Account;

public interface AccountOverdraw {

    void execute(final Account account);
}
