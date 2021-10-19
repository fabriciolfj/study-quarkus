package com.github.fabriciolfj.account.adapter.out.persistence.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "account")
@NamedQuery(name = "Accounts.findAll", query = "Select a from AccountData a Order by a.accountNumber")
@NamedQuery(name = "Accounts.findByAccountNumber", query = "Select a from AccountData a where a.accountNumber = :accountNumber Order by a.accountNumber")
public class AccountData {

    @Id
    @SequenceGenerator(name = "accountsSequence", sequenceName = "account_id_seq", allocationSize = 1, initialValue = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accountSequence")
    private Long id;
    private Long accountNumber;
    private String customerName;
    private Long customerNumber;
    private BigDecimal balance;
    private String accountStatus;
}
