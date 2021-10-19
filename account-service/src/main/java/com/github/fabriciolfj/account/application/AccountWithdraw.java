package com.github.fabriciolfj.account.application;

import com.github.fabriciolfj.account.application.in.AccountMakeWithdrawal;
import com.github.fabriciolfj.account.application.out.AccountFindByNumber;
import com.github.fabriciolfj.account.application.out.AccountUpdate;
import com.github.fabriciolfj.account.domain.Account;
import lombok.RequiredArgsConstructor;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.math.BigDecimal;

@RequiredArgsConstructor
@ApplicationScoped
public class AccountWithdraw implements AccountMakeWithdrawal {

    private final AccountUpdate accountUpdate;
    private final AccountFindByNumber accountFindByNumber;

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public Account execute(final Long accountNumber, final String amount) {
        final var account = accountFindByNumber.findByNumber(accountNumber);
        account.withdrawFunds(new BigDecimal(amount));

        accountUpdate.update(account, accountNumber);

        return account;
    }
}
