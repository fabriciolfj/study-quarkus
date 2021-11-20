package com.github.fabriciolfj.account.adapter.in;

import com.github.fabriciolfj.account.adapter.in.dto.AccountRequestDTO;
import com.github.fabriciolfj.account.adapter.in.dto.ErrorResponse;
import com.github.fabriciolfj.account.adapter.in.mapper.AccountDTOMapper;
import com.github.fabriciolfj.account.application.in.AccountCrud;
import com.github.fabriciolfj.account.application.in.AccountMakeWithdrawal;
import com.github.fabriciolfj.account.application.in.FindBalance;
import com.github.fabriciolfj.account.domain.Account;
import io.opentracing.Tracer;
import org.eclipse.microprofile.metrics.Counter;
import org.eclipse.microprofile.metrics.annotation.Metric;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.opentracing.Traced;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Path("accounts")
@OpenAPIDefinition(
        tags = {

                @Tag(name = "admin",
                        description = "Operations for managing accounts.")
        },
        info = @Info(
                title = "Account Service",
                description = "Service for maintaining accounts, their balances, and issuing deposit and withdrawal transactions",
                version = "1.0.0",
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"
                )
        )
)
public class AccountResource {

    @Inject
    AccountCrud accountCrud;
    @Inject
    AccountMakeWithdrawal accountMakeWithdrawal;
    @Inject
    FindBalance findBalance;
    @Inject
    Tracer tracer;

    @Provider
    public static class ErrorMapper implements ExceptionMapper<Exception> {

        @Metric(
                name = "ErrorMapperCounter",
                description = "Number of times the AccountResource ErrorMapper is invoked"
        )
        Counter errorMapperCounter;

        @Override
        public Response toResponse(Exception exception) {
            errorMapperCounter.inc();

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
    @APIResponse(responseCode = "200", description = "Retrieved all Accounts",
            content = @Content(
                    schema = @Schema(
                            type = SchemaType.ARRAY,
                            implementation = Account.class)
            )
    )
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
    @Operation(description = "Create a bew bank account")
    @APIResponse(responseCode = "201", description =
            "Successfully created a new account.",
            content = @Content(
                    schema = @Schema(implementation = Account.class))
    )
    @APIResponse(responseCode = "400", description = "No account number was specified on the Account.",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class))
    )
    public Response create(final AccountRequestDTO dto) {
        accountCrud.create(AccountDTOMapper.toDomain(dto));
        return Response
                .status(201)
                .build();
    }

    @DELETE
    @Path("{accountNumber}")
    public Response delete(@PathParam("accountNumber") final Long accountNumber) {
        accountCrud.delete(accountNumber);
        return Response.noContent().build();
    }

    @APIResponse(responseCode = "200", description =
            "Successfully deposited funds to an account.",
            content = @Content(
                    schema = @Schema(implementation = Account.class))
    )
    @RequestBody(
            name = "amount",
            description = "Amount to be deposited into the account.",
            required = true,
            content = @Content(
                    schema = @Schema(
                            name = "amount",
                            type = SchemaType.STRING,
                            required = true,
                            minLength = 4),
                    example = "435.61"
            )
    )
    @PUT
    @Path("{accountNumber}/{amount}")
    @Traced(operationName = "withdraw-from-account")
    @Tag(name = "transactions",
            description = "Operations manipulating account balances.")
    public Map<String, List<String>> withdral(@Context final HttpHeaders headers,
                                              @Parameter(
                                                      name = "accountNumber",
                                                      description = "Number of the Account to deposit into.",
                                                      required = true,
                                                      in = ParameterIn.PATH
                                              )@PathParam("accountNumber") final Long accountNumber, @PathParam("amount") final String amount) {
        tracer.activeSpan().setTag("accountNumber", accountNumber);
        tracer.activeSpan().setBaggageItem("withdrawalAmount", amount);

        accountMakeWithdrawal.execute(accountNumber, amount);
        return headers.getRequestHeaders();
    }

    @GET
    @Path("balance/{accountNumber}")
    @RolesAllowed("customer")
    @Tag(name = "transactions",
            description = "Operations manipulating account balances.")
    public BigDecimal getBalance(@PathParam("accountNumber") final Long accountNumber) {
        return findBalance.getBalance(accountNumber);
    }

}
