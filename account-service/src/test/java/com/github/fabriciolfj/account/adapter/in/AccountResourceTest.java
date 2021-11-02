package com.github.fabriciolfj.account.adapter.in;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@QuarkusTestResource(H2DatabaseTestResource.class)
@QuarkusTest
public class AccountResourceTest {

    @Test
    @Order(1)
    void testRetrieveAll() {
        final var result =
                given()
                        .when().get("/accounts")
                        .then()
                        .statusCode(200)
                        .body(
                                containsString("Debbie Hall"),
                                containsString("David Tennant"),
                                containsString("Alex Kingston")
                        )
                        .extract()
                        .response();

        final var accounts = result.jsonPath().getList("$");
        assertThat(accounts, not(empty()));
        assertThat(accounts, hasSize(8));
    }

    /*@Test
    @Order(2)
    void testGetAccount() {
        final Account account =
                given()
                        .when().get("/accounts/{accountNumber}", 545454545)
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(Account.class);

        assertThat(account.getAccountNumber(), equalTo(545454545L));
        assertThat(account.getCustomerName(), equalTo("Diana Rigg"));
        assertThat(account.getBalance(), equalTo(new BigDecimal("422.00")));
        assertThat(account.getAccountStatus(), equalTo(AccountStatus.OPEN));
    }

    @Test
    @Order(3)
    void testCreateAccount() {
        Account newAccount = new Account(324324L, 112244L, "Sandy Holmes",
                new BigDecimal("154.55"));

        Account returnedAccount =
                given()
                        .contentType(ContentType.JSON)
                        .body(newAccount)
                        .when().post("/accounts")
                        .then()
                        .statusCode(201)
                        .extract()
                        .as(Account.class);

        assertThat(returnedAccount, notNullValue());
        assertThat(returnedAccount, equalTo(newAccount));

        Response result =
                given()
                        .when().get("/accounts")
                        .then()
                        .statusCode(200)
                        .body(
                                containsString("George Baird"),
                                containsString("Mary Taylor"),
                                containsString("Diana Rigg"),
                                containsString("Sandy Holmes")
                        )
                        .extract()
                        .response();

        List<Account> accounts = result.jsonPath().getList("$");
        assertThat(accounts, not(empty()));
        assertThat(accounts, hasSize(4));
    }

    @Test
    @Order(4)
    void testDeleteAccount() {
        given()
                .when().delete("/accounts/{accountNumber}", 545454545)
                .then()
                .statusCode(204);

        Response result =
                given()
                        .when().get("/accounts")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        List<Account> accounts = result.jsonPath().getList("$");
        assertThat(accounts, not(empty()));
        assertThat(accounts, hasSize(3));
    }*/
}
