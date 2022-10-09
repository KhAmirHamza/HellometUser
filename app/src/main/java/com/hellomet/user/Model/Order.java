package com.hellomet.user.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Order {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("meta_data")
    @Expose
    private MetaData metaData;
    @SerializedName("items")
    @Expose
    private List<Item> items = null;
    @SerializedName("prescriptionImageUrls")
    @Expose
    private List<PrescriptionImageURL> prescriptionImageUrls = null;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public Order() {
    }

    public Order(MetaData metaData) {
        super();
        this.metaData = metaData;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<PrescriptionImageURL> getPrescriptionImageUrls() {
        return prescriptionImageUrls;
    }

    public void setPrescriptionImageUrls(List<Order.PrescriptionImageURL> prescriptionImageUrls) {
        this.prescriptionImageUrls = prescriptionImageUrls;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }


    public static class PrescriptionImageURL {
        @SerializedName("prescriptionImageUrl")
        @Expose
        String prescriptionImageUrl;

        public PrescriptionImageURL(String prescriptionImageUrl) {
            this.prescriptionImageUrl = prescriptionImageUrl;
        }

        public String getPrescriptionImageUrl() {
            return prescriptionImageUrl;
        }

        public void setPrescriptionImageUrl(String prescriptionImageUrl) {
            this.prescriptionImageUrl = prescriptionImageUrl;
        }
    }


    public static class Item {

        @SerializedName("brand")
        @Expose
        private String brand;
        @SerializedName("features")
        @Expose
        private String features;
        @SerializedName("medicine_id")
        @Expose
        private String medicineId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("quantity")
        @Expose
        private String quantity;
        @SerializedName("sub_total")
        @Expose
        private String subTotal;


        public Item() {
        }


        public Item(String medicineId, String name, String price, String quantity, String brand, String features, String subTotal) {
            super();
            this.brand = brand;
            this.features = features;
            this.medicineId = medicineId;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
            this.subTotal = subTotal;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getFeatures() {
            return features;
        }

        public void setFeatures(String features) {
            this.features = features;
        }

        public String getMedicineId() {
            return medicineId;
        }

        public void setMedicineId(String medicineId) {
            this.medicineId = medicineId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getSubTotal() {
            return subTotal;
        }

        public void setSubTotal(String subTotal) {
            this.subTotal = subTotal;
        }

    }

    public static class MetaData {

        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("payment_method")
        @Expose
        private String paymentMethod;
        @SerializedName("payment_status")
        @Expose
        private String paymentStatus;
        @SerializedName("requirement")
        @Expose
        private String requirement;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("total_price")
        @Expose
        private String totalPrice;
        @SerializedName("pharmacy_address")
        @Expose
        private String pharmacyAddress;
        @SerializedName("pharmacy_id")
        @Expose
        private String pharmacyId;
        @SerializedName("pharmacy_lat")
        @Expose
        private String pharmacyLat;
        @SerializedName("pharmacy_lng")
        @Expose
        private String pharmacyLng;
        @SerializedName("pharmacy_name")
        @Expose
        private String pharmacyName;
        @SerializedName("pharmacy_phone_number")
        @Expose
        private String pharmacyPhoneNumber;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("user_lat")
        @Expose
        private String userLat;
        @SerializedName("user_lng")
        @Expose
        private String userLng;
        @SerializedName("user_name")
        @Expose
        private String userName;
        @SerializedName("user_address")
        @Expose
        private String userAddress;
        @SerializedName("user_phone_number")
        @Expose
        private String userPhoneNumber;
        @SerializedName("rider_id")
        @Expose
        private String riderId;
        @SerializedName("rider_name")
        @Expose
        private String riderName;
        @SerializedName("rider_phone_number")
        @Expose
        private String riderPhoneNumber;
        @SerializedName("_id")
        @Expose
        private String id;


        public MetaData() {
        }

        public MetaData(String createdAt, String paymentMethod, String paymentStatus, String requirement,
                        String status, String totalPrice, String pharmacyAddress, String pharmacyId,
                        String pharmacyLat, String pharmacyLng, String pharmacyName, String pharmacyPhoneNumber,
                        String userId, String userLat, String userLng, String userName, String userAddress,
                        String userPhoneNumber, String riderId, String riderName, String riderPhoneNumber) {
            super();
            this.createdAt = createdAt;
            this.paymentMethod = paymentMethod;
            this.paymentStatus = paymentStatus;
            this.requirement = requirement;
            this.status = status;
            this.totalPrice = totalPrice;
            this.pharmacyAddress = pharmacyAddress;
            this.pharmacyId = pharmacyId;
            this.pharmacyLat = pharmacyLat;
            this.pharmacyLng = pharmacyLng;
            this.pharmacyName = pharmacyName;
            this.pharmacyPhoneNumber = pharmacyPhoneNumber;
            this.userId = userId;
            this.userLat = userLat;
            this.userLng = userLng;
            this.userName = userName;
            this.userAddress = userAddress;
            this.userPhoneNumber = userPhoneNumber;
            this.riderId = riderId;
            this.riderName = riderName;
            this.riderPhoneNumber = riderPhoneNumber;

        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public String getPaymentStatus() {
            return paymentStatus;
        }

        public void setPaymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
        }

        public String getRequirement() {
            return requirement;
        }

        public void setRequirement(String requirement) {
            this.requirement = requirement;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(String totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getPharmacyAddress() {
            return pharmacyAddress;
        }

        public void setPharmacyAddress(String pharmacyAddress) {
            this.pharmacyAddress = pharmacyAddress;
        }

        public String getPharmacyId() {
            return pharmacyId;
        }

        public void setPharmacyId(String pharmacyId) {
            this.pharmacyId = pharmacyId;
        }

        public String getPharmacyLat() {
            return pharmacyLat;
        }

        public void setPharmacyLat(String pharmacyLat) {
            this.pharmacyLat = pharmacyLat;
        }

        public String getPharmacyLng() {
            return pharmacyLng;
        }

        public void setPharmacyLng(String pharmacyLng) {
            this.pharmacyLng = pharmacyLng;
        }

        public String getPharmacyName() {
            return pharmacyName;
        }

        public void setPharmacyName(String pharmacyName) {
            this.pharmacyName = pharmacyName;
        }

        public String getPharmacyPhoneNumber() {
            return pharmacyPhoneNumber;
        }

        public void setPharmacyPhoneNumber(String pharmacyPhoneNumber) {
            this.pharmacyPhoneNumber = pharmacyPhoneNumber;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserLat() {
            return userLat;
        }

        public void setUserLat(String userLat) {
            this.userLat = userLat;
        }

        public String getUserLng() {
            return userLng;
        }

        public void setUserLng(String userLng) {
            this.userLng = userLng;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserAddress() {
            return userAddress;
        }

        public void setUserAddress(String userAddress) {
            this.userAddress = userAddress;
        }

        public String getUserPhoneNumber() {
            return userPhoneNumber;
        }

        public void setUserPhoneNumber(String userPhoneNumber) {
            this.userPhoneNumber = userPhoneNumber;
        }

        public String getRiderId() {
            return riderId;
        }

        public void setRiderId(String riderId) {
            this.riderId = riderId;
        }

        public String getRiderName() {
            return riderName;
        }

        public void setRiderName(String riderName) {
            this.riderName = riderName;
        }

        public String getRiderPhoneNumber() {
            return riderPhoneNumber;
        }

        public void setRiderPhoneNumber(String riderPhoneNumber) {
            this.riderPhoneNumber = riderPhoneNumber;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }





/*    @SerializedName("_id")
    @Expose
    String id;


    @SerializedName("items")
    @Expose
    List<Item> items;

    @SerializedName("prescriptionImageUrls")
    @Expose
    List<PrescriptionImageURL> prescriptionImageUrls;

    @SerializedName("meta_data")
    @Expose
    MetaData meta_data;

    public static class MetaData {
        @SerializedName("created_at")
        @Expose
        String created_at;
        @SerializedName("rider_id")
        @Expose
        String rider_id;
        @SerializedName("rider_name")
        @Expose
        String rider_name;
        @SerializedName("rider_phone_number")
        @Expose
        String rider_phone_number;
        @SerializedName("payment_method")
        @Expose
        String payment_method;
        @SerializedName("payment_status")
        @Expose
        String payment_status;
        @SerializedName("pharmacy_address")
        @Expose
        String pharmacy_address;
        @SerializedName("pharmacy_id")
        @Expose
        String pharmacy_id;
        @SerializedName("pharmacy_lat")
        @Expose
        String pharmacy_lat;
        @SerializedName("pharmacy_lng")
        @Expose
        String pharmacy_lng;
        @SerializedName("pharmacy_name")
        @Expose
        String pharmacy_name;
        @SerializedName("pharmacy_phone_number")
        @Expose
        String pharmacy_phone_number;
        @SerializedName("requirement")
        @Expose
        String requirement;
        @SerializedName("status")
        @Expose
        String status;
        @SerializedName("total_price")
        @Expose
        String total_price;
        @SerializedName("user_address")
        @Expose
        String user_address;
        @SerializedName("user_id")
        @Expose
        String user_id;
        @SerializedName("user_lat")
        @Expose
        String user_lat;
        @SerializedName("user_lng")
        @Expose
        String user_lng;
        @SerializedName("user_name")
        @Expose
        String user_name;
        @SerializedName("user_phone_number")
        @Expose
        String user_phone_number;

        public MetaData(String created_at, String rider_id, String rider_name, String rider_phone_number, String payment_method, String payment_status, String pharmacy_address, String pharmacy_id, String pharmacy_lat, String pharmacy_lng, String pharmacy_name, String pharmacy_phone_number, String requirement, String status, String total_price, String user_address, String user_id, String user_lat, String user_lng, String user_name, String user_phone_number) {
            this.created_at = created_at;
            this.rider_id = rider_id;
            this.rider_name = rider_name;
            this.rider_phone_number = rider_phone_number;
            this.payment_method = payment_method;
            this.payment_status = payment_status;
            this.pharmacy_address = pharmacy_address;
            this.pharmacy_id = pharmacy_id;
            this.pharmacy_lat = pharmacy_lat;
            this.pharmacy_lng = pharmacy_lng;
            this.pharmacy_name = pharmacy_name;
            this.pharmacy_phone_number = pharmacy_phone_number;
            this.requirement = requirement;
            this.status = status;
            this.total_price = total_price;
            this.user_address = user_address;
            this.user_id = user_id;
            this.user_lat = user_lat;
            this.user_lng = user_lng;
            this.user_name = user_name;
            this.user_phone_number = user_phone_number;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getRider_id() {
            return rider_id;
        }

        public void setRider_id(String rider_id) {
            this.rider_id = rider_id;
        }

        public String getRider_name() {
            return rider_name;
        }

        public void setRider_name(String rider_name) {
            this.rider_name = rider_name;
        }

        public String getRider_phone_number() {
            return rider_phone_number;
        }

        public void setRider_phone_number(String rider_phone_number) {
            this.rider_phone_number = rider_phone_number;
        }

        public String getPayment_method() {
            return payment_method;
        }

        public void setPayment_method(String payment_method) {
            this.payment_method = payment_method;
        }

        public String getPayment_status() {
            return payment_status;
        }

        public void setPayment_status(String payment_status) {
            this.payment_status = payment_status;
        }

        public String getPharmacy_address() {
            return pharmacy_address;
        }

        public void setPharmacy_address(String pharmacy_address) {
            this.pharmacy_address = pharmacy_address;
        }

        public String getPharmacy_id() {
            return pharmacy_id;
        }

        public void setPharmacy_id(String pharmacy_id) {
            this.pharmacy_id = pharmacy_id;
        }

        public String getPharmacy_lat() {
            return pharmacy_lat;
        }

        public void setPharmacy_lat(String pharmacy_lat) {
            this.pharmacy_lat = pharmacy_lat;
        }

        public String getPharmacy_lng() {
            return pharmacy_lng;
        }

        public void setPharmacy_lng(String pharmacy_lng) {
            this.pharmacy_lng = pharmacy_lng;
        }

        public String getPharmacy_name() {
            return pharmacy_name;
        }

        public void setPharmacy_name(String pharmacy_name) {
            this.pharmacy_name = pharmacy_name;
        }

        public String getPharmacy_phone_number() {
            return pharmacy_phone_number;
        }

        public void setPharmacy_phone_number(String pharmacy_phone_number) {
            this.pharmacy_phone_number = pharmacy_phone_number;
        }

        public String getRequirement() {
            return requirement;
        }

        public void setRequirement(String requirement) {
            this.requirement = requirement;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTotal_price() {
            return total_price;
        }

        public void setTotal_price(String total_price) {
            this.total_price = total_price;
        }

        public String getUser_address() {
            return user_address;
        }

        public void setUser_address(String user_address) {
            this.user_address = user_address;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUser_lat() {
            return user_lat;
        }

        public void setUser_lat(String user_lat) {
            this.user_lat = user_lat;
        }

        public String getUser_lng() {
            return user_lng;
        }

        public void setUser_lng(String user_lng) {
            this.user_lng = user_lng;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getUser_phone_number() {
            return user_phone_number;
        }

        public void setUser_phone_number(String user_phone_number) {
            this.user_phone_number = user_phone_number;
        }
    }

    public static class PrescriptionImageURL{
        @SerializedName("prescriptionImageUrl")
        @Expose
        String prescriptionImageUrl;

        public PrescriptionImageURL(String prescriptionImageUrl) {
            this.prescriptionImageUrl = prescriptionImageUrl;
        }

        public String getPrescriptionImageUrl() {
            return prescriptionImageUrl;
        }

        public void setPrescriptionImageUrl(String prescriptionImageUrl) {
            this.prescriptionImageUrl = prescriptionImageUrl;
        }
    }

    public static class Item {
        @SerializedName("brand")
        @Expose
        String brand;
        @SerializedName("features")
        @Expose
        String features;
        @SerializedName("medicine_id")
        @Expose
        String medicine_id;
        @SerializedName("name")
        @Expose
        String name;
        @SerializedName("price")
        @Expose
        String price;
        @SerializedName("quantity")
        @Expose
        String quantity;
        @SerializedName("sub_total")
        @Expose
        String sub_total;

        public Item(String medicine_id2, String name2, String price2, String quantity2, String brand2, String features2, String sub_total2) {
            this.medicine_id = medicine_id2;
            this.name = name2;
            this.price = price2;
            this.quantity = quantity2;
            this.brand = brand2;
            this.features = features2;
            this.sub_total = sub_total2;
        }

        public String getMedicine_id() {
            return this.medicine_id;
        }

        public void setMedicine_id(String medicine_id2) {
            this.medicine_id = medicine_id2;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name2) {
            this.name = name2;
        }

        public String getPrice() {
            return this.price;
        }

        public void setPrice(String price2) {
            this.price = price2;
        }

        public String getQuantity() {
            return this.quantity;
        }

        public void setQuantity(String quantity2) {
            this.quantity = quantity2;
        }

        public String getBrand() {
            return this.brand;
        }

        public void setBrand(String brand2) {
            this.brand = brand2;
        }

        public String getFeatures() {
            return this.features;
        }

        public void setFeatures(String features2) {
            this.features = features2;
        }

        public String getSub_total() {
            return this.sub_total;
        }

        public void setSub_total(String sub_total2) {
            this.sub_total = sub_total2;
        }
    }



    public Order(MetaData meta_data) {
        this.meta_data = meta_data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<PrescriptionImageURL> getPrescriptionImageUrls() {
        return prescriptionImageUrls;
    }

    public void setPrescriptionImageUrls(List<PrescriptionImageURL> prescriptionImageUrls) {
        this.prescriptionImageUrls = prescriptionImageUrls;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public MetaData getMeta_data() {
        return meta_data;
    }

    public void setMeta_data(MetaData meta_data) {
        this.meta_data = meta_data;
    }*/
    }
}
