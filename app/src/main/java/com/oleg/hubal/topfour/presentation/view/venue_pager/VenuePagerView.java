package com.oleg.hubal.topfour.presentation.view.venue_pager;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.oleg.hubal.topfour.model.VenueItem;

import java.util.List;

public interface VenuePagerView extends MvpView {
    void addVenueList(List<VenueItem> venueItems);
    @StateStrategyType(SkipStrategy.class)
    void notifyVenueInserted(int position);
    void showProgressDialog();
    void dismissProgressDialog();
    @StateStrategyType(AddToEndSingleStrategy.class)
    void changeGrid(int spanCount);
}
