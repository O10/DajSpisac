package com.swmansion.dajspisac.exercise;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by olek on 19.08.14.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class AttachmentImage {
    private Image image;

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
