package com.github.fabriciolfj.account.adapter.out.persistence;

import com.github.fabriciolfj.account.adapter.out.persistence.entities.AccountData;
import com.github.fabriciolfj.account.adapter.out.persistence.mapper.AccountDataMapper;
import com.github.fabriciolfj.account.application.out.AccountFindAll;
import com.github.fabriciolfj.account.application.out.AccountFindByNumber;
import com.github.fabriciolfj.account.application.out.AccountSave;
import com.github.fabriciolfj.account.application.out.AccountUpdate;
import com.github.fabriciolfj.account.domain.Account;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.WebApplicationException;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class AccountAdapter implements AccountFindAll, AccountFindByNumber, AccountUpdate, AccountSave {

    @Override
    public List<Account> allAccounts() {
        final List<AccountData> accounts = AccountData.listAll();
        return accounts.stream()
                .map(AccountDataMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Account findByNumber(final Long accountNumber) {
        return AccountDataMapper.toDomain(getAccountData(accountNumber));
    }

    @Override
    public void save(final Account account) {
        final var accountData = AccountDataMapper.toEntity(account);
        AccountData.persist(accountData);
    }

    @Override
    public void update(final Account account, final Long accountNumber) {
        var entity = getAccountData(accountNumber);
        var entityMerge = AccountDataMapper.toEntityMerge(account, entity);
        AccountData.persist(entityMerge);
    }

    private AccountData getAccountData(final Long accountNumber) {
        return AccountData.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new WebApplicationException("Account with " + accountNumber + "does not exist.", 404));
    }
}
