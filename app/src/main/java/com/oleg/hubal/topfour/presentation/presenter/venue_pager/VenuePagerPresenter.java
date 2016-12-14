package com.oleg.hubal.topfour.presentation.presenter.venue_pager;


import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.gson.Gson;
import com.oleg.hubal.topfour.model.VenueItem;
import com.oleg.hubal.topfour.model.api.ModelImpl;
import com.oleg.hubal.topfour.model.api.data.Venue;
import com.oleg.hubal.topfour.model.database.VenueDB;
import com.oleg.hubal.topfour.presentation.view.venue_pager.VenuePagerView;
import com.oleg.hubal.topfour.utils.PreferenceManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@InjectViewState
public class VenuePagerPresenter extends MvpPresenter<VenuePagerView> {

    private static final String TAG = "VenuePagerPresenter";

    private static final int CACHED_ITEM_LIMIT = 5;

    private final Context mContext;
    private ModelImpl mModel;
    private String mLocation;
    private List<VenueItem> mVenueItems;
    private int mApiLimit = 0;
    private boolean isLoading = false;

    private Callback<ResponseBody> mVenueDataCallback = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            try {
                if (response.isSuccessful()) {
                    parseResponse(response.body());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {

        }
    };

    public VenuePagerPresenter(Context context) {
        mContext = context;
        mModel = new ModelImpl(context);
        mVenueItems = new ArrayList<>();
        mLocation = PreferenceManager.getLocation(context);
    }

    public void onLoadData() {
        getViewState().addVenueList(mVenueItems);
        boolean isDatabaseEmpty = loadDataFromDatabase();
        if (isDatabaseEmpty) {
            loadDataFromApi();
        }
    }

    private boolean loadDataFromDatabase() {
        List<VenueDB> dbVenueList = new ArrayList<>();
        dbVenueList = SQLite.select().from(VenueDB.class).queryList();
        if (dbVenueList.size() == 0) {
            return true;
        } else {
            mVenueItems.addAll(dbVenueList);
            mApiLimit = dbVenueList.size();
            getViewState().addVenueList(mVenueItems);
            return false;
        }
    }

    private void loadDataFromApi() {
        getViewState().showProgressDialog();
        isLoading = true;
        mApiLimit += 5;
        Call<ResponseBody> venueDataCall = mModel.getVenuesData(mLocation, mApiLimit);
        venueDataCall.enqueue(mVenueDataCallback);
    }

    private void parseResponse(ResponseBody responseBody) throws IOException, JSONException {
        Gson gson = new Gson();

        JSONObject responseJSON = new JSONObject(responseBody.string());
        JSONArray venueJSONArray = responseJSON.getJSONObject("response").getJSONArray("venues");

        for (int i = 0; i < venueJSONArray.length(); i++) {
            JSONObject venueJSON = venueJSONArray.getJSONObject(i);
            Venue venue = gson.fromJson(venueJSON.toString(), Venue.class);
            handleNewVenueItem(venue);
        }
    }

    private void handleNewVenueItem(Venue venue) {
        if (mVenueItems.size() < CACHED_ITEM_LIMIT) {
            venue.setCached(true);
            venue.saveToDatabase();
        }
        addItemIfNotExist(venue);
    }

    private void addItemIfNotExist(Venue venue) {
        for (VenueItem venueItem : mVenueItems) {
            if (venueItem.getId().equals(venue.getId())) {
                return;
            }
        }
        mVenueItems.add(venue);
        getViewState().notifyVenueInserted(mVenueItems.size() - 1);
        if (mVenueItems.size() == mApiLimit) {
            isLoading = false;
            getViewState().dismissProgressDialog();
        }
    }

    public void onPowerFling(int lastItemPosition) {
        if (lastItemPosition == mVenueItems.size() - 1 && !isLoading) {
            loadDataFromApi();
        }
    }
}
