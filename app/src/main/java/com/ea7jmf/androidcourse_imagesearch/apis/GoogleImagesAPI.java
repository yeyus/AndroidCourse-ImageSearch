package com.ea7jmf.androidcourse_imagesearch.apis;

import android.renderscript.RSInvalidStateException;

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
    private static final String PARAM_START = "start";
    private static final String PARAM_SIZE = "imgsz";
    private static final String PARAM_COLOR = "imgcolor";
    private static final String PARAM_TYPE = "imgtype";
    private static final String PARAM_SITE = "as_sitesearch";

    private static final String ANY = "any";

    private static final String API_VERSION = "1.0";

    private AsyncHttpClient client;

    private int pageSize = 8;
    private int start = 0;
    private String color = ANY;
    private String size = ANY;
    private String type = ANY;
    private String site;

    private String query = null;

    public GoogleImagesAPI() {
        this.client = new AsyncHttpClient();
    }

    public RequestHandle query(ResponseHandlerInterface responseHandler) throws IllegalStateException {
        return client.get(buildQueryURL(query), responseHandler);
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String getQuery() {
        return query;
    }

    /* Sets the query and resets cursor */
    public void setQuery(String query) {
        this.query = query;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public int nextPage() {
        return this.start + this.pageSize;
    }

    private String buildQueryURL(String query) {
        StringBuffer endpoint = new StringBuffer(ENDPOINT + "?" + PARAM_VERSION + "=" + API_VERSION +
                "&" + PARAM_QUERY + "=" + query +
                "&" + PARAM_PAGE_SIZE + "=" + pageSize +
                "&" + PARAM_START + "=" + start);

        if(size != ANY && !size.isEmpty()) {
            endpoint.append("&" + PARAM_SIZE + "=" + size);
        }

        if(type != ANY && !type.isEmpty()) {
            endpoint.append("&" + PARAM_TYPE + "=" + type);
        }

        if(color != ANY && !color.isEmpty()) {
            endpoint.append("&" + PARAM_COLOR + "=" + color);
        }

        return endpoint.toString();
    }
}
