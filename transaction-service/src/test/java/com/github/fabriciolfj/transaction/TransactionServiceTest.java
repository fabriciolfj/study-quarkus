package com.github.fabriciolfj.transaction;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

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
                .statusCode(204);
    }

    @Test
    void testTimeout() {
        given()
                .contentType(ContentType.JSON)
                .get("/transactions/123456/balance").then().statusCode(504);      // <2>

        given()
                .contentType(ContentType.JSON)
                .get("/transactions/456789/balance").then().statusCode(202);      // <3>
    }

    //@Test
    void testCircuitBreaker() {
        RequestSpecification request =
                given()
                        .body("142.12")
                        .contentType(ContentType.JSON);

        request.get("/transactions/456789/balance").then().statusCode(200);
        request.get("/transactions/456789/balance").then().statusCode(502);
        request.get("/transactions/456789/balance").then().statusCode(502);
        request.get("/transactions/456789/balance").then().statusCode(503);
        request.get("/transactions/456789/balance").then().statusCode(503);

        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
        }

        request.post("/transactions/456789/balance").then().statusCode(200);
        request.post("/transactions/456789/balance").then().statusCode(200);
    }
}