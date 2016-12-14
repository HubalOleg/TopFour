package com.oleg.hubal.topfour.presentation.jobs;

import com.oleg.hubal.topfour.model.api.Model;
import com.oleg.hubal.topfour.model.api.ModelImpl;
import com.oleg.hubal.topfour.presentation.events.CheckValidLocationEvent;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by User on 14.12.2016.
 */

public class CheckValidLocationJob extends Job {

    private static final AtomicInteger jobCounter = new AtomicInteger(0);

    private final int id;

    private String mLocation;
    private String mToken;

    public CheckValidLocationJob(String location, String token) {
        super(new Params(Priority.MID).requireNetwork().persist().groupBy("check_valid_location"));

        id = jobCounter.incrementAndGet();
        mLocation = location;
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
        Model model = new ModelImpl();

        Call<ResponseBody> venueDataCall = model.getVenuesData(mLocation, 1, mToken);
        Response<ResponseBody> response = venueDataCall.execute();
        boolean isValid = response.isSuccessful();
        EventBus.getDefault().post(new CheckValidLocationEvent(isValid));
    }

    @Override
    protected void onCancel() {

    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }
}
