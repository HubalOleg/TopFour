package com.oleg.hubal.topfour.presentation.jobs;

import com.google.gson.Gson;
import com.oleg.hubal.topfour.model.api.Model;
import com.oleg.hubal.topfour.model.api.ModelImpl;
import com.oleg.hubal.topfour.model.api.data.User;
import com.oleg.hubal.topfour.presentation.events.LoadUserDataEvent;
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
 * Created by User on 14.12.2016.
 */

public class LoadUserDataJob extends Job {
    private static final AtomicInteger jobCounter = new AtomicInteger(0);

    private final int id;
    private final String mToken;

    public LoadUserDataJob(String token) {
        super(new Params(Priority.MID).requireNetwork().persist().groupBy("load_user_data"));
        id = jobCounter.incrementAndGet();
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
        Gson gson = new Gson();
        Call<ResponseBody> userDataCall = model.getUserData(mToken);
        Response<ResponseBody> response = userDataCall.execute();

        if (!response.isSuccessful()) {
            return;
        }

        JSONObject responseJSON = Utility.getJSONObjectFromResponse(response);
        JSONObject userJSON = responseJSON.getJSONObject("user");
        User user = gson.fromJson(userJSON.toString(), User.class);
        EventBus.getDefault().post(new LoadUserDataEvent(user));
    }

    @Override
    protected void onCancel() {

    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }
}
