package com.keselik.artistinfo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CoverArtApiResponse {

    @JsonProperty("images")
    private List<CoverArtImage> images;

    @JsonProperty("release")
    private String release;

    public List<CoverArtImage> getImages() {
        return images;
    }

    public void setImages(List<CoverArtImage> images) {
        this.images = images;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }
}
