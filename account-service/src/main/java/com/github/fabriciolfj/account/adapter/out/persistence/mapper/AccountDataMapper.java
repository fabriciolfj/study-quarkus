package com.github.fabriciolfj.account.adapter.out.persistence.mapper;

import com.github.fabriciolfj.account.adapter.out.persistence.entities.AccountData;
import com.github.fabriciolfj.account.domain.Account;
import com.github.fabriciolfj.account.domain.AccountStatus;

public class AccountDataMapper {

    private AccountDataMapper() { }

    public static AccountData toEntity(final Account account) {
        return AccountData
                .builder()
                .customerNumber(account.getCustomerNumber())
                .accountNumber(account.getAccountNumber())
                .accountStatus(account.getAccountStatus().name())
                .customerName(account.getCustomerName())
                .balance(account.getBalance())
                .overdraftLimit(account.getOverdraftLimit())
                .build();
    }

    public static Account toDomain(final AccountData accountData) {
        return new Account(accountData.getAccountNumber(),
                accountData.getCustomerNumber(),
                accountData.getCustomerName(),
                accountData.getBalance(),
                accountData.getOverdraftLimit(),
                AccountStatus.toStatus(Integer.parseInt(accountData.getAccountStatus())));
    }

    public static AccountData toEntityMerge(final Account account, final AccountData accountData) {
        accountData.setBalance(account.getBalance());
        accountData.setAccountStatus(String.valueOf(account.getAccountStatus().getOrder()));
        accountData.setAccountNumber(account.getAccountNumber());
        accountData.setCustomerName(account.getCustomerName());
        accountData.setCustomerNumber(account.getCustomerNumber());
        accountData.setOverdraftLimit(account.getOverdraftLimit());

        return accountData;
    }
}
