package com.vigyos.vigyoscentercrm.Model;

public class WalletHistoryModel {

    private String trx_id;
    private String user_id;
    private int trx_amount;
    private String trx_status;
    private String trx_type;
    private String description;
    private int entity_id;
    private String entity_type;
    private String amount_type;
    private String timestamp;
    private String proof;
    private String wallet_id;
    private String created_at;
    private int record_count;
    private String first_name;
    private String last_name;
    private int updated_amount;

    public WalletHistoryModel() { }

    public String getTrx_id() {
        return trx_id;
    }

    public void setTrx_id(String trx_id) {
        this.trx_id = trx_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getTrx_amount() {
        return trx_amount;
    }

    public void setTrx_amount(int trx_amount) {
        this.trx_amount = trx_amount;
    }

    public String getTrx_status() {
        return trx_status;
    }

    public void setTrx_status(String trx_status) {
        this.trx_status = trx_status;
    }

    public String getTrx_type() {
        return trx_type;
    }

    public void setTrx_type(String trx_type) {
        this.trx_type = trx_type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getEntity_id() {
        return entity_id;
    }

    public void setEntity_id(int entity_id) {
        this.entity_id = entity_id;
    }

    public String getEntity_type() {
        return entity_type;
    }

    public void setEntity_type(String entity_type) {
        this.entity_type = entity_type;
    }

    public String getAmount_type() {
        return amount_type;
    }

    public void setAmount_type(String amount_type) {
        this.amount_type = amount_type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getProof() {
        return proof;
    }

    public void setProof(String proof) {
        this.proof = proof;
    }

    public String getWallet_id() {
        return wallet_id;
    }

    public void setWallet_id(String wallet_id) {
        this.wallet_id = wallet_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getRecord_count() {
        return record_count;
    }

    public void setRecord_count(int record_count) {
        this.record_count = record_count;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public int getUpdated_amount() {
        return updated_amount;
    }

    public void setUpdated_amount(int updated_amount) {
        this.updated_amount = updated_amount;
    }
}