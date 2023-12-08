package com.keselik.artistinfo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CoverArtImage {

    @JsonProperty("approved")
    private boolean approved;

    @JsonProperty("back")
    private boolean back;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("edit")
    private int edit;

    @JsonProperty("front")
    private boolean front;

    @JsonProperty("id")
    private long id;

    @JsonProperty("image")
    private String image;

    @JsonProperty("thumbnails")
    private Map<String, String> thumbnails;

    @JsonProperty("types")
    private List<String> types;

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean isBack() {
        return back;
    }

    public void setBack(boolean back) {
        this.back = back;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getEdit() {
        return edit;
    }

    public void setEdit(int edit) {
        this.edit = edit;
    }

    public boolean isFront() {
        return front;
    }

    public void setFront(boolean front) {
        this.front = front;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Map<String, String> getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(Map<String, String> thumbnails) {
        this.thumbnails = thumbnails;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }
}
