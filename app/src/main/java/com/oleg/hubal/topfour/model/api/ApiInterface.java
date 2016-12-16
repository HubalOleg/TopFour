package com.oleg.hubal.topfour.model.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by User on 12.12.2016.
 */

public interface ApiInterface {

    @GET("users/self")
    Call<ResponseBody> getUserData(@Query("oauth_token") String token, @Query("v") String v);

    @GET("venues/search")
    Call<ResponseBody> getVenuesData(@Query("near") String location, @Query("limit") int limit, @Query("oauth_token") String token, @Query("v") String v);

    @GET("venues/{venueID}/photos")
    Call<ResponseBody> getVenuePhoto(@Path("venueID") String venueID, @Query("limit") int limit, @Query("oauth_token") String token, @Query("v") String v);

    @GET("venues/{venueID}")
    Call<ResponseBody> getVenueById(@Path("venueID") String venueID, @Query("oauth_token") String token, @Query("v") String v);
}
