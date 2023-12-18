package com.vigyos.vigyoscentercrm.Model;

public class PayBillsModel {

    String user_id;
    String operator;
    String canumber;
    String amount;
    String latitude;
    String longitude;
    String mode;
    String mobilenumber;
    String accessmodetype;
    String transactiontype;

    private BillFetch bill_fetch;

    public PayBillsModel() {
        this.bill_fetch = new BillFetch();
    }

    public BillFetch getBill_fetch() {
        return bill_fetch;
    }

    public void setBill_fetch(BillFetch bill_fetch) {
        this.bill_fetch = bill_fetch;
    }


    // Nested class representing the "bill_fetch" object
    public static class BillFetch {
        private String billAmount;
        private String billnetamount;
        private String billdate;
        private String dueDate;
        private boolean acceptPayment;
        private boolean acceptPartPay;
        private String cellNumber;
        private String userName;

        // Constructors, getters, and setters go here

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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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
