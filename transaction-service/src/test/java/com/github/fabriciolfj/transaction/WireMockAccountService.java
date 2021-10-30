package com.github.fabriciolfj.transaction;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class WireMockAccountService implements QuarkusTestResourceLifecycleManager {

    private WireMockServer wireMockServer;

    private static final String SERVER_ERROR_1 = "CB Fail 1";
    private static final String SERVER_ERROR_2 = "CB Fail 2";
    private static final String CB_OPEN_1 = "CB Open 1";
    private static final String CB_OPEN_2 = "CB Open 2";
    private static final String CB_OPEN_3 = "CB Open 3";
    private static final String CB_SUCCESS_1 = "CB Success 1";
    private static final String CB_SUCCESS_2 = "CB Success 2";

    @Override
    public Map<String, String> start() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();

        mockAccountService();
        mockTimeout();
       // mockCircuitBreaker();

        return Collections.singletonMap("com.github.fabriciolfj.transaction.integration.httpAccountService/mp-rest/url",
                wireMockServer.baseUrl());
    }


    void mockCircuitBreaker() {
        // Define wiremock scenario to support the required by a
        // circuitbreaker state machine

        createCircuitBreakerStub(Scenario.STARTED, SERVER_ERROR_1,
                "100.00", 200);
        createCircuitBreakerStub(SERVER_ERROR_1, SERVER_ERROR_2,
                "200.00", 502);
        createCircuitBreakerStub(SERVER_ERROR_2, CB_OPEN_1, "300.00", 502);
        createCircuitBreakerStub(CB_OPEN_1, CB_OPEN_2, "400.00", 200);
        createCircuitBreakerStub(CB_OPEN_2, CB_OPEN_3, "400.00", 200);
        createCircuitBreakerStub(CB_OPEN_3, CB_SUCCESS_1, "500.00", 200);
        createCircuitBreakerStub(CB_SUCCESS_1, CB_SUCCESS_2, "600.00", 200);
    }

    protected void mockAccountService() {
        stubFor(get(urlEqualTo("/accounts/balance/121212"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json").withBody("435.76")));

        stubFor(post(urlEqualTo("/accounts/balance/121212")).willReturn(aResponse()
                // noContent() needed to be changed once the external service returned a Map
                .withHeader("Content-Type", "application/json").withStatus(200).withBody("{}")));
    }

    protected void mockTimeout() {
        stubFor(get(urlEqualTo("/accounts/balance/123456"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json").withStatus(200).withFixedDelay(200).withBody("435.76")));

        stubFor(get(urlEqualTo("/accounts/balance/456789"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json").withStatus(200).withBody("435.76")));
    }

    void createCircuitBreakerStub(String currentState, String nextState,
                                  String response, int status) {
        stubFor(post(urlEqualTo("/accounts/456789/transaction"))
                .inScenario("circuitbreaker")
                .whenScenarioStateIs(currentState).willSetStateTo(nextState)
                .willReturn(aResponse().withStatus(status).withHeader(
                        "Content-Type", MediaType.TEXT_PLAIN).withBody(response)));
    }

    @Override
    public void stop() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }
}
