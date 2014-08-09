package com.example.olek.firsttest;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import org.springframework.web.client.RestClientException;

//Create a request in its own Java file, it should not an inner class of a Context
public class FollowersRequest extends SpringAndroidSpiceRequest<FollowerList> {

    private String user;

    public FollowersRequest(String user) {
        super(FollowerList.class);
        this.user = user;
    }

    @Override
    public FollowerList loadDataFromNetwork() throws Exception {

        String url = String.format("https://api.github.com/users/%s/followers", user);

        try {
            return getRestTemplate().getForObject(url, FollowerList.class);
        } catch (RestClientException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method generates a unique cache key for this request. In this case
     * our cache key depends just on the keyword.
     * @return
     */
    public String createCacheKey() {
        return "followers." + user;
    }
}