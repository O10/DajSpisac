package com.swmansion.dajspisac.exercise;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by olek on 19.08.14.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Image {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
