package com.ayush.proms.utils;

import com.ayush.proms.enums.ApiResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GlobalApiResponse {
    private ApiResponseStatus responseStatus;
    private String message;
    private Object data;
}
