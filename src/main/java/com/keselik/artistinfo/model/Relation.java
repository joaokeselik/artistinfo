package com.keselik.artistinfo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Relation {

    @JsonProperty("url")
    private RelationUrl url;

    @JsonProperty("target-type")
    private String targetType;

    @JsonProperty("type")
    private String type;


    @JsonProperty("source-credit")
    private String sourceCredit;

    @JsonProperty("end")
    private String end;

    @JsonProperty("direction")
    private String direction;

    @JsonProperty("target-credit")
    private String targetCredit;

    @JsonProperty("begin")
    private String begin;

    @JsonProperty("ended")
    private boolean ended;

    @JsonProperty("type-id")
    private String typeId;

    // Additional fields as needed

    // Getters and setters

    public RelationUrl getUrl() {
        return url;
    }

    public void setUrl(RelationUrl relationUrl) {
        this.url = relationUrl;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSourceCredit() {
        return sourceCredit;
    }

    public void setSourceCredit(String sourceCredit) {
        this.sourceCredit = sourceCredit;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getTargetCredit() {
        return targetCredit;
    }

    public void setTargetCredit(String targetCredit) {
        this.targetCredit = targetCredit;
    }

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
}