package com.keselik.artistinfo.model;

public class WikipediaNotFoundException extends RuntimeException {
    public WikipediaNotFoundException(String message) {
        super(message);
    }
}
