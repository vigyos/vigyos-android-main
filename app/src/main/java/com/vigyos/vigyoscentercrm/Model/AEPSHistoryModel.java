package com.vigyos.vigyoscentercrm.Model;

public class AEPSHistoryModel {

    private String user_id;
    private String commission;
    private String referenceno;
    private String transactiontype;
    private int amount;
    private String timestamp;
    private String bene_id;
    private String adhaarnumber;
    private String ackno;

    public AEPSHistoryModel() { }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getReferenceno() {
        return referenceno;
    }

    public void setReferenceno(String referenceno) {
        this.referenceno = referenceno;
    }

    public String getTransactiontype() {
        return transactiontype;
    }

    public void setTransactiontype(String transactiontype) {
        this.transactiontype = transactiontype;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getBene_id() {
        return bene_id;
    }

    public void setBene_id(String bene_id) {
        this.bene_id = bene_id;
    }

    public String getAdhaarnumber() {
        return adhaarnumber;
    }

    public void setAdhaarnumber(String adhaarnumber) {
        this.adhaarnumber = adhaarnumber;
    }

    public String getAckno() {
        return ackno;
    }

    public void setAckno(String ackno) {
        this.ackno = ackno;
    }
}
