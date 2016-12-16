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
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by User on 16.12.2016.
 */

public class LoadVenueByIdJob extends Job {
    private static final AtomicInteger jobCounter = new AtomicInteger(0);

    private final int id;
    private final String mVenueId;
    private final String mToken;

    public LoadVenueByIdJob(String venueId, String token) {
        super(new Params(Priority.MID).requireNetwork().persist().groupBy("load_venue_by_id"));
        id = jobCounter.incrementAndGet();
        mVenueId = venueId;
        mToken = token;
    }


    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        Gson gson = new Gson();
        Model model = new ModelImpl();

        Call<ResponseBody> venueResponseCall = model.getVenueByID(mVenueId, mToken);
        Response<ResponseBody> response = venueResponseCall.execute();
        JSONObject responseJSON = Utility.getJSONObjectFromResponse(response);

        if (!response.isSuccessful()) {
            return;
        }

        JSONObject venueJSON = responseJSON.getJSONObject("venue");
        Venue venue = gson.fromJson(venueJSON.toString(), Venue.class);
        EventBus.getDefault().post(new LoadVenueEvent(venue));
    }

    @Override
    protected void onCancel() {

    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }
}
