package com.sstrzelka.merapar.interview.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidXmlException extends Exception {
    public InvalidXmlException() {
        super("XML file is invalid");
    }
}
