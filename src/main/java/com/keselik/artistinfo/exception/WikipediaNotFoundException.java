package com.keselik.artistinfo.exception;

public class WikipediaNotFoundException extends RuntimeException {
    public WikipediaNotFoundException(String message) {
        super(message);
    }
}
