package com.vigyos.vigyoscentercrm.Model;

public class CompletedItemModel {

    private String service_name;
    private String status;
    private String user_service_id;
    private String customer_name;
    private String customer_phone;
    private int price;
    private String wallet_transaction_amount;
    private String created_time;

    public CompletedItemModel() { }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser_service_id() {
        return user_service_id;
    }

    public void setUser_service_id(String user_service_id) {
        this.user_service_id = user_service_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getWallet_transaction_amount() {
        return wallet_transaction_amount;
    }

    public void setWallet_transaction_amount(String wallet_transaction_amount) {
        this.wallet_transaction_amount = wallet_transaction_amount;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }
}