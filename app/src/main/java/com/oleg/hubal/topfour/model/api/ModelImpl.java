package com.oleg.hubal.topfour.model.api;

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

    public ModelImpl() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        mCurrentDate = dateFormat.format(new Date());
    }
// TODO check date format
    @Override
    public Call<ResponseBody> getUserData(String userToken) {
        return mApiInterface.getUserData(userToken, mCurrentDate);
    }
}
