package com.github.fabriciolfj.account.adapter.out.events;

import com.github.fabriciolfj.account.adapter.out.events.dto.OverdrawDTO;
import com.github.fabriciolfj.account.application.out.AccountOverdraw;
import com.github.fabriciolfj.account.domain.Account;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@Slf4j
@ApplicationScoped
public class OverdrawAdapter implements AccountOverdraw {

    @Inject
    @Channel("account-overdrawn")
    Emitter<OverdrawDTO> emitter;

    private int ack = 0;

    @Transactional
    public void execute(final Account account) {
        final var overdraw = OverdrawDTO.builder()
                .balance(account.getBalance())
                .customer(account.getCustomerNumber())
                .overdraftLimit(account.getOverdraftLimit())
                .account(account.getAccountNumber())
                .build();

        emitter.send(overdraw);

        /*CompletableFuture<Account> future = new CompletableFuture<>();
        final List<Throwable> failures = new ArrayList<>();

        emitter.send(Message.of(overdraw,
                () -> {
                    ack++;
                    future.complete(account);
                    return CompletableFuture.completedFuture(null);
                },
                reason -> {
                    failures.add(reason);
                    future.completeExceptionally(reason);
                    return CompletableFuture.completedFuture(null);
                }));*/
    }
}
