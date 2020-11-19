package com.intevalue.bankingapi.model;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {
    private String status;
    private int code;
    private String path;
    private String message;
    private String timeStamp;


    public ExceptionResponse(int status, Map<String, Object> errorAttributes) {
        this.setStatus("failure");
        this.setPath((String) errorAttributes.get("path"));
        this.setMessage((String) errorAttributes.get("message"));
        this.setTimeStamp(errorAttributes.get("timestamp").toString());
        this.setCode(status);
    }

}
