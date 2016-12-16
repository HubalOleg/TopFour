package com.oleg.hubal.topfour.presentation.presenter.venue_pager;


import android.content.Context;
import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.oleg.hubal.topfour.global.TopFourApplication;
import com.oleg.hubal.topfour.model.VenueItem;
import com.oleg.hubal.topfour.model.api.data.Venue;
import com.oleg.hubal.topfour.model.database.VenueDB;
import com.oleg.hubal.topfour.presentation.events.LoadVenueEvent;
import com.oleg.hubal.topfour.presentation.jobs.SaveVenueJob;
import com.oleg.hubal.topfour.presentation.jobs.SearchVenueJob;
import com.oleg.hubal.topfour.presentation.view.venue_pager.VenuePagerView;
import com.oleg.hubal.topfour.utils.PreferenceManager;
import com.path.android.jobqueue.JobManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

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

    private List<VenueItem> mVenueItems = new ArrayList<>();
    private int mGridSpanCount = 1;

    private final Context mContext;
    private int mApiLimit = 0;
    private boolean isLoading = false;
    private JobManager mJobManager;

    private QueryTransaction.QueryResultListCallback<VenueDB> mVenueQueryListCallback = new QueryTransaction.QueryResultListCallback<VenueDB>() {
        @Override
        public void onListQueryResult(QueryTransaction transaction, @NonNull List<VenueDB> venueDBList) {
            if (venueDBList.size() == 0) {
                getViewState().addVenueList(mVenueItems);
                loadDataFromApi();
            } else {
                mVenueItems.addAll(venueDBList);
                mApiLimit = mVenueItems.size();
                getViewState().addVenueList(mVenueItems);
            }
        }
    };

    public VenuePagerPresenter() {
        mContext = TopFourApplication.getAppContext();
        EventBus.getDefault().register(VenuePagerPresenter.this);
        mJobManager = TopFourApplication.getInstance().getJobManager();
        getViewState().changeGrid(mGridSpanCount);
    }

    public void onLoadData() {
        if (mVenueItems.size() !=  0) {
            getViewState().addVenueList(mVenueItems);
            return;
        }
        SQLite.select().from(VenueDB.class)
                .async()
                .queryListResultCallback(mVenueQueryListCallback)
                .execute();
    }

    private void loadDataFromApi() {
        getViewState().showProgressDialog();
        isLoading = true;
        mApiLimit += VENUES_PER_REQUEST;

        String location = PreferenceManager.getLocation(mContext);
        String token = PreferenceManager.getToken(mContext);

        mJobManager.addJobInBackground(new SearchVenueJob(location, mApiLimit, token));
    }

    private void handleVenueItem(Venue venue) {
        if (mVenueItems.size() < CACHED_ITEM_LIMIT) {
            venue.setCached(true);
            mJobManager.addJobInBackground(new SaveVenueJob(venue));
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

    public void onChangeGridItemSelected() {
        mGridSpanCount = (mGridSpanCount % 2) + 1;
        getViewState().changeGrid(mGridSpanCount);
    }

    public int getSpanCount() {
        return mGridSpanCount;
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
