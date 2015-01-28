package com.ea7jmf.androidcourse_imagesearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by yeyus on 1/27/15.
 */
public class ImageResult implements Serializable {

    private static final long serialVersionUID = -39480239840923L;
    private String fullUrl;
    private String thumbUrl;
    private String title;
    private int height;
    private int width;

    public ImageResult(String fullUrl, String thumbUrl, String title, int height, int width) {
        this.fullUrl = fullUrl;
        this.thumbUrl = thumbUrl;
        this.title = title;
        this.height = height;
        this.width = width;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public static ImageResult fromJSONObject(JSONObject ir) throws JSONException {
        ImageResult imageResult = new ImageResult(
                ir.getString("url"),
                ir.getString("tbUrl"),
                ir.getString("title"),
                Integer.parseInt(ir.getString("height")),
                Integer.parseInt(ir.getString("width")));

        return imageResult;
    };

    public static ArrayList<ImageResult> fromJSONArray(JSONArray ja) throws JSONException {
        ArrayList<ImageResult> imageResults = new ArrayList<>();

        for (int i = 0; i < ja.length(); i++) {
            imageResults.add(ImageResult.fromJSONObject(ja.getJSONObject(i)));
        }

        return imageResults;
    }

    @Override
    public String toString() {
        return "ImageResult{" +
                "fullUrl='" + fullUrl + '\'' +
                ", thumbUrl='" + thumbUrl + '\'' +
                ", title='" + title + '\'' +
                ", height=" + height +
                ", width=" + width +
                '}';
    }
}
