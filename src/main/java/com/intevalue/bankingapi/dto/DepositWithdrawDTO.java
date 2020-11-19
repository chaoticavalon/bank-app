package com.intevalue.bankingapi.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepositWithdrawDTO {
    @NotEmpty(message = "Account Number must not be empty")
    private String accountNumber;
    @NotNull(message = "Amount must not be empty")
    private Double amount;
    @NotEmpty(message = "Currency must not be empty")
    private String currency;
}
