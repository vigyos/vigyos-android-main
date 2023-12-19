package com.vigyos.vigyoscentercrm.Model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class PayBillsModel {

    // Main request model
    public static class RequestModel {
        @SerializedName("user_id")
        private String userId;

        @SerializedName("operator")
        private String operator;

        @SerializedName("canumber")
        private String canumber;

        @SerializedName("amount")
        private String amount;

        @SerializedName("latitude")
        private String latitude;

        @SerializedName("longitude")
        private String longitude;

        @SerializedName("mode")
        private String mode;

        @SerializedName("bill_fetch")
        private Map<String, String> billFetch;

        @SerializedName("mobilenumber")
        private String mobilenumber;

        @SerializedName("accessmodetype")
        private String accessmodetype;

        @SerializedName("transactiontype")
        private String transactiontype;

        // Constructors, getters, and setters
        public RequestModel() { }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public String getCanumber() {
            return canumber;
        }

        public void setCanumber(String canumber) {
            this.canumber = canumber;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
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

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public Map<String, String> getBillFetch() {
            return billFetch;
        }

        public void setBillFetch(Map<String, String> billFetch) {
            this.billFetch = billFetch;
        }

        public String getMobilenumber() {
            return mobilenumber;
        }

        public void setMobilenumber(String mobilenumber) {
            this.mobilenumber = mobilenumber;
        }

        public String getAccessmodetype() {
            return accessmodetype;
        }

        public void setAccessmodetype(String accessmodetype) {
            this.accessmodetype = accessmodetype;
        }

        public String getTransactiontype() {
            return transactiontype;
        }

        public void setTransactiontype(String transactiontype) {
            this.transactiontype = transactiontype;
        }
    }
}