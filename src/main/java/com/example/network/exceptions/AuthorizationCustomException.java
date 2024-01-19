package com.example.network.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class AuthorizationCustomException extends Exception{
    public AuthorizationCustomException(String message) {
        super(message);
    }
}
