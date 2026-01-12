package com.mycompany.myapp.web.rest.errors;

import org.springframework.http.HttpStatus;

public class CatedraException extends RuntimeException {
    private final HttpStatus status;

    public CatedraException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }

    public CatedraException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
