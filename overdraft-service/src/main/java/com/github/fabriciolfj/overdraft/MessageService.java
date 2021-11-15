package com.github.fabriciolfj.overdraft;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.math.BigDecimal;

@Slf4j
@ApplicationScoped
public class MessageService {

    @Transactional
    @Incoming("account-overdrawn")
    @Outgoing("overdraft-update")
    public Message<OverdraftLimitUpdateDTO> execute(final Message<OverdrawDTO> message) {
        final var payload = message.getPayload();
        log.info("Consumer overdraft: {}", payload.toString());

        final var update = OverdraftLimitUpdateDTO
                .builder()
                .accountNumber(payload.getAccount())
                .newOverdraftLimit(BigDecimal.valueOf(1000.00))
                .build();

        return Message.of(update);
    }
}
