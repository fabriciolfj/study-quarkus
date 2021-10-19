package com.github.fabriciolfj.account.adapter.out.persistence;

import com.github.fabriciolfj.account.adapter.out.persistence.entities.AccountData;
import com.github.fabriciolfj.account.adapter.out.persistence.mapper.AccountDataMapper;
import com.github.fabriciolfj.account.application.out.AccountFindAll;
import com.github.fabriciolfj.account.application.out.AccountFindByNumber;
import com.github.fabriciolfj.account.application.out.AccountSave;
import com.github.fabriciolfj.account.application.out.AccountUpdate;
import com.github.fabriciolfj.account.domain.Account;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.ws.rs.WebApplicationException;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class AccountAdapter implements AccountFindAll, AccountFindByNumber, AccountUpdate, AccountSave {

    @Inject
    EntityManager entityManager;

    @Override
    public List<Account> allAccounts() {
        return entityManager.createNamedQuery("Accounts.findAll", AccountData.class)
                .getResultList()
                .stream()
                .map(AccountDataMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Account findByNumber(final Long accountNumber) {
        try {
            return AccountDataMapper.toDomain(getAccountData(accountNumber));
        } catch (NoResultException e) {
            throw new WebApplicationException("Account with " + accountNumber + "does not exist.", 404);
        }
    }

    @Override
    public void save(final Account account) {
        final var accountData = AccountDataMapper.toEntity(account);
        entityManager.persist(accountData);
    }

    @Override
    public Account update(final Account account, final Long accountNumber) {
        var entity = getAccountData(accountNumber);
        var entityMerge = AccountDataMapper.toEntityMerge(account, entity);
        return AccountDataMapper.toDomain(entityMerge);
    }

    private AccountData getAccountData(Long accountNumber) {
        return entityManager.createNamedQuery("Accounts.findByAccountNumber", AccountData.class)
                .setParameter("accountNumber", accountNumber)
                .getSingleResult();
    }
}
