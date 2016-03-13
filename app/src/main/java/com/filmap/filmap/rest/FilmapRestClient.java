package com.filmap.filmap.rest;

import com.loopj.android.http.*;
import cz.msebera.android.httpclient.Header;

/**
 * Created by ivanilson on 2016-03-13.
 *
 * AsyncHttpClient implementation for Filmap.
 * http://loopj.com/android-async-http/
 */
public class FilmapRestClient {

    // URL for api access.
    private static final String BASE_URL = "http://api.filmap.labs.ga/";

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

}
