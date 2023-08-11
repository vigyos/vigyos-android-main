package com.example.vigyoscentercrm.Model;

public class DocumentItemModel {

    private String key;
    private String document_name;
    private String document_type;
    private String document_label;

    public DocumentItemModel() { }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDocument_name() {
        return document_name;
    }

    public void setDocument_name(String document_name) {
        this.document_name = document_name;
    }

    public String getDocument_type() {
        return document_type;
    }

    public void setDocument_type(String document_type) {
        this.document_type = document_type;
    }

    public String getDocument_label() {
        return document_label;
    }

    public void setDocument_label(String document_label) {
        this.document_label = document_label;
    }
}
