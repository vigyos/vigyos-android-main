package com.vigyos.vigyoscentercrm.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class MiniStatementModel implements Parcelable {

    private String date;
    private String txnType;
    private double amount;
    private String narration;

    public MiniStatementModel() { }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    protected MiniStatementModel(Parcel in) {
        date = in.readString();
        txnType = in.readString();
        amount = in.readDouble();
        narration = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(txnType);
        dest.writeDouble(amount);
        dest.writeString(narration);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MiniStatementModel> CREATOR = new Creator<MiniStatementModel>() {
        @Override
        public MiniStatementModel createFromParcel(Parcel in) {
            return new MiniStatementModel(in);
        }

        @Override
        public MiniStatementModel[] newArray(int size) {
            return new MiniStatementModel[size];
        }
    };
}