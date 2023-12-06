package com.keselik.musicinfo.model;

public class WikidataNotFoundException extends RuntimeException {
    public WikidataNotFoundException(String message) {
        super(message);
    }
}