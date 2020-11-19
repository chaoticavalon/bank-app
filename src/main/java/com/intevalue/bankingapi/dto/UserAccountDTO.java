package com.intevalue.bankingapi.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountDTO {
    @NotEmpty(message = "fullName must not be empty.")
    private String fullName;
    @NotEmpty(message = "email must not be empty.")
    @Email(message = "Email must be in correct format.")
    private String email;
    private String address;
    private String phoneNumber;
    @NotNull(message = "Currency must not be empty")
    @NotEmpty(message = "Currency must not be empty")
    private String currency;
    @NotNull(message = "Deposit must not be empty")
    private Double deposit;
}
