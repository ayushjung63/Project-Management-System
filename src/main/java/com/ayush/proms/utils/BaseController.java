package com.ayush.proms.utils;

import com.ayush.proms.enums.ApiResponseStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

public class BaseController {
        private final ApiResponseStatus API_SUCCESS=ApiResponseStatus.SUCCESS;
        private final ApiResponseStatus API_FAIL=ApiResponseStatus.FAIL;

        private ObjectMapper objectMapper=new ObjectMapper();

        @Autowired
        public CustomMessageSource customMessageSource;

        public GlobalApiResponse successResponse(String message,Object data){
            GlobalApiResponse globalApiResponse=new GlobalApiResponse();
            globalApiResponse.setStatus(API_SUCCESS);
            globalApiResponse.setMessage(message);
            globalApiResponse.setData(data);
            return globalApiResponse;
        }

        public GlobalApiResponse errorResponse(String message,Object data){
            GlobalApiResponse globalApiResponse=new GlobalApiResponse();
            globalApiResponse.setStatus(API_FAIL);
            globalApiResponse.setMessage(message);
            globalApiResponse.setData(data);
            return globalApiResponse;
        }
}
