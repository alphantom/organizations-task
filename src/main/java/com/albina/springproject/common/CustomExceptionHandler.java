package com.albina.springproject.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.StringJoiner;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(ex.getLocalizedMessage());
        return new ResponseEntity<>(error, HttpStatus.OK);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<ErrorResponse> handleValidationExceptions(ConstraintViolationException ex, WebRequest request) {
        StringJoiner message = new StringJoiner("; ");
        ex.getConstraintViolations().stream().forEach(violation -> {
            message.add(violation.getMessage());
        });
        ErrorResponse error = new ErrorResponse(message.toString());
        return new ResponseEntity<>(error, HttpStatus.OK);
    }

}
