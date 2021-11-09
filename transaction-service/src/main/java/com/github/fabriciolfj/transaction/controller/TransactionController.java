package com.github.fabriciolfj.transaction.controller;

import com.github.fabriciolfj.transaction.exceptions.TransactionServiceFallbackHandler;
import com.github.fabriciolfj.transaction.integration.http.AccountService;
import org.eclipse.microprofile.faulttolerance.*;
import org.eclipse.microprofile.faulttolerance.exceptions.BulkheadException;
import org.eclipse.microprofile.metrics.annotation.ConcurrentGauge;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeoutException;

@Path("transactions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TransactionController {

    @Inject
    @RestClient
    AccountService accountService;

    @ConcurrentGauge(
            name = "concurrentBlockingTransactions",
            absolute = true,
            description = "Number of concurrent transactions using blocking api"
    )
    @PUT
    @Path("{accountNumber}/{amount}")
    public Map<String, List<String>> newTransaction(@PathParam("accountNumber") final Long accountNumber, @PathParam("amount") final String amount) {
        try {
            return accountService.transact(accountNumber, amount);
        } catch (Exception e) {
            e.printStackTrace();
            final var response = new HashMap<String, List<String>>();
            response.put("Exception - " + e.getClass(), Collections.singletonList(e.getMessage()));
            return response;
        }
    }

    @PUT
    @Path("{accountNumber}/{amount}/async")
    public CompletionStage<Map<String, List<String>>> newAsyncTransaction(@PathParam("accountNumber") final Long accountNumber, @PathParam("amount") final String amount) {
        return accountService.asyncTransact(accountNumber, amount);
    }

    @GET
    @CircuitBreaker(requestVolumeThreshold=3, failureRatio=.90, successThreshold=2, delay = 5, delayUnit = ChronoUnit.SECONDS)
    @Retry(maxRetries = 3, delay = 100, jitter = 25, retryOn = TimeoutException.class)
    @Timeout(50)
    @Bulkhead(value=1)
    @Fallback(value = TransactionServiceFallbackHandler.class
            /*fallbackMethod = "bulkheadBalance" ,
            applyOn = BulkheadException.class*/)
    @Path("{accountNumber}/balance")
    public Response getBalance(@PathParam("accountNumber") final Long accountNumber) {
        return Response.accepted(accountService.getBalance(accountNumber)).build();
    }

    public Response bulkheadBalance(final Long accountNumber) {
        return Response.status(Response.Status.TOO_MANY_REQUESTS).build();
    }
}
