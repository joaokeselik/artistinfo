package com.keselik.artistinfo.exception;

public class CoverArtNotFoundException extends RuntimeException {
    public CoverArtNotFoundException(String message) {
        super(message);
    }
}
