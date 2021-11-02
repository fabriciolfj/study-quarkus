package com.github.fabriciolfj.account.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.util.Objects;

public class Account {
    private Long accountNumber;
    private Long customerNumber;
    private String customerName;
    private BigDecimal balance;
    private AccountStatus accountStatus = AccountStatus.OPEN;
    private BigDecimal overdraftLimit;

    public Account() {
    }

    public Account(final Long accountNumber, final Long customerNumber, final String customerName, final BigDecimal balance, final BigDecimal overdraftLimit, final AccountStatus status) {
        this.accountNumber = accountNumber;
        this.customerNumber = customerNumber;
        this.customerName = customerName;
        this.balance = balance;
        this.overdraftLimit = overdraftLimit;
        this.accountStatus = status;
    }

    public void markOverdrawn() {
        accountStatus = AccountStatus.OVERDRAWN;
    }

    public void removeOverdrawnStatus() {
        accountStatus = AccountStatus.OPEN;
    }

    public void close() {
        accountStatus = AccountStatus.CLOSED;
        balance = BigDecimal.valueOf(0);
    }

    public void withdrawFunds(BigDecimal amount) {
        balance = balance.subtract(amount);
    }

    public void addFunds(BigDecimal amount) {
        balance = balance.add(amount);
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public Long getCustomerNumber() {
        return customerNumber;
    }

    public BigDecimal getOverdraftLimit() {
        return overdraftLimit;
    }

    @JsonIgnore
    public boolean isBalanceSmallerOverdraft() {
        return this.balance.compareTo(this.overdraftLimit) <= 0;
    }

    @JsonIgnore
    public boolean isBalanceNegative() {
        return this.balance.compareTo(BigDecimal.ZERO) < 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(accountNumber, account.accountNumber) && Objects.equals(customerNumber, account.customerNumber) && Objects.equals(customerName, account.customerName) && Objects.equals(balance, account.balance) && accountStatus == account.accountStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, customerNumber, customerName, balance, accountStatus);
    }
}