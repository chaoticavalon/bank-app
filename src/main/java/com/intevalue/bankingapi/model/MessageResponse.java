package com.intevalue.bankingapi.model;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse<T> {
    private String message = "Successful";
    private int status = HttpStatus.OK.value();
    private boolean isSuccessful = true;
    private T data;
}
