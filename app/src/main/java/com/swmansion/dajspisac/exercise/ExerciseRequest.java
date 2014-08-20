package com.swmansion.dajspisac.exercise;

import android.util.Log;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import org.springframework.web.client.RestClientException;

import java.net.URI;

/**
 * Created by olek on 06.08.14.
 */
public class ExerciseRequest extends SpringAndroidSpiceRequest<Exercise> {
    private String query;

    public ExerciseRequest(String query) {
        super(Exercise.class);
        this.query = query;
    }

    @Override
    public Exercise loadDataFromNetwork() throws Exception {

        String url = String.format("http://dajspisac.pl/api/v1/%s", query);
        Log.d("retro", url);

        try {
            return getRestTemplate().getForObject(new URI(url), Exercise.class);
        } catch (RestClientException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method generates a unique cache key for this request. In this case
     * our cache key depends just on the keyword.
     *
     * @return
     */
    public String createCacheKey() {
        return "exercises." + query;
    }
}