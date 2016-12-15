package com.oleg.hubal.topfour.presentation.presenter.profile;


import android.support.annotation.Nullable;

import com.oleg.hubal.topfour.model.database.UserDB;
import com.oleg.hubal.topfour.presentation.view.profile.ProfileView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

@InjectViewState
public class ProfilePresenter extends MvpPresenter<ProfileView> {

    private QueryTransaction.QueryResultSingleCallback<UserDB> mResultSingleCallback = new QueryTransaction.QueryResultSingleCallback<UserDB>() {
        @Override
        public void onSingleQueryResult(QueryTransaction transaction, @Nullable UserDB userDB) {
            if (userDB != null) {
                getViewState().showUserData(userDB);
            }
        }
    };

    public void onLoadProfileData() {
        SQLite.select()
                .from(UserDB.class)
                .async()
                .querySingleResultCallback(mResultSingleCallback)
                .execute();
    }

}
