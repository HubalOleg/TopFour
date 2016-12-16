package com.oleg.hubal.topfour.presentation.jobs;

import com.oleg.hubal.topfour.global.TopFourApplication;
import com.oleg.hubal.topfour.model.api.Model;
import com.oleg.hubal.topfour.model.api.ModelImpl;
import com.oleg.hubal.topfour.presentation.presenter.venue_pager.VenuePagerPresenter;
import com.oleg.hubal.topfour.utils.Utility;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.Params;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by User on 14.12.2016.
 */

public class SearchVenueJob extends Job {
    private static final AtomicInteger jobCounter = new AtomicInteger(0);

    private final int id;

    private String mLocation;
    private String mToken;
    private int mApiLimit;

    public SearchVenueJob(String location, int apiLimit, String token) {
        super(new Params(Priority.MID).requireNetwork().persist().groupBy("search_venue"));
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
        JobManager jobManager = TopFourApplication.getInstance().getJobManager();

        Model model = new ModelImpl();

        Call<ResponseBody> venueDataCall = model.getVenuesData(mLocation, mApiLimit, mToken);
        Response<ResponseBody> response = venueDataCall.execute();

        if (!response.isSuccessful()) {
            return;
        }

        JSONObject responseJSON = Utility.getJSONObjectFromResponse(response);
        JSONArray venueJSONArray = responseJSON.getJSONArray("venues");

        int firstElementPosition = mApiLimit - VenuePagerPresenter.VENUES_PER_REQUEST;

        for (int i = firstElementPosition; i < venueJSONArray.length(); i++) {
            JSONObject venueJSON = venueJSONArray.getJSONObject(i);
            String venueId = venueJSON.getString("id");
            jobManager.addJobInBackground(new LoadVenueByIdJob(venueId, mToken));
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
