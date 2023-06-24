package com.akbarf.generateimagewithtext.api;

import com.akbarf.generateimagewithtext.model.ResponseData;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {

    @POST("/api/register")
    Call<ResponseData> register(@Body RequestBody requestBody);

    @POST("/api/login")
    Call<ResponseData> login(@Body RequestBody requestBody);


    /**
     * API User
     */
    @GET("/api/user/{id}")
    Call<ResponseData> getUser(@Path("id") String id);

    @PUT("/api/user/{id}/updatePassword")
    Call<ResponseData> updatePassword(@Path("id") String id, @Body RequestBody requestBody);

    @DELETE("/api/user/{id}/deleteAccount")
    Call<ResponseData> deleteAccount(@Path("id") String id, @Body RequestBody requestBody);


    /**
     * History Generate Image
     */
    @GET("/api/history-generate-image")
    Call<ResponseData> getHistoryGenerateImages(@Query("userId") String userId);

    @POST("/api/history-generate-image")
    Call<ResponseData> storeGenerateImage(@Body RequestBody requestBody);

    @DELETE("/api/history-generate-image")
    Call<ResponseData> clearAllHistoryGenerateImage(@Query("userId") String userId);
}
