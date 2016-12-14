package com.oleg.hubal.topfour.presentation.presenter.cache_location;


import android.content.Context;
import android.text.TextUtils;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.oleg.hubal.topfour.global.TopFourApplication;
import com.oleg.hubal.topfour.model.api.data.User;
import com.oleg.hubal.topfour.presentation.events.CheckValidLocationEvent;
import com.oleg.hubal.topfour.presentation.events.LoadUserDataEvent;
import com.oleg.hubal.topfour.presentation.jobs.CheckValidLocationJob;
import com.oleg.hubal.topfour.presentation.jobs.LoadUserDataJob;
import com.oleg.hubal.topfour.presentation.view.cache_location.CacheLocationView;
import com.oleg.hubal.topfour.utils.PreferenceManager;
import com.path.android.jobqueue.JobManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@InjectViewState
public class CacheLocationPresenter extends MvpPresenter<CacheLocationView> {

    private static final String TAG = "CacheLocationPresenter";

    private final Context mContext;
    private String mUserLocation;
    private JobManager mJobManager;

    public CacheLocationPresenter(Context context) {
        mContext = context;
        EventBus.getDefault().register(CacheLocationPresenter.this);
        mJobManager = TopFourApplication.getInstance().getJobManager();
    }

    public void onReadyToLoadUserData() {
        mJobManager.addJobInBackground(new LoadUserDataJob(PreferenceManager.getToken(mContext)));
    }

    public void onLocationCorrect(String location) {
        if (!TextUtils.isEmpty(location)) {
            mUserLocation = location;
        }
        mJobManager.addJobInBackground(new CheckValidLocationJob(mUserLocation, PreferenceManager.getToken(mContext)));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoadUserDataEvent(LoadUserDataEvent loadUserDataEvent) {
        User user = loadUserDataEvent.mUser;
        user.saveToDatabase();
        mUserLocation = user.getHomeCity();
        getViewState().showLocation(mUserLocation);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCheckValidLocation(CheckValidLocationEvent checkValidLocationEvent) {
        if (checkValidLocationEvent.isValid) {
            saveLocationPref();
            startTopFourActivity();
        } else {
            getViewState().askAnotherLocation();
        }
    }

    private void startTopFourActivity() {
        getViewState().showTopFourActivity();
    }

    private void saveLocationPref() {
        PreferenceManager.setLocation(mContext, mUserLocation);
        PreferenceManager.setLocationCashed(mContext, true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(CacheLocationPresenter.this);
    }
}
