package com.intevalue.bankingapi.dto;

import java.util.Date;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private String id;
    private String email;
    private String token;
    private String fullName;
    private Date tokenExpiryTime;
    private String role;
}
