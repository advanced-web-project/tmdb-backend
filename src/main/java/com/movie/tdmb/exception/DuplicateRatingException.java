package com.movie.tdmb.exception;

public class DuplicateRatingException extends RuntimeException {
    public DuplicateRatingException(String message) {
        super(message);
    }
}
