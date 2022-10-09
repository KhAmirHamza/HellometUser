package com.hellomet.user.Room;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


public class MedicineEntityRoom {

    public static class MetaData {
        String brand;
        String description;
        String features;
        String image_url;
        String indication;
        String name;
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

        public void setName(String name) {
            this.name = name;
        }

        public String getImage_url() {
            return this.image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }

        public String getFeatures() {
            return this.features;
        }

        public void setFeatures(String features) {
            this.features = features;
        }

        public String getBrand() {
            return this.brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
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

        public void setPrice(String price) {
            this.price = price;
        }

        public String getDescription() {
            return this.description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    @Entity
    public static class Medicine {

        /* renamed from: id */
        @PrimaryKey(autoGenerate = true)
        public int id;

        public String medicineId;

        @Embedded
        public MetaData metaData;

        public Medicine(String medicineId, MetaData metaData) {
            this.medicineId = medicineId;
            this.metaData = metaData;
        }

        public int getId() {
            return this.id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMedicineId() {
            return this.medicineId;
        }

        public void setMedicineId(String medicineId2) {
            this.medicineId = medicineId2;
        }

        public MetaData getMetaData() {
            return this.metaData;
        }

        public void setMetaData(MetaData metaData2) {
            this.metaData = metaData2;
        }
    }
}
