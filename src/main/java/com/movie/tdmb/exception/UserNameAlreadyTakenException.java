package com.movie.tdmb.exception;

public class UserNameAlreadyTakenException extends RuntimeException {
    public UserNameAlreadyTakenException(String message) {
        super(message);
    }
}
