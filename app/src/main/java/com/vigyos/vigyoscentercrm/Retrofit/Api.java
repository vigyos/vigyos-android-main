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
    Call<Object> AuthAPI(
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
    Call<Object> createPayOutAPI(
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


}