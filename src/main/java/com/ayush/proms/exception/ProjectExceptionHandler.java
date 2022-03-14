package com.ayush.proms.exception;

import com.ayush.proms.exception.project.ProjectStatusNotValidException;
import com.ayush.proms.utils.BaseController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@RestController
@ControllerAdvice
public class ProjectExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ProjectStatusNotValidException.class)
    public ResponseEntity projectStatusNotValidException(ProjectStatusNotValidException exception){
        return new ResponseEntity(Map.of("message",exception.getMessage()), HttpStatus.OK);
    }
}
