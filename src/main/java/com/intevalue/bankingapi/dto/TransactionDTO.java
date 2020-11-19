package com.intevalue.bankingapi.dto;

import java.sql.Timestamp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionDTO {
    private String transactionType;
    private Timestamp transactionDateTime;
    private Double amount;
    private String accountTransferFrom;
    private String accountTransferTo;
    private String depositWithdrawalAccount;
    private String currency;

}
