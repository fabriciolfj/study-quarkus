package com.github.fabriciolfj.account.adapter.in.mapper;

import com.github.fabriciolfj.account.adapter.in.dto.AccountRequestDTO;
import com.github.fabriciolfj.account.domain.Account;
import com.github.fabriciolfj.account.domain.AccountStatus;

public class AccountDTOMapper {

    private AccountDTOMapper() { }

    public static Account toDomain(final AccountRequestDTO dto) {
        return new Account(dto.getAccountNumber(),
                dto.getCustomerNumber(),
                dto.getCustomerName(),
                dto.getBalance(),
                dto.getOverdraftLimit(),
                AccountStatus.OPEN);
    }
}
