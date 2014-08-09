package com.example.olek.firsttest;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by olek on 03.08.14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Follower {

    private String login;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}