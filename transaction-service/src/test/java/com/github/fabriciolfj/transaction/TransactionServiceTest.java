package com.github.fabriciolfj.transaction;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static io.restassured.RestAssured.given;

@QuarkusTest
@QuarkusTestResource(WireMockAccountService.class)
public class TransactionServiceTest {
    @Test
    void testGetBalance() {
        stubFor(get(urlEqualTo("/accounts/balance/121212"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("400.00")));

        given()
                .body("142.12")
                .contentType(ContentType.JSON)
                .when().get("/transactions/{accountNumber}/balance", 121212)
                .then()
                .statusCode(202);
    }

    @Test
    void testTransaction() {
        stubFor(put(urlEqualTo("/accounts/121212/100"))
                .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(202)));

        given()
                .contentType(ContentType.JSON)
                .when().put("/transactions/{accountNumber}/{amount}", 121212, 100)
                .then()
                .statusCode(200);
    }
}