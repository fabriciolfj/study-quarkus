package com.github.fabriciolfj.transaction;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

/*@QuarkusTest
@QuarkusTestResource(WireMockAccountService.class)
@TestSecurity(user = "duke", roles = { "customer" })*/
public class SecurityTest {
    //@Test
    public void built_in_security() {
        given()
                .when()
                .get("/transactions/secure/{acctNumber}/balance", 121212)
                .then()
                .statusCode(202)
                .body(containsString("435.76"));
    }
}
