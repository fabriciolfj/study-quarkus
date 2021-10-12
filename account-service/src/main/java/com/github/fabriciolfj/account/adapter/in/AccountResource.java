package com.github.fabriciolfj.account.adapter.in;

import com.github.fabriciolfj.account.application.in.AccountCrud;
import com.github.fabriciolfj.account.domain.Account;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Set;

@Path("accounts")
public class AccountResource {

    @Inject
    AccountCrud accountCrud;

    @Provider
    public static class ErrorMapper implements ExceptionMapper<Exception> {

        @Override
        public Response toResponse(Exception exception) {

            int code = 500;
            if (exception instanceof WebApplicationException) {
                code = ((WebApplicationException)
                        exception).getResponse().getStatus();
            }

            JsonObjectBuilder entityBuilder = Json.createObjectBuilder()
                    .add("exceptionType", exception.getClass().getName())
                    .add("code", code);

            if (exception.getMessage() != null) {
                entityBuilder.add("error", exception.getMessage());
            }

            return Response.status(code)
                    .entity(entityBuilder.build())
                    .build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Set<Account> allAccounts() {
        return accountCrud.findAll();
    }

    @GET
    @Path("{accountNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public Account getAccount(@PathParam("accountNumber") final Long accountNumber) {
        return accountCrud.getByNumber(accountNumber);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(final Account account) {
        final var entity = accountCrud.create(account);
        return Response
                .status(201)
                .entity(entity)
                .build();
    }

    @DELETE
    @Path("{accountNumber}")
    public Response delete(@PathParam("accountNumber") final Long accountNumber) {
        accountCrud.delete(accountNumber);
        return Response.noContent().build();
    }


}
