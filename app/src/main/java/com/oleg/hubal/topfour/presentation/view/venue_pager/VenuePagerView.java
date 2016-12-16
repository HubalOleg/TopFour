package com.oleg.hubal.topfour.presentation.view.venue_pager;

import com.arellomobile.mvp.MvpView;
import com.oleg.hubal.topfour.model.VenueItem;

import java.util.List;

public interface VenuePagerView extends MvpView {
    void addVenueList(List<VenueItem> venueItems);
    void notifyVenueInserted(int position);
    void showProgressDialog();
    void dismissProgressDialog();
    void changeGrid(int spanCount);
}
