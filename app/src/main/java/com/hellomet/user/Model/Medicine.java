package com.hellomet.user.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Medicine {
    @SerializedName("_id")
    @Expose

    /* renamed from: id */
    String id;
    @SerializedName("meta_data")
    @Expose
    MetaData meta_data;

    public static class MetaData {
        @SerializedName("brand")
        @Expose
        String brand;
        @SerializedName("description")
        @Expose
        String description;
        @SerializedName("features")
        @Expose
        String features;
        @SerializedName("image_url")
        @Expose
        String image_url;
        @SerializedName("indication")
        @Expose
        String indication;
        @SerializedName("name")
        @Expose
        String name;
        @SerializedName("price")
        @Expose
        String price;

        public MetaData(String name, String image_url, String features, String brand, String indication, String price, String description) {
            this.name = name;
            this.image_url = image_url;
            this.features = features;
            this.brand = brand;
            this.indication = indication;
            this.price = price;
            this.description = description;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name2) {
            this.name = name2;
        }

        public String getImage_url() {
            return this.image_url;
        }

        public void setImage_url(String image_url2) {
            this.image_url = image_url2;
        }

        public String getFeatures() {
            return this.features;
        }

        public void setFeatures(String features2) {
            this.features = features2;
        }

        public String getBrand() {
            return this.brand;
        }

        public void setBrand(String brand2) {
            this.brand = brand2;
        }

        public String getIndication() {
            return this.indication;
        }

        public void setIndication(String indication2) {
            this.indication = indication2;
        }

        public String getPrice() {
            return this.price;
        }

        public void setPrice(String price2) {
            this.price = price2;
        }

        public String getDescription() {
            return this.description;
        }

        public void setDescription(String description2) {
            this.description = description2;
        }
    }



    public Medicine(String id, MetaData meta_data) {
        this.id = id;
        this.meta_data = meta_data;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MetaData getMeta_data() {
        return this.meta_data;
    }

    public void setMeta_data(MetaData meta_data2) {
        this.meta_data = meta_data2;
    }
}
