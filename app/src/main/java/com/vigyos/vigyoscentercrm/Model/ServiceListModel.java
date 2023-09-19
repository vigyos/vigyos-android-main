package com.vigyos.vigyoscentercrm.Model;

public class ServiceListModel {

    private String service_name;
    private String service_id;
    private int price;

    public ServiceListModel() { }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
