package com.github.fabriciolfj.account.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum AccountStatus {
    OPEN(0),
    CLOSED(1),
    OVERDRAWN(2);

    private int order;

    public static AccountStatus toStatus(final int order) {
        return Stream.of(AccountStatus.values())
                .filter(type -> type.getOrder() == order)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Type not found: order " + order));
    }
}
