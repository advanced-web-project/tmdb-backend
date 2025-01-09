package com.movie.tdmb.exception;

public class DuplicateFavoriteListException extends RuntimeException {
    public DuplicateFavoriteListException(String message) {
        super(message);
    }
}
