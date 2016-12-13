package com.oleg.hubal.topfour.presentation.view.venue_pager;

import com.arellomobile.mvp.MvpView;
import com.oleg.hubal.topfour.model.VenueItem;

import java.util.List;

public interface VenuePagerView extends MvpView {

    void addVenueList(List<VenueItem> venueItems);
    void addVenue(VenueItem venueItem);
}
