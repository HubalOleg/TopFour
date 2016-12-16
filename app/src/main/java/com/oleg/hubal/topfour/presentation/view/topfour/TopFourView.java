package com.oleg.hubal.topfour.presentation.view.topfour;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface TopFourView extends MvpView {
    void initMaterialDrawer(String userName, String userPhotoUrl);
    @StateStrategyType(SkipStrategy.class)
    void launchProfileFragment();
    @StateStrategyType(SkipStrategy.class)
    void launchVenuePagerFragment();
}
