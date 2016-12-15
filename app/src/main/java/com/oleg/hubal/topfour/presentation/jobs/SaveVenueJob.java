package com.oleg.hubal.topfour.presentation.jobs;

import com.oleg.hubal.topfour.model.VenueItem;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by User on 15.12.2016.
 */

public class SaveVenueJob extends Job {

    private static final AtomicInteger jobCounter = new AtomicInteger(0);

    private final int id;

    private VenueItem mVenueItem;

    public SaveVenueJob(VenueItem venueItem) {
        super(new Params(Priority.MID).requireNetwork().persist().groupBy("load_venue_job"));
        id = jobCounter.incrementAndGet();
        mVenueItem = venueItem;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        mVenueItem.saveToDatabase();
    }

    @Override
    protected void onCancel() {

    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }
}
