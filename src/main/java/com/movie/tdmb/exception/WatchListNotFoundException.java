package com.movie.tdmb.exception;

public class WatchListNotFoundException extends RuntimeException {
    public WatchListNotFoundException(String message) {
        super(message);
    }
}
