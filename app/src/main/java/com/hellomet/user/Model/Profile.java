package com.hellomet.user.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile {

    public static class MetaData {
        @SerializedName("email")
        @Expose
        String email;
        @SerializedName("image_url")
        @Expose
        String image_url;
        @SerializedName("name")
        @Expose
        String name;
        @SerializedName("phone_number")
        @Expose
        String phone_number;

        public MetaData(String name, String image_url, String phone_number, String email) {
            this.name = name;
            this.image_url = image_url;
            this.phone_number = phone_number;
            this.email = email;

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

        public String getPhone_number() {
            return this.phone_number;
        }

        public void setPhone_number(String phone_number2) {
            this.phone_number = phone_number2;
        }

        public String getEmail() {
            return this.email;
        }

        public void setEmail(String email2) {
            this.email = email2;
        }

    }

    public static class Auth{
        @SerializedName("phone_number")
        @Expose
        String phone_number;
        @SerializedName("password")
        @Expose
        String password;

        public Auth(String phone_number, String password) {
            this.phone_number = phone_number;
            this.password = password;
        }

        public String getPhone_number() {
            return phone_number;
        }

        public void setPhone_number(String phone_number) {
            this.phone_number = phone_number;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }


    @SerializedName("_id")
    @Expose
    String id;

    @SerializedName("meta_data")
    @Expose
    MetaData metaData;

    @SerializedName("auth")
    @Expose
    Auth auth;

    public Profile(MetaData metaData, Auth auth) {
        this.metaData = metaData;
        this.auth = auth;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MetaData getMetaData() {
        return this.metaData;
    }

    public void setMetaData(MetaData metaData2) {
        this.metaData = metaData2;
    }

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }
}
