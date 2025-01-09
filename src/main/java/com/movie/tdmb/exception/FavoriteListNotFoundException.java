package com.movie.tdmb.exception;

public class FavoriteListNotFoundException extends RuntimeException {
    public FavoriteListNotFoundException(String message) {
        super(message);
    }
}
