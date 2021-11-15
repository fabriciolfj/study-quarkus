package com.github.fabriciolfj.account.adapter.out.persistence;

import com.github.fabriciolfj.account.adapter.out.persistence.entities.AccountData;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class AccountRepository implements PanacheRepository<AccountData> {

    public Optional<AccountData> findByAccountNumber(final Long accountNumber) {
        return find("accountNumber", accountNumber)
                .firstResultOptional();
    }

    public long totalAccountForCustomer(final Long customerLong) {
        return find("customerNumber", customerLong).count();
    }
}
