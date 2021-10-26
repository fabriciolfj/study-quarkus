package com.github.fabriciolfj.transaction.controller;

import com.github.fabriciolfj.transaction.integration.http.AccountService;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("transactions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TransactionController {

    @Inject
    @RestClient
    AccountService accountService;

    @PUT
    @Path("{accountNumber}/{amount}")
    public Response newTransaction(@PathParam("accountNumber") final Long accountNumber, @PathParam("amount") final String amount) {
        accountService.transact(accountNumber, amount);
        return Response.ok().build();
    }

    @GET
    @Path("{accountNumber}/balance")
    public Response getBalance(@PathParam("accountNumber") final Long accountNumber) {
        return Response.accepted(accountService.getBalance(accountNumber)).build();
    }
}
