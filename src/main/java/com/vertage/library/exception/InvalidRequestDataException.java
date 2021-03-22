package com.vertage.library.exception;

public class InvalidRequestDataException extends RuntimeException {

    public InvalidRequestDataException() {
    }

    public InvalidRequestDataException(String message) {
        super(message);
    }
}
