package com.oleg.hubal.topfour.model.api;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by User on 12.12.2016.
 */

public interface Model {
    Call<ResponseBody> getUserData(String token);
    Call<ResponseBody> getVenuesData(String location, int limit, String token);
    Call<ResponseBody> getVenuePhoto(String venueID, int limit, String token);
}
