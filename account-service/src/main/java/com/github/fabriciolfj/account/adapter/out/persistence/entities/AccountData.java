package com.github.fabriciolfj.account.adapter.out.persistence.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Optional;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "account")
public class AccountData extends PanacheEntityBase {

    @Id
    @SequenceGenerator(name = "accountsSequence", sequenceName = "account_id_seq", allocationSize = 1, initialValue = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accountSequence")
    private Long id;
    private Long accountNumber;
    private String customerName;
    private Long customerNumber;
    private BigDecimal balance;
    private String accountStatus;

    public static Optional<AccountData> findByAccountNumber(final Long accountNumber) {
        return find("accountNumber", accountNumber)
                .firstResultOptional();
    }

    public static long totalAccountForCustomer(final Long customerLong) {
        return find("customerNumber", customerLong).count();
    }
}
