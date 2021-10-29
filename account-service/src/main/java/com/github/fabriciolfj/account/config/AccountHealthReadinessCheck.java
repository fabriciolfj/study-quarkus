package com.github.fabriciolfj.account.config;

import com.github.fabriciolfj.account.application.in.FindBalance;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import java.math.BigDecimal;

@Readiness
@ApplicationScoped
public class AccountHealthReadinessCheck implements HealthCheck {

    @Inject
    FindBalance findBalance;

    BigDecimal balance;

    @Override
    public HealthCheckResponse call() {
        try {
            balance = findBalance.getBalance(999999999L);
        } catch (WebApplicationException ex) {
            // This class is a singleton, so clear last request's balance
            balance = new BigDecimal(Integer.MIN_VALUE);

            if (ex.getResponse().getStatus() >= 500) {
                return HealthCheckResponse
                        .named("AccountServiceCheck")
                        .withData("exception", ex.toString())
                        .down()
                        .build();
            }
        }

        return HealthCheckResponse
                .named("AccountServiceCheck")
                .withData("balance", balance.toString())
                .up()
                .build();
    }
}