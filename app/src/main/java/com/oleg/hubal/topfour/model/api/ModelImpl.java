package com.oleg.hubal.topfour.model.api;

import android.content.Context;

import com.oleg.hubal.topfour.utils.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by User on 12.12.2016.
 */

public class ModelImpl implements Model {

    private ApiInterface mApiInterface = ApiModule.getApiInterface();
    private String mCurrentDate;
    private String mToken;

    public ModelImpl(Context context) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        mCurrentDate = dateFormat.format(new Date());
        mToken = PreferenceManager.getToken(context);
    }
// TODO check date format
    @Override
    public Call<ResponseBody> getUserData() {
        return mApiInterface.getUserData(mToken, mCurrentDate);
    }

    @Override
    public Call<ResponseBody> getVenuesData(String location, int limit) {
        return mApiInterface.getVenuesData(location, limit, mToken, mCurrentDate);
    }

    @Override
    public Call<ResponseBody> getVenuePhoto(String venueID, int limit) {
        return mApiInterface.getVenuePhoto(venueID, limit, mToken, mCurrentDate);
    }
}
