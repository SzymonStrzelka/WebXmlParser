package com.sstrzelka.merapar.interview.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidUrlException extends Exception {
    public InvalidUrlException() {
        super("Provided URL address is invalid");
    }
}
