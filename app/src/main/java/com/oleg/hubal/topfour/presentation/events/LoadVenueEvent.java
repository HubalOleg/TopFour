package com.oleg.hubal.topfour.presentation.events;

import com.oleg.hubal.topfour.model.api.data.Venue;

/**
 * Created by User on 14.12.2016.
 */

public class LoadVenueEvent {
    public final Venue mVenue;

    public LoadVenueEvent(Venue venue) {
        mVenue = venue;
    }
}
