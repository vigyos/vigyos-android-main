package com.vigyos.vigyoscentercrm.Adapter;

public class MunicipalityItem {

    private String categoryName;
    private String categoryIconUrl;

    public MunicipalityItem(String categoryName, String categoryIconUrl) {
        this.categoryName = categoryName;
        this.categoryIconUrl = categoryIconUrl;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryIconUrl() {
        return categoryIconUrl;
    }
}
