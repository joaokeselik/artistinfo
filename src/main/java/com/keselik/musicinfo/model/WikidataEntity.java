package com.keselik.musicinfo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WikidataEntity {

    @JsonProperty("type")
    private String type;

    @JsonProperty("id")
    private String id;

    @JsonProperty("sitelinks")
    private Map<String, Sitelink> sitelinks;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Sitelink> getSitelinks() {
        return sitelinks;
    }

    public void setSitelinks(Map<String, Sitelink> sitelinks) {
        this.sitelinks = sitelinks;
    }
}