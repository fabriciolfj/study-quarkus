package com.github.fabriciolfj.account.adapter.in;

import com.github.fabriciolfj.account.adapter.in.dto.AccountRequestDTO;
import com.github.fabriciolfj.account.adapter.in.mapper.AccountDTOMapper;
import com.github.fabriciolfj.account.application.in.AccountCrud;
import com.github.fabriciolfj.account.application.in.AccountMakeWithdrawal;
import com.github.fabriciolfj.account.application.in.FindBalance;
import com.github.fabriciolfj.account.domain.Account;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/accounts")
public class AccountResource {

    @Inject
    AccountCrud accountCrud;
    @Inject
    AccountMakeWithdrawal accountMakeWithdrawal;
    @Inject
    FindBalance findBalance;

    @RestControllerAdvice
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

    @GetMapping
    public Set<Account> allAccounts() {
        return accountCrud.findAll();
    }

    @GetMapping("/{accountNumber}")
    public Account getAccount(@PathParam("accountNumber") final Long accountNumber) {
        return accountCrud.getByNumber(accountNumber);
    }

    @PostMapping
    public Response create(final AccountRequestDTO dto) {
        accountCrud.create(AccountDTOMapper.toDomain(dto));
        return Response
                .status(201)
                .build();
    }

    @DeleteMapping("/{accountNumber}")
    public Response delete(@PathParam("accountNumber") final Long accountNumber) {
        accountCrud.delete(accountNumber);
        return Response.noContent().build();
    }

    @PutMapping("/{accountNumber}/{amount}")
    public Map<String, List<String>> withdral(@Context final HttpHeaders headers, @PathParam("accountNumber") final Long accountNumber, @PathParam("amount") final String amount) {
        accountMakeWithdrawal.execute(accountNumber, amount);
        return headers.getRequestHeaders();
    }

    @GetMapping("/balance/{accountNumber}")
    public BigDecimal getBalance(@PathParam("accountNumber") final Long accountNumber) {
        return findBalance.getBalance(accountNumber);
    }

}
