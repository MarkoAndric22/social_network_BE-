package com.example.network.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundCustomException extends Exception{
    public NotFoundCustomException(String message) {
        super(message);
    }
}
