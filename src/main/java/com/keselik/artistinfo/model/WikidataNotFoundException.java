package com.keselik.artistinfo.model;

public class WikidataNotFoundException extends RuntimeException {
    public WikidataNotFoundException(String message) {
        super(message);
    }
}