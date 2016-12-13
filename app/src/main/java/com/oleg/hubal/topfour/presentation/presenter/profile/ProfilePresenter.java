package com.oleg.hubal.topfour.presentation.presenter.profile;


import com.oleg.hubal.topfour.model.database.UserDB;
import com.oleg.hubal.topfour.presentation.view.profile.ProfileView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.raizlabs.android.dbflow.sql.language.SQLite;

@InjectViewState
public class ProfilePresenter extends MvpPresenter<ProfileView> {

    public void onLoadProfileData() {
        UserDB user = SQLite.select().from(UserDB.class).querySingle();
        getViewState().showUserData(user);
    }

}
