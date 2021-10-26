package com.github.fabriciolfj.transaction;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.util.Collections;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class WireMockAccountService implements QuarkusTestResourceLifecycleManager {

    private WireMockServer wireMockServer;

    @Override
    public Map<String, String> start() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();

        stubFor(get(urlEqualTo("/accounts/balance/121212"))
                .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                        .withBody("400.00")));

        return Collections.singletonMap("com.github.fabriciolfj.transaction.integration.httpAccountService/mp-rest/url",
                wireMockServer.baseUrl());
    }

    @Override
    public void stop() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }
}
