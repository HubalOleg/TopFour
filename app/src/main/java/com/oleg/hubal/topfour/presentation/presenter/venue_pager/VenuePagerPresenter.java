package com.oleg.hubal.topfour.presentation.presenter.venue_pager;


import android.content.Context;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.oleg.hubal.topfour.global.TopFourApplication;
import com.oleg.hubal.topfour.model.VenueItem;
import com.oleg.hubal.topfour.model.api.ModelImpl;
import com.oleg.hubal.topfour.model.api.data.Venue;
import com.oleg.hubal.topfour.model.database.VenueDB;
import com.oleg.hubal.topfour.presentation.events.LoadVenueEvent;
import com.oleg.hubal.topfour.presentation.jobs.LoadVenueJob;
import com.oleg.hubal.topfour.presentation.view.venue_pager.VenuePagerView;
import com.oleg.hubal.topfour.utils.PreferenceManager;
import com.path.android.jobqueue.JobManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

@InjectViewState
public class VenuePagerPresenter extends MvpPresenter<VenuePagerView> {

    private static final String TAG = "VenuePagerPresenter";

    private static final int CACHED_ITEM_LIMIT = 5;

    public static final int VENUES_PER_REQUEST = 5;

    private final Context mContext;
    private ModelImpl mModel;
    private String mLocation;
    private List<VenueItem> mVenueItems;
    private int mApiLimit = 0;
    private boolean isLoading = false;
    private JobManager mJobManager;

    public VenuePagerPresenter(Context context) {
        mContext = context;
        mModel = new ModelImpl();
        mVenueItems = new ArrayList<>();
        mLocation = PreferenceManager.getLocation(context);
        EventBus.getDefault().register(VenuePagerPresenter.this);
        mJobManager = TopFourApplication.getInstance().getJobManager();
    }

    public void onLoadData() {
        getViewState().addVenueList(mVenueItems);
        boolean isDatabaseEmpty = loadDataFromDatabase();
        if (isDatabaseEmpty) {
            loadDataFromApi();
        }
    }

    private boolean loadDataFromDatabase() {
        List<VenueDB> dbVenueList = SQLite.select().from(VenueDB.class).queryList();
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
        mApiLimit += VENUES_PER_REQUEST;

        String token = PreferenceManager.getToken(mContext);
        Log.d(TAG, "loadDataFromApi: " + token);

        mJobManager.addJobInBackground(new LoadVenueJob(mLocation, mApiLimit, token));
    }

    private void handleVenueItem(Venue venue) {
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoadVenueEvent(LoadVenueEvent loadVenueEvent) {
        handleVenueItem(loadVenueEvent.mVenue);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(VenuePagerPresenter.this);
    }
}
