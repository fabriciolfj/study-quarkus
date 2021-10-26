package com.github.fabriciolfj.transaction;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
@QuarkusTestResource(WireMockAccountService.class)
public class TransactionServiceTest {
    @Test
    void testTransaction() {
        given()
                .body("142.12")
                .contentType(ContentType.JSON)
                .when().get("/transactions/{accountNumber}/balance", 121212)
                .then()
                .statusCode(202);
    }
}