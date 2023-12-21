package com.vigyos.vigyoscentercrm.Model;

public class KeyValueModel {

    private String key;
    private String value;

    public KeyValueModel(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
