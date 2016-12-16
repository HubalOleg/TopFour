package com.oleg.hubal.topfour.presentation.jobs;

import com.oleg.hubal.topfour.model.VenueItem;
import com.oleg.hubal.topfour.model.database.VenueDB;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by User on 15.12.2016.
 */

public class SaveVenueJob extends Job {

    private static final AtomicInteger jobCounter = new AtomicInteger(0);

    private final int id;

    private String mId;
    private String mName;
    private String mAddress;
    private String mCity;
    private String mCountry;
    private String mPhotoUrl;


    public SaveVenueJob(VenueItem venueItem) {
        super(new Params(Priority.MID).requireNetwork().persist().groupBy("load_venue_job"));
        id = jobCounter.incrementAndGet();
        mId = venueItem.getId();
        mName = venueItem.getName();
        mCity = venueItem.getCity();
        mAddress = venueItem.getAddress();
        mCountry = venueItem.getCountry();
        mPhotoUrl = venueItem.getPhotoUrl();
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        VenueDB venueDB = new VenueDB(mId, mName, mAddress, mCity, mCountry, mPhotoUrl);
        venueDB.saveToDatabase();
    }

    @Override
    protected void onCancel() {

    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }
}
