package com.filmap.filmap.rest;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by duartemac on 2016-03-13.
 */
public class OMDBRestClient {


    // URL for api access.
    private static final String BASE_URL = "http://www.omdbapi.com/?";
    private static final String BY_ID = "i";
    private static final String BY_SEARCH = "s";

    // AsyncHttpClient, as found in http://loopj.com/android-async-http/
    private static AsyncHttpClient client = new AsyncHttpClient();

    // Get request
    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    // Duh...
    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    // Get URL for api call.
    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    public static void getFilm(String id, AsyncHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put(BY_ID, id);
        get(null, params, responseHandler);
    }

    public static void searchFilm(String query, AsyncHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put(BY_SEARCH, query);
        get(null, params, responseHandler);
    }


}
