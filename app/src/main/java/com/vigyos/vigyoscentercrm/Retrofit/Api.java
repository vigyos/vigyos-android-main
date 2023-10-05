package com.vigyos.vigyoscentercrm.Retrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
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
            @Header("Authorization") String Authorization
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
            @Field("gender") String gender,
            @Field("redirect_url") String redirect_url,
            @Field("email") String email,
            @Field("user_id") String user_id,
            @Field("remarks") String remarks,
            @Field("status") String status,
            @Field("customer_phone") String customer_phone,
            @Field("customer_adds") String customer_adds
    );

    @GET("service_req")
    Call<Object> serviceRequest(
            @Header("Authorization") String Authorization,
            @Query("user_id") String id
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
}