package com.github.fabriciolfj.account.adapter.out.events;

import com.github.fabriciolfj.account.adapter.out.events.dto.OverdraftLimitUpdateDTO;
import com.github.fabriciolfj.account.adapter.out.persistence.AccountRepository;
import io.smallrye.reactive.messaging.annotations.Blocking;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.WebApplicationException;

@Slf4j
@ApplicationScoped
public class OverdrawUpdateAdapter {

    @Inject
    private AccountRepository accountRepository;

    @Incoming("overdraft-update")
    @Blocking
    @Transactional
    public void execute(final OverdraftLimitUpdateDTO dto) {
        log.info("Consumer: {}", dto.toString());
        final var account = accountRepository
                .findByAccountNumber(dto.getAccountNumber()).orElseThrow(() -> new WebApplicationException("Account with " + dto.getAccountNumber() + "does not exist.", 404));
        account.setOverdraftLimit(dto.getNewOverdraftLimit());
    }
}
