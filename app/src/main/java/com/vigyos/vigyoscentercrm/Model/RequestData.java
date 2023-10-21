package com.vigyos.vigyoscentercrm.Model;

import java.util.List;
import java.util.Map;

public class RequestData {

    private String user_id;
    private String service_id;
    private String remarks;
    private String status;
    private String service_group_id;
    private String costumer_name;
    private String customer_phone;
    private String customer_adds;
    private String customer_email;
    private List<Map<String, Object>> required_data;

    public RequestData() { }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getService_group_id() {
        return service_group_id;
    }

    public void setService_group_id(String service_group_id) {
        this.service_group_id = service_group_id;
    }

    public String getCostumer_name() {
        return costumer_name;
    }

    public void setCostumer_name(String costumer_name) {
        this.costumer_name = costumer_name;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public String getCustomer_adds() {
        return customer_adds;
    }

    public void setCustomer_adds(String customer_adds) {
        this.customer_adds = customer_adds;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public List<Map<String, Object>> getRequired_data() {
        return required_data;
    }

    public void setRequired_data(List<Map<String, Object>> required_data) {
        this.required_data = required_data;
    }
}
