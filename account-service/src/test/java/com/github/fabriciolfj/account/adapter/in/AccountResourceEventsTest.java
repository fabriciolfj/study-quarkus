package com.github.fabriciolfj.account.adapter.in;

import com.github.fabriciolfj.account.adapter.out.events.dto.OverdrawDTO;
import com.github.fabriciolfj.account.domain.Account;
import com.github.fabriciolfj.account.domain.AccountStatus;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.smallrye.reactive.messaging.connectors.InMemoryConnector;
import io.smallrye.reactive.messaging.connectors.InMemorySink;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.junit.jupiter.api.Test;

import javax.enterprise.inject.Any;
import javax.inject.Inject;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
@QuarkusTestResource(InMemoryLifecycleManager.class)
public class AccountResourceEventsTest {

    @Inject
    @Any
    InMemoryConnector connector;

    @Test
    void testOverdraftEvent() {
        InMemorySink<OverdrawDTO> overdrawnSink = connector.sink(
                "account-overdrawn");

        Account account =
                given()
                        .when().get("/accounts/{accountNumber}", 78790)
                        .then().statusCode(200)
                        .extract().as(Account.class);

        BigDecimal withdrawal = new BigDecimal("23.82");
        BigDecimal balance = account.getBalance().subtract(withdrawal);


        given()
                .contentType(ContentType.JSON)
                .body(withdrawal.toString())
                .when().put("/accounts/{accountNumber}/" + withdrawal, 78790)
                .then().statusCode(200);

        // Asserts verifying account and balance have been removed.

        assertThat(overdrawnSink.received().size(), equalTo(0));

        account =
                given()
                        .when().get("/accounts/{accountNumber}", 78790)
                        .then().statusCode(200)
                        .extract().as(Account.class);

        withdrawal = new BigDecimal("6000.00");
        balance = account.getBalance().subtract(withdrawal);


        given()
                .contentType(ContentType.JSON)
                .body(withdrawal.toString())
                .when().put("/accounts/{accountNumber}/" + withdrawal, 78790)
                .then().statusCode(200);

        account =
                given()
                        .when().get("/accounts/{accountNumber}", 78790)
                        .then().statusCode(200)
                        .extract().as(Account.class);

        // Asserts verifying account and customer details have been removed.
        assertThat(account.getAccountStatus(), equalTo(AccountStatus.OVERDRAWN));
        assertThat(account.getBalance(), equalTo(balance));

        assertThat(overdrawnSink.received().size(), equalTo(1));

        Message<OverdrawDTO> overdrawnMsg = overdrawnSink.received().get(0);
        assertThat(overdrawnMsg, notNullValue());
        OverdrawDTO event = overdrawnMsg.getPayload();
        assertThat(event.getAccount(), equalTo(78790L));
        assertThat(event.getCustomer(), equalTo(444222L));
        assertThat(event.getBalance(), equalTo(balance));
        assertThat(event.getOverdraftLimit(), equalTo(new BigDecimal("-200.00")));
    }
}
