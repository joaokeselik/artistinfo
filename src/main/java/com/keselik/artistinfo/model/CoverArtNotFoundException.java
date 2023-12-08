package com.keselik.artistinfo.model;

public class CoverArtNotFoundException extends RuntimeException {
    public CoverArtNotFoundException(String message) {
        super(message);
    }
}
