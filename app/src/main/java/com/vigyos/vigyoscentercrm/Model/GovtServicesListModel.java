package com.vigyos.vigyoscentercrm.Model;

public class GovtServicesListModel {

    private String group_id;
    private String group_icon;
    private String group_name;
    private boolean active;
    private String service_icon;
    private String service_link;
    private boolean service_active;
    private String govt_service_name;

    public GovtServicesListModel() { }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_icon() {
        return group_icon;
    }

    public void setGroup_icon(String group_icon) {
        this.group_icon = group_icon;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public String getService_icon() {
        return service_icon;
    }

    public void setService_icon(String service_icon) {
        this.service_icon = service_icon;
    }

    public String getService_link() {
        return service_link;
    }

    public void setService_link(String service_link) {
        this.service_link = service_link;
    }

    public boolean isService_active() {
        return service_active;
    }

    public void setService_active(boolean service_active) {
        this.service_active = service_active;
    }

    public String getGovt_service_name() {
        return govt_service_name;
    }

    public void setGovt_service_name(String govt_service_name) {
        this.govt_service_name = govt_service_name;
    }
}
