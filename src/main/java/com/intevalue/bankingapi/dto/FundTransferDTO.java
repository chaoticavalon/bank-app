package com.intevalue.bankingapi.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FundTransferDTO {
    @NotEmpty(message = "Recipient Account Number must not be empty.")
    private String accountTransferTo;
    @NotEmpty(message = "Sender Account Number must not be empty.")
    private String  accountTransferFrom;
    @NotNull(message = "Amount must not be empty.")
    private Double amount;
    @NotEmpty(message = "Currency must not be empty")
    private String currency;


}
