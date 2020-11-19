package com.intevalue.bankingapi.dto;

import java.sql.Timestamp;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDTO {
    private String accountId;
    private String accountNumber;
    private Double availableBalance;
    private Double totalBalance;
    private Timestamp dateTimeCreated;
    private String email;
    private String accountName;
    private String status;
    private String currency;

}
