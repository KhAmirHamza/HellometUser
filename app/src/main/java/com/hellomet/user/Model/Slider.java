package com.hellomet.user.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Slider {

    public static class Data {
        @SerializedName("title")
        @Expose
        String title;

        @SerializedName("image_url")
        @Expose
        String image_url;

        public Data(String title, String image_url) {
            this.title = title;
            this.image_url = image_url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }
    }

    @SerializedName("_id")
    @Expose
    String id;

    @SerializedName("data")
    @Expose
    Data data;

    public Slider(Data data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
