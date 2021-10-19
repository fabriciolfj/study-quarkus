package com.github.fabriciolfj.account.application.in;

import com.github.fabriciolfj.account.domain.Account;

public interface AccountMakeWithdrawal {

    Account execute(final Long accountNumber, final String amount);
}
