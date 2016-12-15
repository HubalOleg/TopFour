package com.oleg.hubal.topfour.presentation.view.topfour;

import com.arellomobile.mvp.MvpView;

public interface TopFourView extends MvpView {
    void initMaterialDrawer(String userName, String userPhotoUrl);
    void launchProfileFragment();
    void launchVenuePagerFragment();
}
