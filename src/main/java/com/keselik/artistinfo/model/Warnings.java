package com.keselik.artistinfo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Warnings {

    @JsonProperty("extracts")
    private Extracts extracts;

    public Extracts getExtracts() {
        return extracts;
    }

    public void setExtracts(Extracts extracts) {
        this.extracts = extracts;
    }
}
