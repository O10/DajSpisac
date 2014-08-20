package com.swmansion.dajspisac.exercise;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by olek on 19.08.14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Attachment {
    private AttachmentImage image;
    private String type;

    public AttachmentImage getImage() {
        return image;
    }

    public void setImage(AttachmentImage image) {
        this.image = image;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
