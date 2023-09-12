package com.example.vigyoscentercrm.Model;

public class BankListModel {

    private int id;
    private String bankName;
    private int iinno;
    private String activeFlag;
    private String aadharpayiinno;

    public BankListModel() { }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIinno() {
        return iinno;
    }

    public void setIinno(int iinno) {
        this.iinno = iinno;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(String activeFlag) {
        this.activeFlag = activeFlag;
    }

    public String getAadharpayiinno() {
        return aadharpayiinno;
    }

    public void setAadharpayiinno(String aadharpayiinno) {
        this.aadharpayiinno = aadharpayiinno;
    }
}