package com.movie.tdmb.exception;

public class DuplicateWatchListException extends RuntimeException {
    public DuplicateWatchListException(String message) {
        super(message);
    }
}
