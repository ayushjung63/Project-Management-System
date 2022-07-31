package com.ayush.proms.pojos;

import com.ayush.proms.enums.ApiResponseStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class ApiError {

    private ApiResponseStatus status;
    private int httpCode;
    @JsonProperty("message")
    private String message;

    public ApiError(ApiResponseStatus status, int httpCode, String message) {
        this.status = status;
        this.httpCode = httpCode;
        this.message = message;
    }
}
