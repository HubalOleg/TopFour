package com.oleg.hubal.topfour.presentation.presenter.cache_location;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.gson.Gson;
import com.oleg.hubal.topfour.model.api.ModelImpl;
import com.oleg.hubal.topfour.model.api.data.User;
import com.oleg.hubal.topfour.presentation.view.cache_location.CacheLocationView;
import com.oleg.hubal.topfour.utils.PreferenceManager;
import com.oleg.hubal.topfour.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@InjectViewState
public class CacheLocationPresenter extends MvpPresenter<CacheLocationView> {

    private static final String TAG = "CacheLocationPresenter";
    private static final int VENUE_LIMIT = 15;

    private final Context mContext;


    private String mUserLocation;
    private ModelImpl mModel;
    private Gson mGson;

    private Callback<ResponseBody> mUserDataCallback = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            try {
                parseResponseAndSaveUserData(response);
                getViewState().showLocation(mUserLocation);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                getViewState().dismissProgressDialog();
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            getViewState().dismissProgressDialog();
        }
    };

    private Callback<ResponseBody> mVenueDataCallback = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if (response.isSuccessful()) {
                saveLocationPref();
                startTopFourActivity();
            } else {
                getViewState().dismissProgressDialog();
                getViewState().askAnotherLocation();
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            Log.d(TAG, "onFailure: ");
        }
    };

    public CacheLocationPresenter(Context context) {
        mContext = context;
        mModel = new ModelImpl(context);
        mGson = new Gson();
    }

    public void onReadyToLoadUserData() {
        getViewState().showProgressDialog();

        Call<ResponseBody> userDataCall = mModel.getUserData();
        userDataCall.enqueue(mUserDataCallback);
    }

    public void onLocationCorrect(String location) {
        if (!TextUtils.isEmpty(location)) {
            mUserLocation = location;
        }

        getViewState().showProgressDialog();

        Call<ResponseBody> venueDataCall = mModel.getVenuesData(mUserLocation, VENUE_LIMIT);
        venueDataCall.enqueue(mVenueDataCallback);
    }

    private void parseResponseAndSaveUserData(Response<ResponseBody> response) throws IOException, JSONException {
        JSONObject responseJSON = Utility.getJSONObjectFromResponse(response);
        JSONObject userJSON = responseJSON.getJSONObject("user");
        User user = mGson.fromJson(userJSON.toString(), User.class);
        user.saveToDatabase();
        mUserLocation = user.getHomeCity();
    }

    private void saveLocationPref() {
        PreferenceManager.setLocation(mContext, mUserLocation);
        PreferenceManager.setLocationCashed(mContext, true);
    }

    private void startTopFourActivity() {
        getViewState().showTopFourActivity();
    }
}
