package com.vigyos.vigyoscentercrm.Model;

public class PlanDetails {

    private String rs;
    private String desc;
    private String validity;
    private String last_update;

    public PlanDetails(String rs, String desc, String validity, String last_update) {
        this.rs = rs;
        this.desc = desc;
        this.validity = validity;
        this.last_update = last_update;
    }

    // Add getters for each field

    public String getRs() {
        return rs;
    }

    public String getDesc() {
        return desc;
    }

    public String getValidity() {
        return validity;
    }

    public String getLastUpdate() {
        return last_update;
    }

}
