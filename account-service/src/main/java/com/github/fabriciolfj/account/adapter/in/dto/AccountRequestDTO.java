package com.github.fabriciolfj.account.adapter.in.dto;

import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

@Data
@Schema(name = "Account", description = "POJO representing an account.",
        type = SchemaType.OBJECT)
public class AccountRequestDTO {
    @Schema(required = true, example = "123456789", minLength = 8,
            type = SchemaType.INTEGER)
    private Long accountNumber;
    @Schema(required = true, example = "432542374", minLength = 6,
            type = SchemaType.INTEGER)
    private Long customerNumber;
    @Schema(example = "Steve Hanger", type = SchemaType.STRING)
    private String customerName;
    @Schema(required = true, example = "438.32")
    private BigDecimal balance;
    private BigDecimal overdraftLimit;
}
