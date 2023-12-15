package com.keselik.artistinfo.exception;

public class WikidataNotFoundException extends RuntimeException {
    public WikidataNotFoundException(String message) {
        super(message);
    }
}