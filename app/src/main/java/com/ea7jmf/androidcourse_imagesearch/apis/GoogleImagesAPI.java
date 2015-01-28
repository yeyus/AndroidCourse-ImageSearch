package com.ea7jmf.androidcourse_imagesearch.apis;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.ResponseHandlerInterface;

import java.net.URLEncoder;

/**
 * Created by yeyus on 1/27/15.
 */
public class GoogleImagesAPI {

    private static final String ENDPOINT = "http://ajax.googleapis.com/ajax/services/search/images";

    private static final String PARAM_VERSION = "v";
    private static final String PARAM_QUERY = "q";
    private static final String PARAM_PAGE_SIZE = "rsz";

    private static final String API_VERSION = "1.0";

    private AsyncHttpClient client;

    private int pageSize = 8;
    private int page = 0;

    public GoogleImagesAPI() {
        this.client = new AsyncHttpClient();
    }

    public RequestHandle executeQuery(String query, ResponseHandlerInterface responseHandler) {
        return client.get(buildQueryURL(query), responseHandler);
    }

    private String buildQueryURL(String query) {
        return ENDPOINT + "?" + PARAM_VERSION + "=" + API_VERSION +
                "&" + PARAM_QUERY + "=" + query +
                "&" + PARAM_PAGE_SIZE + "=" + pageSize;
    }
}
