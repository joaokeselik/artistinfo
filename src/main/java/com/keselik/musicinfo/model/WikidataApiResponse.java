package com.keselik.musicinfo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WikidataApiResponse {

    @JsonProperty("entities")
    private Map<String, WikidataEntity> entities;

    @JsonProperty("success")
    private int success;

    public Map<String, WikidataEntity> getEntities() {
        return entities;
    }

    public void setEntities(Map<String, WikidataEntity> entities) {
        this.entities = entities;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }
}
