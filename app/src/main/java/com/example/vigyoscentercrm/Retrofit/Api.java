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
            @Field("password") String password
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

}