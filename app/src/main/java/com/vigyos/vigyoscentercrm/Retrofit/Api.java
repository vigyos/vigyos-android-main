package com.vigyos.vigyoscentercrm.Retrofit;

import com.vigyos.vigyoscentercrm.Model.RequestData;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    @FormUrlEncoded
    @POST ("loginlog")
    Call<Object> login (
            @Field("email") String email,
            @Field("password") String password,
            @Field("type") String type
    );

    @GET("profile")
    Call<Object> profile(
            @Header("Authorization") String Authorization
    );

    @GET("services")
    Call<Object> getServiceName(
            @Header("Authorization") String Authorization,
            @Query("limit") String limit
    );

    @GET("services/{id}")
    Call<Object> serviceDetails(
            @Header("Authorization") String Authorization,
            @Path("id") String id
    );

    @GET("services_g_c")
    Call<Object> servicesGC(
            @Header("Authorization") String Authorization,
            @Query("group_id") String id
    );

    @GET("services_g_c")
    Call<Object> servicesGCList(
            @Header("Authorization") String Authorization,
            @Query("page") int page
    );

    //Fino AEPS API Start
    @GET("aeps/bank_list")
    Call<Object> bankList(
            @Header("Authorization") String Authorization
    );

    @FormUrlEncoded
    @POST("aeps/withdrawal")
    Call<Object> withdrawal(
            @Header("Authorization") String Authorization,
            @Field("accessmodetype") String accessmodetype,
            @Field("adhaarnumber") String adhaarnumber,
            @Field("mobilenumber") String mobilenumber,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("timestamp") String timestamp,
            @Field("data") String data,
            @Field("ipaddress") String ipaddress,
            @Field("pipe") String pipe,
            @Field("submerchantid") String submerchantid,
            @Field("nationalbankidentification") String nationalbankidentification,
            @Field("requestremarks") String requestremarks,
            @Field("transactiontype") String transactiontype,
            @Field("amount") String amount
    );

    @FormUrlEncoded
    @POST("aeps/enquiry")
    Call<Object> enquiry(
            @Header("Authorization") String Authorization,
            @Field("accessmodetype") String accessmodetype,
            @Field("adhaarnumber") String adhaarnumber,
            @Field("mobilenumber") String mobilenumber,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("timestamp") String timestamp,
            @Field("data") String data,
            @Field("ipaddress") String ipaddress,
            @Field("pipe") String pipe,
            @Field("submerchantid") String submerchantid,
            @Field("nationalbankidentification") String nationalbankidentification,
            @Field("requestremarks") String requestremarks,
            @Field("transactiontype") String transactiontype
    );

    @FormUrlEncoded
    @POST("aeps/mini_statement")
    Call<Object> miniStatement(
            @Header("Authorization") String Authorization,
            @Field("accessmodetype") String accessmodetype,
            @Field("adhaarnumber") String adhaarnumber,
            @Field("mobilenumber") String mobilenumber,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("timestamp") String timestamp,
            @Field("data") String data,
            @Field("ipaddress") String ipaddress,
            @Field("pipe") String pipe,
            @Field("submerchantid") String submerchantid,
            @Field("nationalbankidentification") String nationalbankidentification,
            @Field("requestremarks") String requestremarks,
            @Field("transactiontype") String transactiontype
    );

    @FormUrlEncoded
    @POST("aeps/bank_registration")
    Call<Object> bankRegistrationAPI(
            @Header("Authorization") String Authorization,
            @Field("accessmodetype") String accessmodetype,
            @Field("adhaarnumber") String adhaarnumber,
            @Field("mobilenumber") String mobilenumber,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("timestamp") String timestamp,
            @Field("data") String data,
            @Field("ipaddress") String ipaddress,
            @Field("bank") String pipe,
            @Field("submerchantid") String submerchantid
    );

    @FormUrlEncoded
    @POST("aeps/bank_registration/verify")
    Call<Object> FinoAuthAPI(
            @Header("Authorization") String Authorization,
            @Field("accessmodetype") String accessmodetype,
            @Field("adhaarnumber") String adhaarnumber,
            @Field("mobilenumber") String mobilenumber,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("timestamp") String timestamp,
            @Field("data") String data,
            @Field("ipaddress") String ipaddress,
            @Field("bank") String pipe,
            @Field("submerchantid") String submerchantid
    );

    @FormUrlEncoded
    @POST("aeps/payout/list")
    Call<Object> payoutList(
            @Header("Authorization") String Authorization,
            @Field("merchantid") String merchantid
    );

    @GET("aeps/payout/bank_list")
    Call<Object> payoutBankList(
            @Header("Authorization") String Authorization
    );

    @FormUrlEncoded
    @POST("aeps/payout/transaction")
    Call<Object> finoCreatePayOutAPI(
            @Header("Authorization") String Authorization,
            @Field("bene_id") String bene_id,
            @Field("amount") String amount,
            @Field("mode") String mode,
            @Field("submerchantid") String submerchantid,
            @Field("accessmodetype") String accessmodetype,
            @Field("timestamp") String timestamp,
            @Field("adhaarnumber") String adhaarnumber,
            @Field("mobile") String mobile,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("ipaddress") String ipaddress
    );

    @FormUrlEncoded
    @POST("aeps/payout/add_account")
    Call<Object> addBankAccountForPayOut(
            @Header("Authorization") String Authorization,
            @Field("bankid") int bankid,
            @Field("merchant_code") String merchant_code,
            @Field("account") String account,
            @Field("ifsc") String ifsc,
            @Field("name") String name,
            @Field("account_type") String account_type
    );

    @Multipart
    @POST("aeps/payout/doc_upload")
    Call<Object> addPayOutAccPanDocUpload(
            @Header("Authorization") String Authorization,
            @Part("doctype") String doctype,
            @Part MultipartBody.Part passbookImage,
            @Part("passbook") String passbook,
            @Part MultipartBody.Part panCardImage,
            @Part("panimage") String panimage,
            @Part("bene_id") String bene_id
    );

    @Multipart
    @POST("aeps/payout/doc_upload")
    Call<Object> addPayOutAccAadhaarDocUpload(
            @Header("Authorization") String Authorization,
            @Part("doctype") String doctype,
            @Part MultipartBody.Part passbookImage,
            @Part("passbook") String passbook,
            @Part("bene_id") String bene_id,
            @Part MultipartBody.Part AadhaarFrontImage,
            @Part("front_image") String frontImage,
            @Part MultipartBody.Part AadhaarBackImage,
            @Part("back_image") String backImage
    );

    @GET("aeps/transaction/logs")
    Call<Object> aepsHistory(
            @Header("Authorization") String Authorization,
            @Query("user_id") String user_id,
            @Query("page") int page,
            @Query("transaction_type") String transaction_type
    );
    //Fino AEPS API End

    //Paytm AEPS API Start
    @GET("aeps/paytm/state_list")
    Call<Object> stateList(
            @Header("Authorization") String Authorization
    );

    @FormUrlEncoded
    @POST("aeps/paytm/onboard_user")
    Call<Object> paytmOnBoardUser(
            @Header("Authorization") String Authorization,
            @Field("user_id") String user_id,
            @Field("merchantmobilenumber") String merchantmobilenumber,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("statecode") String statecode,
            @Field("merchant_name") String merchant_name,
            @Field("address") String address,
            @Field("pannumber") String pannumber,
            @Field("pincode") String pincode,
            @Field("city") String city
    );

    @FormUrlEncoded
    @POST("aeps/paytm/onboard/status")
    Call<Object> onBoardStatus(
            @Header("Authorization") String Authorization,
            @Field("merchantcode") String merchantcode,
            @Field("mobile") String mobile,
            @Field("pipe") String pipe
    );

    @FormUrlEncoded
    @POST("aeps/paytm/bank_registration/verify")
    Call<Object> paytmAuthAPI(
            @Header("Authorization") String Authorization,
            @Field("accessmodetype") String accessmodetype,
            @Field("adhaarnumber") String adhaarnumber,
            @Field("mobilenumber") String mobilenumber,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("timestamp") String timestamp,
            @Field("data") String data,
            @Field("ipaddress") String ipaddress,
            @Field("submerchantid") String submerchantid
    );

    @GET("aeps/payout/bank_list")
    Call<Object> paytmBankList(
            @Header("Authorization") String Authorization
    );

    @FormUrlEncoded
    @POST("aeps/paytm/withdrawl")
    Call<Object> paytmWithdrawal(
            @Header("Authorization") String Authorization,
            @Field("merchant_name") String merchant_name,
            @Field("pannumber") String pannumber,
            @Field("address") String address,
            @Field("city") String city,
            @Field("pincode") String pincode,
            @Field("statecode") String statecode,
            @Field("mobilenumber") String mobilenumber,
            @Field("accessmodetype") String accessmodetype,
            @Field("ipaddress") String ipaddress,
            @Field("adhaarnumber") String adhaarnumber,
            @Field("merchantmobilenumber") String merchantmobilenumber,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("nationalbankidentification") String nationalbankidentification,
            @Field("pipe") String pipe,
            @Field("transcationtype") String transcationtype,
            @Field("requestremarks") String requestremarks,
            @Field("submerchantid") String submerchantid,
            @Field("data") String data,
            @Field("timestamp") String timestamp,
            @Field("amount") String amount
    );

    @FormUrlEncoded
    @POST("aeps/paytm/balance_enquiry")
    Call<Object> paytmEnquiry(
            @Header("Authorization") String Authorization,
            @Field("merchant_name") String merchant_name,
            @Field("pannumber") String pannumber,
            @Field("address") String address,
            @Field("city") String city,
            @Field("pincode") String pincode,
            @Field("statecode") String statecode,
            @Field("mobilenumber") String mobilenumber,
            @Field("accessmodetype") String accessmodetype,
            @Field("ipaddress") String ipaddress,
            @Field("adhaarnumber") String adhaarnumber,
            @Field("merchantmobilenumber") String merchantmobilenumber,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("nationalbankidentification") String nationalbankidentification,
            @Field("pipe") String pipe,
            @Field("transcationtype") String transcationtype,
            @Field("requestremarks") String requestremarks,
            @Field("submerchantid") String submerchantid,
            @Field("data") String data,
            @Field("timestamp") String timestamp
    );

    @FormUrlEncoded
    @POST("aeps/paytm/mini_statement")
    Call<Object> paytmMiniStatement(
            @Header("Authorization") String Authorization,
            @Field("merchant_name") String merchant_name,
            @Field("pannumber") String pannumber,
            @Field("address") String address,
            @Field("city") String city,
            @Field("pincode") String pincode,
            @Field("statecode") String statecode,
            @Field("mobilenumber") String mobilenumber,
            @Field("accessmodetype") String accessmodetype,
            @Field("ipaddress") String ipaddress,
            @Field("adhaarnumber") String adhaarnumber,
            @Field("merchantmobilenumber") String merchantmobilenumber,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("nationalbankidentification") String nationalbankidentification,
            @Field("pipe") String pipe,
            @Field("transcationtype") String transcationtype,
            @Field("requestremarks") String requestremarks,
            @Field("submerchantid") String submerchantid,
            @Field("data") String data,
            @Field("timestamp") String timestamp
    );

    @FormUrlEncoded
    @POST("aeps/paytm/payout/list")
    Call<Object> paytmPayoutList(
            @Header("Authorization") String Authorization,
            @Field("merchantid") String merchantid
    );

    @FormUrlEncoded
    @POST("aeps/paytm/payout/transaction")
    Call<Object> paytmCreatePayOutAPI(
            @Header("Authorization") String Authorization,
            @Field("bene_id") String bene_id,
            @Field("amount") String amount,
            @Field("mode") String mode,
            @Field("submerchantid") String submerchantid,
            @Field("accessmodetype") String accessmodetype,
            @Field("timestamp") String timestamp,
            @Field("adhaarnumber") String adhaarnumber,
            @Field("mobile") String mobile,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("ipaddress") String ipaddress
    );

    @Multipart
    @POST("aeps/payout/doc_upload")
    Call<Object> paytmAddPayOutAccPanDocUpload(
            @Header("Authorization") String Authorization,
            @Part("doctype") String doctype,
            @Part("bene_id") String bene_id,
            @Part MultipartBody.Part passbookImage,
            @Part("passbook") String passbook,
            @Part MultipartBody.Part panCardImage,
            @Part("panimage") String panimage
    );

    @FormUrlEncoded
    @POST("aeps/paytm/payout/add_account")
    Call<Object> paytmAddAccountForPayOut(
            @Header("Authorization") String Authorization,
            @Field("bankid") int bankid,
            @Field("merchant_code") String merchant_code,
            @Field("account") String account,
            @Field("ifsc") String ifsc,
            @Field("name") String name,
            @Field("account_type") String account_type
    );
    //Paytm AEPS API End

    @GET("govt_services")
    Call<Object> govtServices(
            @Header("Authorization") String Authorization
    );

    @FormUrlEncoded
    @POST("pan/generateNewUrl")
    Call<Object> panCardCreate(
            @Header("Authorization") String Authorization,
            @Field("title") String title,
            @Field("firstname") String firstname,
            @Field("middlename") String middlename,
            @Field("lastname") String lastname,
            @Field("mode") String mode,
            @Field("kyc_type") String kyc_type,
            @Field("gender") String gender,
            @Field("redirect_url") String redirect_url,
            @Field("email") String email,
            @Field("user_id") String user_id,
            @Field("remarks") String remarks,
            @Field("status") String status,
            @Field("customer_phone") String customer_phone,
            @Field("customer_adds") String customer_adds
    );

    @FormUrlEncoded
    @POST("pan/generateCorrectionUrl")
    Call<Object> panCardUpdate(
            @Header("Authorization") String Authorization,
            @Field("title") String title,
            @Field("firstname") String firstname,
            @Field("middlename") String middlename,
            @Field("lastname") String lastname,
            @Field("mode") String mode,
            @Field("kyc_type") String kyc_type,
            @Field("gender") String gender,
            @Field("redirect_url") String redirect_url,
            @Field("email") String email,
            @Field("user_id") String user_id,
            @Field("remarks") String remarks,
            @Field("status") String status,
            @Field("customer_phone") String customer_phone,
            @Field("customer_adds") String customer_adds
    );

    @FormUrlEncoded
    @POST("pan/verify_pan_transaction")
    Call<Object> panVerify(
            @Header("Authorization") String Authorization,
            @Field("encdata") String encdata
    );

    @GET("service_req")
    Call<Object> serviceRequest(
            @Header("Authorization") String Authorization,
            @Query("user_id") String id,
            @Query("page") int page,
            @Query("status") String status
    );

    @Multipart
    @POST("upload")
    Call<Object> uploadImage(
            @Header("Authorization") String Authorization,
            @Part MultipartBody.Part imageName,
            @Part("file") String image
    );

    @POST("service_req")
    Call<Object> buyServiceAPI(
            @Header("Authorization") String Authorization,
            @Body RequestData data
    );

    @GET("transactions")
    Call<Object> walletHistory(
            @Header("Authorization") String Authorization,
            @Query("user_id") String user_id,
            @Query("page") int page,
            @Query("trx_type") String trx_type
    );

    @GET("bbps_paysprint/bill_payment/operator_list")
    Call<Object> payBillOperator(
            @Header("Authorization") String Authorization,
            @Query("mode") String mode
    );

    @FormUrlEncoded
    @POST("bbps_paysprint/bill_payment/fetch_bill")
    Call<Object> fetchBill(
            @Header("Authorization") String Authorization,
            @Field("operator") String operator,
            @Field("canumber") String canumber,
            @Field("mode") String mode
    );

    @POST("bbps_paysprint/bill_payment/pay_bill")
    Call<Object> payBill(
            @Header("Authorization") String authorization,
            @Body Object requestModel
    );

    @GET("bbps_paysprint/recharge/operator_list")
    Call<Object> mobileRechargeOperator(
            @Header("Authorization") String Authorization
    );

    @GET("bbps_paysprint/recharge/get_circle")
    Call<Object> mobileRechargeCircle(
            @Header("Authorization") String Authorization
    );

    @FormUrlEncoded
    @POST("bbps_paysprint/recharge/get_plans_api")
    Call<Object> planForRecharge(
            @Header("Authorization") String Authorization,
            @Field("number") String number,
            @Field("type") String type
    );

    @FormUrlEncoded
    @POST("razorpay/create_order")
    Call<Object> razorpayCreateOrder(
            @Header("Authorization") String Authorization,
            @Field("amount") int amount,
            @Field("currency") String currency,
            @Field("user_id") String user_id,
            @Field("description") String description,
            @Field("name") String name,
            @Field("mobile") String mobile,
            @Field("email") String email,
            @Field("policy_name") String policy_name,
            @Field("transaction_type") String transaction_type
    );

    @FormUrlEncoded
    @POST("razorpay/callback_order")
    Call<Object> razorpayCallBackOrder(
            @Header("Authorization") String Authorization,
            @Field("order_id") String order_id,
            @Field("success") String success,
            @Field("plan_id") String plan_id,
            @Field("plan_start_date") String plan_start_date,
            @Field("plan_end_date") String plan_end_date
    );
}