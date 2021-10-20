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
public class AccountData {

    @Id
    @GeneratedValue
    private Long id;
    private Long accountNumber;
    private String customerName;
    private Long customerNumber;
    private BigDecimal balance;
    private String accountStatus;

}
