package com.oleg.hubal.topfour.presentation.view.profile;

import com.arellomobile.mvp.MvpView;
import com.oleg.hubal.topfour.model.database.UserDB;

public interface ProfileView extends MvpView {
    void showUserData(UserDB user);
}
