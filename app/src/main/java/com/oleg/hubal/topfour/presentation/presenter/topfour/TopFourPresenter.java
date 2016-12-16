package com.oleg.hubal.topfour.presentation.presenter.topfour;


import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.oleg.hubal.topfour.model.database.UserDB;
import com.oleg.hubal.topfour.presentation.view.topfour.TopFourView;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

@InjectViewState
public class TopFourPresenter extends MvpPresenter<TopFourView> {

    private QueryTransaction.QueryResultSingleCallback<UserDB> mResultSingleCallback = new QueryTransaction.QueryResultSingleCallback<UserDB>() {
        @Override
        public void onSingleQueryResult(QueryTransaction transaction, @Nullable UserDB userDB) {
            if (userDB != null) {
                String userName = userDB.getFirstName() + " " + userDB.getLastName();
                String userPhotoUrl = userDB.getPhoto();
                getViewState().initMaterialDrawer(userName, userPhotoUrl);
            }
        }
    };

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        onLoadProfileData();
    }

    public void onLoadProfileData() {
        SQLite.select()
                .from(UserDB.class)
                .async()
                .querySingleResultCallback(mResultSingleCallback)
                .execute();
    }

    public void onProfileImageClick() {
        getViewState().launchProfileFragment();
    }

    public void onVenuesItemClick() {
        getViewState().launchVenuePagerFragment();
    }
}
