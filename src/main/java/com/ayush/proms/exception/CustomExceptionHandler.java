package com.ayush.proms.exception;

import com.ayush.proms.enums.ApiResponseStatus;
import com.ayush.proms.pojos.ApiError;
import com.ayush.proms.utils.CustomMessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.*;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private final CustomMessageSource customMessageSource;

    public CustomExceptionHandler(CustomMessageSource customMessageSource) {
        this.customMessageSource = customMessageSource;
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity handleConstraintViolation(DataIntegrityViolationException ex, WebRequest webRequest){
        final List<String> errors=new ArrayList<>();
        String fieldName="";
        HttpStatus httpStatus=HttpStatus.BAD_REQUEST;
        if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException){
            org.hibernate.exception.ConstraintViolationException violation = ((org.hibernate.exception.ConstraintViolationException) ex.getCause());
            if (violation.getConstraintName().contains("unique_")){
                String[] datas=violation.getConstraintName().split("_");
                String message=customMessageSource.get(datas[datas.length-1]);
                errors.add(customMessageSource.get("error.already.exist",message));
            }else{
                errors.add(customMessageSource.get("cannot.delete"));
            }

            ApiError apiError=new ApiError(ApiResponseStatus.FAIL,httpStatus.value(),errors.get(0));
            return new ResponseEntity<>(apiError, new HttpHeaders(), httpStatus);
        }
        final ApiError apiError = new ApiError(ApiResponseStatus.FAIL, httpStatus.value(), customMessageSource.get("error.database.error"));
        return new ResponseEntity<>(apiError, new HttpHeaders(), httpStatus);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity handleAll(Exception ex,WebRequest webRequest){
        ex.printStackTrace();
        HttpStatus httpStatus=HttpStatus.INTERNAL_SERVER_ERROR;
        ApiError apiError=new ApiError(ApiResponseStatus.FAIL,httpStatus.value(),ex.getMessage());
        return new ResponseEntity(apiError,new HttpHeaders(),httpStatus);
    }
}
