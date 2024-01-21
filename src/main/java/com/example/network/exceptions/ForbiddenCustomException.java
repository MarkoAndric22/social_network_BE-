package com.example.network.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class ForbiddenCustomException extends Exception{
    public ForbiddenCustomException(String message) {
            super(message);
        }
}
