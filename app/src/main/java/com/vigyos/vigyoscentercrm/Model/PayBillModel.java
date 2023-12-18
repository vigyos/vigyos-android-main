package com.vigyos.vigyoscentercrm.Model;

import com.google.gson.annotations.SerializedName;

public class PayBillModel {

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
    private BillFetch billFetch;

    @SerializedName("mobilenumber")
    private String mobilenumber;

    @SerializedName("accessmodetype")
    private String accessmodetype;

    @SerializedName("transactiontype")
    private String transactiontype;

    // Constructor, getters, and setters go here

    public static class BillFetch {

        @SerializedName("billAmount")
        private String billAmount;

        @SerializedName("billnetamount")
        private String billnetamount;

        @SerializedName("billdate")
        private String billdate;

        @SerializedName("dueDate")
        private String dueDate;

        @SerializedName("minBillAmount")
        private String minBillAmount;

        @SerializedName("acceptPayment")
        private boolean acceptPayment;

        @SerializedName("acceptPartPay")
        private boolean acceptPartPay;

        @SerializedName("cellNumber")
        private String cellNumber;

        @SerializedName("userName")
        private String userName;

        // Constructors, getters, and setters go here

        public String getMinBillAmount() {
            return minBillAmount;
        }

        public void setMinBillAmount(String minBillAmount) {
            this.minBillAmount = minBillAmount;
        }

        public String getBillAmount() {
            return billAmount;
        }

        public void setBillAmount(String billAmount) {
            this.billAmount = billAmount;
        }

        public String getBillnetamount() {
            return billnetamount;
        }

        public void setBillnetamount(String billnetamount) {
            this.billnetamount = billnetamount;
        }

        public String getBilldate() {
            return billdate;
        }

        public void setBilldate(String billdate) {
            this.billdate = billdate;
        }

        public String getDueDate() {
            return dueDate;
        }

        public void setDueDate(String dueDate) {
            this.dueDate = dueDate;
        }

        public boolean isAcceptPayment() {
            return acceptPayment;
        }

        public void setAcceptPayment(boolean acceptPayment) {
            this.acceptPayment = acceptPayment;
        }

        public boolean isAcceptPartPay() {
            return acceptPartPay;
        }

        public void setAcceptPartPay(boolean acceptPartPay) {
            this.acceptPartPay = acceptPartPay;
        }

        public String getCellNumber() {
            return cellNumber;
        }

        public void setCellNumber(String cellNumber) {
            this.cellNumber = cellNumber;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }


    public PayBillModel() { }

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

    public BillFetch getBillFetch() {
        return billFetch;
    }

    public void setBillFetch(BillFetch billFetch) {
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
