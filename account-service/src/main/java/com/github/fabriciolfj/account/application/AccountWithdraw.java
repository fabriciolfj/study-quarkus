package com.github.fabriciolfj.account.application;

import com.github.fabriciolfj.account.application.in.AccountMakeWithdrawal;
import com.github.fabriciolfj.account.application.in.FindBalance;
import com.github.fabriciolfj.account.application.out.AccountFindByNumber;
import com.github.fabriciolfj.account.application.out.AccountOverdraw;
import com.github.fabriciolfj.account.application.out.AccountUpdate;
import com.github.fabriciolfj.account.domain.Account;
import com.github.fabriciolfj.account.domain.AccountStatus;
import lombok.RequiredArgsConstructor;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.WebApplicationException;
import java.math.BigDecimal;

@RequiredArgsConstructor
@ApplicationScoped
public class AccountWithdraw implements AccountMakeWithdrawal, FindBalance {

    private final AccountUpdate accountUpdate;
    private final AccountFindByNumber accountFindByNumber;
    private final AccountOverdraw accountOverdraw;

    @Override
    public Account execute(final Long accountNumber, final String amount) {
        final var account = accountFindByNumber.findByNumber(accountNumber);

        if (account.getAccountStatus().equals(AccountStatus.OVERDRAWN) && account.isBalanceSmallerOverdraft()) {
            throw new WebApplicationException("Account is overdrawn, no further withdrawals permitted", 400);
        }

        account.withdrawFunds(new BigDecimal(amount));

        if (account.isBalanceNegative()) {
            account.markOverdrawn();
            accountOverdraw.execute(account);
        }

        accountUpdate.update(account, accountNumber);
        return account;
    }

    @Override
    public BigDecimal getBalance(final Long accountNumber) {
        return accountFindByNumber.findByNumber(accountNumber)
                .getBalance();
    }
}
