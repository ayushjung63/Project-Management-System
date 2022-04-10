package com.ayush.proms.exception;

import com.ayush.proms.exception.project.ProjectStatusNotValidException;
import com.ayush.proms.utils.BaseController;
import com.ayush.proms.utils.CustomMessageSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestController
@ControllerAdvice
public class ProjectExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private CustomMessageSource customMessageSource;

    @ExceptionHandler(ProjectStatusNotValidException.class)
    public ResponseEntity projectStatusNotValidException(ProjectStatusNotValidException exception){
        return ResponseEntity.ok(customMessageSource.get("not.foung",customMessageSource.get("project")));
    }
}
