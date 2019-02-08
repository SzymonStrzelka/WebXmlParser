package com.sstrzelka.merapar.interview.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class HostUnreachableException extends Exception {
    public HostUnreachableException() {
        super("URL address is invalid or couldn't connect to host");
    }
}
