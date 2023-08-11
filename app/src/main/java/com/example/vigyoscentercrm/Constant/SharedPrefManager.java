package com.example.vigyoscentercrm.Constant;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    public SharedPreferences pref;
    public SharedPreferences.Editor editor;
    public Context context;

    private static final String PREF_NAME = "welcome";

    private static final String user_id = "id";
    private static final String token = "token";
    private static final String first_name = "first_name";
    private static final String last_name = "last_name";
    private static final String company = "company";
    private static final String plan = "plan";
    private static final String email = "email";
    private static final String phone = "phone";
    private static final String password = "password";
    private static final String aadhaar_number = "aadhar_number";
    private static final String aadhaar_attachment = "aadhar_attachment";
    private static final String pan_card_number = "pan_card_number";
    private static final String pan_card_attachment = "pan_card_attachment";
    private static final String is_deleted = "is_deleted";
    private static final String user_type = "user_type";
    private static final String is_active = "is_active";
    private static final String city = "city";
    private static final String state = "state";
    private static final String pincode = "pincode";
    private static final String profile_picture = "profile_picture";
    private static final String other_document = "other_document";
    private static final String create_time = "create_time";
    private static final String update_time = "update_time";
    private static final String license_no = "license_no";
    private static final String time_stamp = "time_stamp";
    private static final String createdby = "createdby";
    private static final String sales_person = "sales_person";
    private static final String wallet_id = "wallet_id";
    private static final String amount = "amount";
    private static final String login = "login";
    private static final String service_id = "service_id";
    private static final String service_name = "service_name";
    private static final String service_description = "service_description";

    public SharedPrefManager(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setServiceDescription(String ServiceDescription){
        editor = pref.edit();
        editor.putString(service_description, ServiceDescription);
        editor.commit();
    }

    public String getServiceDescription(){
        return pref.getString(service_description, "null");
    }

    public void setServiceName(String ServiceName){
        editor = pref.edit();
        editor.putString(service_name, ServiceName);
        editor.commit();
    }

    public String getServiceName(){
        return pref.getString(service_name, "null");
    }

    public void setServiceID(String ServiceID){
        editor = pref.edit();
        editor.putString(service_id, ServiceID);
        editor.commit();
    }

    public String getServiceID(){
        return pref.getString(service_id, "null");
    }

    public void setLogin(boolean Login){
        editor = pref.edit();
        editor.putBoolean(login, Login);
        editor.commit();
    }

    public boolean getLogin(){
        return pref.getBoolean(login, false);
    }

    public void setToken(String Token){
        editor = pref.edit();
        editor.putString(token, Token);
        editor.commit();
    }

    public String getToken(){
        return pref.getString(token, "null");
    }

    public void setUserID(String userID){
        editor = pref.edit();
        editor.putString(user_id, userID);
        editor.commit();
    }

    public String getUserID(){
        return pref.getString(user_id, "null");
    }

    public void setFirstName(String FirstName){
        editor = pref.edit();
        editor.putString(first_name, FirstName);
        editor.commit();
    }

    public String getFirstName(){
        return pref.getString(first_name, "null");
    }

    public void setLastName(String LastName){
        editor = pref.edit();
        editor.putString(last_name, LastName);
        editor.commit();
    }

    public String getLastName(){
        return pref.getString(last_name, "null");
    }

    public void setEmail(String Email){
        editor = pref.edit();
        editor.putString(email, Email);
        editor.commit();
    }

    public String getEmail(){
        return pref.getString(email, "null");
    }

    public void setPhone(String Phone){
        editor = pref.edit();
        editor.putString(phone, Phone);
        editor.commit();
    }

    public String getPhone(){
        return pref.getString(phone, "null");
    }

    public void setAadhaarNumber(String AadhaarNumber){
        editor = pref.edit();
        editor.putString(aadhaar_number, AadhaarNumber);
        editor.commit();
    }

    public String getAadhaarNumber(){
        return pref.getString(aadhaar_number, "null");
    }

    public void setAadhaarAttachment(String AadhaarAttachment){
        editor = pref.edit();
        editor.putString(aadhaar_attachment, AadhaarAttachment);
        editor.commit();
    }

    public String getAadhaarAttachment(){
        return pref.getString(aadhaar_attachment, "null");
    }

    public void setPanCardNumber(String PanCardNumber){
        editor = pref.edit();
        editor.putString(pan_card_number, PanCardNumber);
        editor.commit();
    }

    public String getPanCardNumber(){
        return pref.getString(pan_card_number, "null");
    }

    public void setPanCardAttachment(String PanCardAttachment){
        editor = pref.edit();
        editor.putString(pan_card_attachment, PanCardAttachment);
        editor.commit();
    }

    public String getPanCardAttachment(){
        return pref.getString(pan_card_attachment, "null");
    }

    public void setLicenseNumber(String LicenseNumber){
        editor = pref.edit();
        editor.putString(license_no, LicenseNumber);
        editor.commit();
    }

    public String getLicenseNumber(){
        return pref.getString(license_no, "null");
    }

    public void setClear(){
        editor.clear();
        editor.commit();
    }

}
