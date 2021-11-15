package com.github.fabriciolfj.transaction.integration.http;

import com.github.fabriciolfj.transaction.exceptions.AccountExceptionMapper;
import com.github.fabriciolfj.transaction.exceptions.AccountNotFoundException;
import com.github.fabriciolfj.transaction.providers.AccountRequestFilter;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParams;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Path("accounts")
@RegisterRestClient(configKey = "account-service")
@ClientHeaderParams({
        @ClientHeaderParam(name = "class-level-param", value = "AccountService interface"),
        @ClientHeaderParam(name = "uuid", value = "{generateValue}"),
})
@RegisterClientHeaders
@RegisterProvider(AccountExceptionMapper.class)
@Produces(MediaType.APPLICATION_JSON)
public interface AccountService {

    @GET
    @Path("balance/{accountNumber}")
    BigDecimal getBalance(@PathParam("accountNumber") final Long accountNumber);

    @PUT
    @Path("{accountNumber}/{amount}")
    @ClientHeaderParam(name = "method-level-param", value = "{generateValue}")
    CompletionStage<Map<String, List<String>>> asyncTransact(@PathParam("accountNumber") final Long accountNumber, @PathParam("amount") final String amount);

    @PUT
    @Path("{accountNumber}/{amount}")
    Map<String, List<String>> transact(@PathParam("accountNumber") final Long accountNumber, @PathParam("amount") final String amount);

    default String generateValue() {
        return UUID.randomUUID().toString();
    }

}
