package com.vigyos.vigyoscentercrm.Constant;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    public SharedPreferences pref;
    public SharedPreferences.Editor editor;
    public Context context;

    private static final String PREF_NAME = "welcome";

    //Profile Data variable start
    private static final String user_id = "id";
    private static final String token = "token";
    private static final String first_name = "first_name";
    private static final String last_name = "last_name";
    private static final String company = "company";
    private static final String email = "email";
    private static final String phone = "phone";
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
    private static final String updated_by = "updated_by";
    private static final String wallet_id = "wallet_id";
    private static final String amount = "amount";
    private static final String dashboard = "dashboard";
    private static final String services = "services";
    private static final String add_to_wallet = "add_to_wallet";
    private static final String service_request = "service_request";
    private static final String users = "users";
    private static final String buy_new_service = "buy_new_service";
    private static final String pan = "pan";
    private static final String bbps = "bbps";
    private static final String aeps = "aeps";
    private static final String wallet = "wallet";
    private static final String my_orders = "my_orders";
    private static final String my_profile = "my_profile";
    private static final String contact_us = "contact_us";
    private static final String plan = "plan";
    private static final String user_plan_id = "user_plan_id";
    private static final String plan_id = "user_plan_id";
    private static final String plan_start_date = "plan_start_date";
    private static final String plan_end_date = "plan_end_date";
    private static final String plan_details = "plan_details";
    //plan_id again
    private static final String plan_details_2 = "plan_details";
    private static final String plan_is_active = "plan_is_active";
    private static final String plan_line = "plan_line";
    private static final String plan_name = "plan_name";
    private static final String plan_created_by = "plan_created_by";
    private static final String plan_is_deleted = "plan_is_deleted";
    private static final String plan_price = "plan_price";
    private static final String plan_updated_by = "plan_updated_by";
    private static final String plan_created_time = "plan_created_time";
    private static final String plan_updated_time = "plan_updated_time";
    private static final String plan_duration = "plan_duration";
    private static final String plan_feature_name = "plan_feature_name";
    private static final String plan_description = "plan_description";
    private static final String plan_duration_type = "plan_duration_type";
    private static final String plan_discounted_price = "plan_discounted_price";
    private static final String merchant_id = "merchant_id";
    private static final String is_verified = "is_verified";
    private static final String bank_verified = "bank_verified";
    private static final String last_verify_timestamp_aeps = "last_verify_timestamp_aeps";
    private static final String payout_balance = "payout_balance";
    private static final String bbps_commission_balance = "bbps_commission_balance";
    private static final String aeps_commission_balance = "aeps_commission_balance";
    //Profile Data variable End

    private static final String login = "login";
    private static final String register = "register";
    private static final String service_id = "service_id";
    private static final String service_name = "service_name";
    private static final String service_description = "service_description";
    private static final String biometric_lock = "biometric_lock";
    private static final String biometric_sensor = "biometric_sensor";
    private static final String onboarding = "onboarding";

    public SharedPrefManager(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setOnboarding(boolean Onboarding) {
        editor = pref.edit();
        editor.putBoolean(onboarding, Onboarding);
        editor.commit();
    }

    public boolean getOnboarding() {
        return pref.getBoolean(onboarding, false);
    }

    public void setBiometricSensor(boolean BiometricSensor) {
        editor = pref.edit();
        editor.putBoolean(biometric_sensor, BiometricSensor);
        editor.commit();
    }

    public boolean getBiometricSensor(){
        return pref.getBoolean(biometric_sensor, false);
    }

    public void setBiometricLock(boolean BiometricLock){
        editor = pref.edit();
        editor.putBoolean(biometric_lock, BiometricLock);
        editor.commit();
    }

    public boolean getBiometricLock(){
        return pref.getBoolean(biometric_lock, false);
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

    public void setLogin(boolean Login) {
        editor = pref.edit();
        editor.putBoolean(login, Login);
        editor.commit();
    }

    public boolean getLogin(){
        return pref.getBoolean(login, false);
    }

    public void setRegister(boolean Register) {
        editor = pref.edit();
        editor.putBoolean(register, Register);
        editor.commit();
    }

    public boolean getRegister(){
        return pref.getBoolean(register, false);
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

    public void setMerchantId(String MerchantId){
        editor = pref.edit();
        editor.putString(merchant_id, MerchantId);
        editor.commit();
    }

    public String getMerchantId(){
        return pref.getString(merchant_id, "null");
    }

    public void setAmount(int Amount){
        editor = pref.edit();
        editor.putInt(amount, Amount);
        editor.commit();
    }

    public int getAmount(){
        return pref.getInt(amount, 0);
    }

    public void setDashboard(boolean Dashboard){
        editor = pref.edit();
        editor.putBoolean(dashboard, Dashboard);
        editor.commit();
    }

    public boolean getDashboard(){
        return pref.getBoolean(dashboard, false);
    }

    public void setServices(boolean Services){
        editor = pref.edit();
        editor.putBoolean(services, Services);
        editor.commit();
    }

    public boolean getServices(){
        return pref.getBoolean(services, false);
    }

    public void setAddToWallet(boolean AddToWallet){
        editor = pref.edit();
        editor.putBoolean(add_to_wallet, AddToWallet);
        editor.commit();
    }

    public boolean getAddToWallet(){
        return pref.getBoolean(add_to_wallet, false);
    }

    public void setServiceRequest(boolean ServiceRequest){
        editor = pref.edit();
        editor.putBoolean(service_request, ServiceRequest);
        editor.commit();
    }

    public boolean getServiceRequest(){
        return pref.getBoolean(service_request, false);
    }

    public void setUsers(boolean Users){
        editor = pref.edit();
        editor.putBoolean(users, Users);
        editor.commit();
    }

    public boolean getUsers(){
        return pref.getBoolean(users, false);
    }

    public void setBuyNewService(boolean BuyNewService){
        editor = pref.edit();
        editor.putBoolean(buy_new_service, BuyNewService);
        editor.commit();
    }

    public boolean getBuyNewService(){
        return pref.getBoolean(buy_new_service, false);
    }

    public void setPan(boolean Pan){
        editor = pref.edit();
        editor.putBoolean(pan, Pan);
        editor.commit();
    }

    public boolean getPan(){
        return pref.getBoolean(pan, false);
    }

    public void setBBPS(boolean BBPS){
        editor = pref.edit();
        editor.putBoolean(bbps, BBPS);
        editor.commit();
    }

    public boolean getBBPS(){
        return pref.getBoolean(bbps, false);
    }

    public void setAEPS(boolean AEPS){
        editor = pref.edit();
        editor.putBoolean(aeps, AEPS);
        editor.commit();
    }

    public boolean getAEPS(){
        return pref.getBoolean(aeps, false);
    }

    public void setWallet(boolean Wallet){
        editor = pref.edit();
        editor.putBoolean(wallet, Wallet);
        editor.commit();
    }

    public boolean getWallet(){
        return pref.getBoolean(wallet, false);
    }

    public void setMyOrders(boolean MyOrders){
        editor = pref.edit();
        editor.putBoolean(my_orders, MyOrders);
        editor.commit();
    }

    public boolean getMyOrders(){
        return pref.getBoolean(my_orders, false);
    }

    public void setMyProfile(boolean MyProfile){
        editor = pref.edit();
        editor.putBoolean(my_profile, MyProfile);
        editor.commit();
    }

    public boolean getMyProfile(){
        return pref.getBoolean(my_profile, false);
    }

    public void setContactUs(boolean ContactUs){
        editor = pref.edit();
        editor.putBoolean(contact_us, ContactUs);
        editor.commit();
    }

    public boolean getContactUs(){
        return pref.getBoolean(contact_us, false);
    }

    public void setProfilePicture(String profilePicture){
        editor = pref.edit();
        editor.putString(profile_picture, profilePicture);
        editor.commit();
    }

    public String getProfilePicture(){
        return pref.getString(profile_picture, "null");
    }

    public void setOtherDocument(String OtherDocument){
        editor = pref.edit();
        editor.putString(other_document, OtherDocument);
        editor.commit();
    }

    public String getOtherDocument(){
        return pref.getString(other_document, "null");
    }

    public void setJoinDate(int JoinDate){
        editor = pref.edit();
        editor.putInt(create_time, JoinDate);
        editor.commit();
    }

    public int getJoinDate(){
        return pref.getInt(create_time, 0);
    }

    public void setUpdateTime(int UpdateTime){
        editor = pref.edit();
        editor.putInt(update_time, UpdateTime);
        editor.commit();
    }

    public int getUpdateTime(){
        return pref.getInt(update_time, 0);
    }

    public void setTimeStamp(int TimeStamp){
        editor = pref.edit();
        editor.putInt(time_stamp, TimeStamp);
        editor.commit();
    }

    public int getTimeStamp(){
        return pref.getInt(time_stamp, 0);
    }

    public void setCreatedBy(String CreatedBy){
        editor = pref.edit();
        editor.putString(createdby, CreatedBy);
        editor.commit();
    }

    public String getCreatedBy(){
        return pref.getString(createdby, "null");
    }

    public void setSalesPerson(String SalesPerson){
        editor = pref.edit();
        editor.putString(sales_person, SalesPerson);
        editor.commit();
    }

    public String getSalesPerson(){
        return pref.getString(sales_person, "null");
    }

    public void setUpdatedBy(String UpdatedBy){
        editor = pref.edit();
        editor.putString(updated_by, UpdatedBy);
        editor.commit();
    }

    public String getUpdatedBy(){
        return pref.getString(updated_by, "null");
    }

    public void setPayoutBalance(int PayoutBalance){
        editor = pref.edit();
        editor.putInt(payout_balance, PayoutBalance);
        editor.commit();
    }

    public int getPayoutBalance(){
        return pref.getInt(payout_balance, 0);
    }

    public void setCompany(String Company){
        editor = pref.edit();
        editor.putString(company, Company);
        editor.commit();
    }

    public String getCompany(){
        return pref.getString(company, "null");
    }

    public void setPlan(String Plan){
        editor = pref.edit();
        editor.putString(plan, Plan);
        editor.commit();
    }

    public String getPlan(){
        return pref.getString(plan, "null");
    }

    public void setIsDeleted(String IsDeleted){
        editor = pref.edit();
        editor.putString(is_deleted, IsDeleted);
        editor.commit();
    }

    public String getIsDeleted(){
        return pref.getString(is_deleted, "null");
    }

    public void setUserType(String UserType){
        editor = pref.edit();
        editor.putString(user_type, UserType);
        editor.commit();
    }

    public String getUserType(){
        return pref.getString(user_type, "null");
    }

    public void setIsActive(String IsActive){
        editor = pref.edit();
        editor.putString(is_active, IsActive);
        editor.commit();
    }

    public String getIsActive(){
        return pref.getString(is_active, "null");
    }

    public void setCity(String City){
        editor = pref.edit();
        editor.putString(city, City);
        editor.commit();
    }

    public String getCity(){
        return pref.getString(city, "null");
    }

    public void setState(String State){
        editor = pref.edit();
        editor.putString(state, State);
        editor.commit();
    }

    public String getState(){
        return pref.getString(state, "null");
    }

    public void setPinCode(String PinCode){
        editor = pref.edit();
        editor.putString(pincode, PinCode);
        editor.commit();
    }

    public String getPinCode(){
        return pref.getString(pincode, "null");
    }

    public void setWalletId(String WalletId){
        editor = pref.edit();
        editor.putString(wallet_id, WalletId);
        editor.commit();
    }

    public String getWalletId(){
        return pref.getString(wallet_id, "null");
    }

    public void setIsVerified(String IsVerified){
        editor = pref.edit();
        editor.putString(is_verified, IsVerified);
        editor.commit();
    }

    public String getIsVerified(){
        return pref.getString(is_verified, "null");
    }

    public void setBankVerified(String BankVerified){
        editor = pref.edit();
        editor.putString(bank_verified, BankVerified);
        editor.commit();
    }

    public String getBankVerified(){
        return pref.getString(bank_verified, "null");
    }

    public void setLastVerifyTimeStampAeps(long LastVerifyTimeStampAeps){
        editor = pref.edit();
        editor.putLong(last_verify_timestamp_aeps, LastVerifyTimeStampAeps);
        editor.commit();
    }

    public long getLastVerifyTimeStampAeps(){
        return pref.getLong(last_verify_timestamp_aeps, 0);
    }

    public void setUserPlanId(String UserPlanId) {
        editor = pref.edit();
        editor.putString(user_plan_id, UserPlanId);
        editor.commit();
    }

    public String getUserPlanId() {
        return pref.getString(user_plan_id, "null");
    }

    public void setPlanId(String PlanId) {
        editor = pref.edit();
        editor.putString(plan_id, PlanId);
        editor.commit();
    }

    public String getPlanId() {
        return pref.getString(plan_id, "null");
    }

    public void setPlanStartDate(int PlanStartDate) {
        editor = pref.edit();
        editor.putInt(plan_start_date, PlanStartDate);
        editor.commit();
    }

    public int getPlanStartDate() {
        return pref.getInt(plan_start_date, 0);
    }

    public void setPlanEndDate(int PlanEndDate) {
        editor = pref.edit();
        editor.putInt(plan_end_date, PlanEndDate);
        editor.commit();
    }

    public int getPlanEndDate() {
        return pref.getInt(plan_end_date, 0);
    }

    public void setPlanDetails(String PlanDetails) {
        editor = pref.edit();
        editor.putString(plan_details, PlanDetails);
        editor.commit();
    }

    public String getPlanDetails() {
        return pref.getString(plan_details, "null");
    }

    public void setPlanIsActive(boolean PlanDetails) {
        editor = pref.edit();
        editor.putBoolean(plan_is_active, PlanDetails);
        editor.commit();
    }

    public boolean getPlanIsActive() {
        return pref.getBoolean(plan_is_active, false);
    }

    public void setPlanLine(String PlanLine) {
        editor = pref.edit();
        editor.putString(plan_line, PlanLine);
        editor.commit();
    }

    public String getPlanLine() {
        return pref.getString(plan_line, "null");
    }

    public void setPlanName(String PlanName) {
        editor = pref.edit();
        editor.putString(plan_name, PlanName);
        editor.commit();
    }

    public String getPlanName() {
        return pref.getString(plan_name, "null");
    }

    public void setPlanCreatedBy(String PlanCreatedBy) {
        editor = pref.edit();
        editor.putString(plan_created_by, PlanCreatedBy);
        editor.commit();
    }

    public String getPlanCreatedBy() {
        return pref.getString(plan_created_by, "null");
    }

    public void setPlanIsDeleted(boolean PlanIsDeleted) {
        editor = pref.edit();
        editor.putBoolean(plan_is_deleted, PlanIsDeleted);
        editor.commit();
    }

    public boolean getPlanIsDeleted() {
        return pref.getBoolean(plan_is_deleted, false);
    }

    public void setPlanPrice(int PlanPrice) {
        editor = pref.edit();
        editor.putInt(plan_price, PlanPrice);
        editor.commit();
    }

    public int getPlanPrice() {
        return pref.getInt(plan_price, 0);
    }

    public void setPlanUpdatedBy(String PlanUpdatedBy) {
        editor = pref.edit();
        editor.putString(plan_updated_by, PlanUpdatedBy);
        editor.commit();
    }

    public String getPlanUpdatedBy() {
        return pref.getString(plan_updated_by, "null");
    }

    public void setPlanCreatedTime(int PlanCreatedTime) {
        editor = pref.edit();
        editor.putInt(plan_created_time, PlanCreatedTime);
        editor.commit();
    }

    public int getPlanCreatedTime() {
        return pref.getInt(plan_created_time, 0);
    }

    public void setPlanUpdatedTime(String PlanUpdatedTime) {
        editor = pref.edit();
        editor.putString(plan_updated_time, PlanUpdatedTime);
        editor.commit();
    }

    public String getPlanUpdatedTime() {
        return pref.getString(plan_updated_time, "null");
    }

    public void setPlanDuration(int PlanDuration) {
        editor = pref.edit();
        editor.putInt(plan_duration, PlanDuration);
        editor.commit();
    }

    public int getPlanDuration() {
        return pref.getInt(plan_duration, 0);
    }

    public void setPlanFeatureName(String PlanFeatureName) {
        editor = pref.edit();
        editor.putString(plan_feature_name, PlanFeatureName);
        editor.commit();
    }

    public String getPlanFeatureName() {
        return pref.getString(plan_feature_name, "null");
    }

    public void setPlanDescription(String PlanDescription) {
        editor = pref.edit();
        editor.putString(plan_description, PlanDescription);
        editor.commit();
    }

    public String getPlanDescription() {
        return pref.getString(plan_description, "null");
    }

    public void setPlanDurationType(String PlanDescription) {
        editor = pref.edit();
        editor.putString(plan_duration_type, PlanDescription);
        editor.commit();
    }

    public String getPlanDurationType() {
        return pref.getString(plan_duration_type, "null");
    }

    public void setPlanDiscountedPrice(int PlanDescription) {
        editor = pref.edit();
        editor.putInt(plan_discounted_price, PlanDescription);
        editor.commit();
    }

    public int getPlanDiscountedPrice() {
        return pref.getInt(plan_discounted_price, 0);
    }

    public void setBBPSCommissionBalance(String BBPSCommissionBalance) {
        editor = pref.edit();
        editor.putString(bbps_commission_balance, BBPSCommissionBalance);
        editor.commit();
    }

    public String getBBPSCommissionBalance() {
        return pref.getString(bbps_commission_balance, "null");
    }

    public void setAEPSCommissionBalance(String AEPSCommissionBalance) {
        editor = pref.edit();
        editor.putString(aeps_commission_balance, AEPSCommissionBalance);
        editor.commit();
    }

    public String getAEPSCommissionBalance() {
        return pref.getString(aeps_commission_balance, "null");
    }

    public void setClear(){
        editor.clear();
        editor.commit();
    }
}