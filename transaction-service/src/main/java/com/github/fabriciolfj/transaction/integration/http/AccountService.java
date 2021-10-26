package com.github.fabriciolfj.transaction.integration.http;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;

@Path("accounts")
@RegisterRestClient(configKey = "account-service")
@Produces(MediaType.APPLICATION_JSON)
public interface AccountService {

    @GET
    @Path("balance/{accountNumber}")
    BigDecimal getBalance(@PathParam("accountNumber") final Long accountNumber);

    @PUT
    @Path("{accountNumber}/{amount}")
    void transact(@PathParam("accountNumber") final Long accountNumber, @PathParam("amount") final String amount);
}
