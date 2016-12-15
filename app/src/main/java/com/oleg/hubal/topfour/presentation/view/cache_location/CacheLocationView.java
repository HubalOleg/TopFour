package com.oleg.hubal.topfour.presentation.view.cache_location;

import com.arellomobile.mvp.MvpView;

public interface CacheLocationView extends MvpView {
    void showLocation(String location);
    void askAnotherLocation();
    void showTopFourActivity();
}
