package com.example.vigyoscentercrm.Retrofit;

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
}