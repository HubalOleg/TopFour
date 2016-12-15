package com.oleg.hubal.topfour.presentation.jobs;

import com.google.gson.Gson;
import com.oleg.hubal.topfour.model.api.Model;
import com.oleg.hubal.topfour.model.api.ModelImpl;
import com.oleg.hubal.topfour.model.api.data.Venue;
import com.oleg.hubal.topfour.presentation.events.LoadVenueEvent;
import com.oleg.hubal.topfour.utils.Utility;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by User on 14.12.2016.
 */

public class LoadVenueJob extends Job {
    private static final AtomicInteger jobCounter = new AtomicInteger(0);

    private final int id;

    private String mLocation;
    private String mToken;
    private int mApiLimit;

    public LoadVenueJob(String location, int apiLimit, String token) {
        super(new Params(Priority.MID).requireNetwork().persist().groupBy("load_venue_job"));
        id = jobCounter.incrementAndGet();
        mLocation = location;
        mApiLimit = apiLimit;
        mToken = token;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        if(id != jobCounter.get()) {
            return;
        }
        Gson gson = new Gson();
        Model model = new ModelImpl();

        Call<ResponseBody> venueDataCall = model.getVenuesData(mLocation, mApiLimit, mToken);
        Response<ResponseBody> response = venueDataCall.execute();

        if (!response.isSuccessful()) {
            return;
        }

        JSONObject responseJSON = Utility.getJSONObjectFromResponse(response);
        JSONArray venueJSONArray = responseJSON.getJSONArray("venues");

        for (int i = 0; i < venueJSONArray.length(); i++) {
            JSONObject venueJSON = venueJSONArray.getJSONObject(i);
            Venue venue = gson.fromJson(venueJSON.toString(), Venue.class);
            EventBus.getDefault().post(new LoadVenueEvent(venue));
        }
    }

    @Override
    protected void onCancel() {

    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }
}
