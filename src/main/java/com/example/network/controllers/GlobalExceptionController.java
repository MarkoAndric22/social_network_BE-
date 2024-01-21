package com.example.network.controllers;

import com.example.network.exceptions.AuthorizationCustomException;
import com.example.network.exceptions.BadRequestCustomException;
import com.example.network.exceptions.ForbiddenCustomException;
import com.example.network.exceptions.NotFoundCustomException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, List<String>>> exceptionHandler(Exception exception) {
        String errors;
        if (exception.getMessage() != null) {
            errors = exception.getMessage();
        } else if (exception.getCause() != null) {
            errors = exception.getCause().toString();
        } else {
            errors = exception.toString();
        }

        return new ResponseEntity<>(getErrorsMap(List.of(errors)), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthorizationCustomException.class)
    public ResponseEntity<Map<String, List<String>>> handleAuthorizationCustomException(AuthorizationCustomException ex) {
        return new ResponseEntity<>(getErrorsMap(ex.getMessage()), new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadRequestCustomException.class)
    public ResponseEntity<Map<String, List<String>>> handleBadRequestCustomException(BadRequestCustomException ex) {
        return new ResponseEntity<>(getErrorsMap(ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundCustomException.class)
    public ResponseEntity<Map<String, List<String>>> notFoundCustomException(NotFoundCustomException ex) {
        return new ResponseEntity<>(getErrorsMap(ex.getMessage()), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ForbiddenCustomException.class)
    public ResponseEntity<Map<String, List<String>>> forbiddenCustomException(ForbiddenCustomException ex) {
        return new ResponseEntity<>(getErrorsMap(ex.getMessage()), new HttpHeaders(), HttpStatus.FORBIDDEN);
    }

    private Map<String, List<String>> getErrorsMap(String error) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", Collections.singletonList(error));
        return errorResponse;
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }
}
