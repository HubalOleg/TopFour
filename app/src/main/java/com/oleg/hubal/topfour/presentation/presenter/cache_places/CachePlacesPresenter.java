package com.oleg.hubal.topfour.presentation.presenter.cache_places;


import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.gson.Gson;
import com.oleg.hubal.topfour.model.ModelImpl;
import com.oleg.hubal.topfour.model.data.UserData;
import com.oleg.hubal.topfour.presentation.view.cache_places.CachePlacesView;
import com.oleg.hubal.topfour.utils.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@InjectViewState
public class CachePlacesPresenter extends MvpPresenter<CachePlacesView> {

    private final Context mContext;

    public CachePlacesPresenter(Context context) {
        mContext = context;
    }

    public void onReadyToLoadUserData() {
        getViewState().showProgressDialog();
        ModelImpl model = new ModelImpl();
        Call<ResponseBody> userDataCall = model.getUserData(PreferenceManager.getToken(mContext));

        userDataCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    parseResponseAndSaveUserData(response.body());
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
        });
    }

    private void parseResponseAndSaveUserData(ResponseBody responseBody) throws IOException, JSONException {
        JSONObject responseJSON = new JSONObject(responseBody.string());
        JSONObject userJSON = responseJSON.getJSONObject("response").getJSONObject("user");
        Gson gson = new Gson();
        UserData userData = gson.fromJson(userJSON.toString(), UserData.class);
    }
}
