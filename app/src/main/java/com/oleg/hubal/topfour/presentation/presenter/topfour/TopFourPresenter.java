package com.oleg.hubal.topfour.presentation.presenter.topfour;


import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.oleg.hubal.topfour.model.database.UserDB;
import com.oleg.hubal.topfour.presentation.view.topfour.TopFourView;
import com.raizlabs.android.dbflow.sql.language.SQLite;

@InjectViewState
public class TopFourPresenter extends MvpPresenter<TopFourView> {

    private final Context mContext;

    public TopFourPresenter(Context context) {
        mContext = context;
    }

    public void onLoadProfileData() {
        UserDB user = SQLite.select().from(UserDB.class).querySingle();
        String userName = user.getFirstName() + " " + user.getLastName();
        String userPhotoUrl = user.getPhoto();
        getViewState().initMaterialDrawer(userName, userPhotoUrl);
    }
}
