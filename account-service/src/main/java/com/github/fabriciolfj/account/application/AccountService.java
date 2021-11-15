package com.github.fabriciolfj.account.application;

import com.github.fabriciolfj.account.application.in.AccountCrud;
import com.github.fabriciolfj.account.application.out.AccountFindAll;
import com.github.fabriciolfj.account.application.out.AccountFindByNumber;
import com.github.fabriciolfj.account.application.out.AccountSave;
import com.github.fabriciolfj.account.domain.Account;
import lombok.RequiredArgsConstructor;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
@RequiredArgsConstructor
public class AccountService implements AccountCrud {

    private final AccountFindAll accountFindAll;
    private final AccountFindByNumber accountFindByNumber;
    private final AccountSave accountSave;

    @Override
    public Set<Account> findAll() {
        return accountFindAll.allAccounts().stream().collect(Collectors.toSet());
    }

    @Override
    public Account getByNumber(final Long accountNumber) {
        return accountFindByNumber.findByNumber(accountNumber);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void create(final Account account) {
        accountSave.save(account);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void delete(final Long number) {

    }
}
