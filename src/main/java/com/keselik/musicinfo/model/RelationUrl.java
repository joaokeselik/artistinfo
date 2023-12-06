package com.keselik.musicinfo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RelationUrl {

    private String id;
    private String resource;

    // Additional fields as needed

    // Constructors

    public RelationUrl() {
    }

    public RelationUrl(String id, String resource) {
        this.id = id;
        this.resource = resource;
    }

    // Getters and setters

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("resource")
    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    // Additional getters and setters

    @Override
    public String toString() {
        return "Url{" +
                "id='" + id + '\'' +
                ", resource='" + resource + '\'' +
                // Additional fields
                '}';
    }
}