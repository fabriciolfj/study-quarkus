package com.github.fabriciolfj.account.application;

import com.github.fabriciolfj.account.application.in.AccountCrud;
import com.github.fabriciolfj.account.domain.Account;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.WebApplicationException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class AccountService implements AccountCrud {

    final Set<Account> accounts = new HashSet<>();

    @PostConstruct
    public void setup() {
        accounts.add(new Account(123456789L, 987654321L, "George Baird",
                new BigDecimal("354.23")));
        accounts.add(new Account(121212121L, 888777666L, "Mary Taylor",
                new BigDecimal("560.03")));
        accounts.add(new Account(545454545L, 222444999L, "Diana Rigg",
                new BigDecimal("422.00")));
    }

    @Override
    public Set<Account> findAll() {
        return accounts;
    }

    @Override
    public Account getByNumber(final Long accountNumber) {
        return accounts.
                stream()
                .filter(act -> act.getAccountNumber().equals(accountNumber))
                .findFirst()
                .orElseThrow(() -> new WebApplicationException("Account with id of " + accountNumber + " does not exist.", 404));
    }

    @Override
    public Account create(final Account account) {
        accounts.add(account);
        return account;
    }

    @Override
    public void delete(Long number) {
        accounts.remove(getByNumber(number));
    }
}
