package com.voidsong.eccu.exceptions;

public class SecurityErrorException extends Exception {
    public SecurityErrorException() {
    }

    public SecurityErrorException(String message) {
        super(message);
    }
}
