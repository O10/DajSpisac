package com.example.olek.firsttest;

import android.util.Log;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import org.springframework.web.client.RestClientException;

import java.net.URI;

/**
 * Created by olek on 04.08.14.
 */
public class BooksRequest extends SpringAndroidSpiceRequest<BookList>  {

    private String query;

    public BooksRequest(String query) {
        super(BookList.class);
        this.query = query;
    }

    @Override
    public BookList loadDataFromNetwork() throws Exception {

        String url = String.format("http://dajspisac.pl/api/v1/%s", query);
        Log.d("retro",url);

        try {
            return getRestTemplate().getForObject(new URI(url),BookList.class);
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
        return "book." + query;
    }
}