package com.hellomet.user.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pharmacy {
    @SerializedName("_id")
    @Expose

    /* renamed from: id */
    String id;
    @SerializedName("meta_data")
    @Expose
    MetaData meta_data;

    public static class MetaData {
        @SerializedName("address")
        @Expose
        String address;
        String distance;
        @SerializedName("founder")
        @Expose
        String founder;
        @SerializedName("image_url")
        @Expose
        String image_url;
        @SerializedName("latitude")
        @Expose
        String latitude;
        @SerializedName("longitude")
        @Expose
        String longitude;
        @SerializedName("name")
        @Expose
        String name;
        @SerializedName("phone_number")
        @Expose
        String phone_number;
        @SerializedName("status")
        @Expose
        String status;

        public MetaData(String address, String distance, String founder, String image_url, String latitude, String longitude, String name, String phone_number, String status) {
            this.address = address;
            this.distance = distance;
            this.founder = founder;
            this.image_url = image_url;
            this.latitude = latitude;
            this.longitude = longitude;
            this.name = name;
            this.phone_number = phone_number;
            this.status = status;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getFounder() {
            return founder;
        }

        public void setFounder(String founder) {
            this.founder = founder;
        }

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone_number() {
            return phone_number;
        }

        public void setPhone_number(String phone_number) {
            this.phone_number = phone_number;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public Pharmacy(String id, MetaData meta_data2) {
        this.id = id;
        this.meta_data = meta_data2;
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
