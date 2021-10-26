package com.github.fabriciolfj.transaction.controller;

import com.github.fabriciolfj.transaction.integration.http.AccountService;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Path("transactions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TransactionController {

    @Inject
    @RestClient
    AccountService accountService;

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
    @Path("{accountNumber}/balance")
    public Response getBalance(@PathParam("accountNumber") final Long accountNumber) {
        return Response.accepted(accountService.getBalance(accountNumber)).build();
    }
}
