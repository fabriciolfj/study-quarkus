package com.github.fabriciolfj.account.application.in;

import com.github.fabriciolfj.account.domain.Account;

import java.util.Set;

public interface AccountCrud {

    Set<Account> findAll();

    Account getByNumber(final Long accountNumber);

    Account create(final Account account);

    void delete(final Long number);
}
