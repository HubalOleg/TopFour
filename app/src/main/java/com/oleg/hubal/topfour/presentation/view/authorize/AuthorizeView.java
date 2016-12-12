package com.oleg.hubal.topfour.presentation.view.authorize;

import android.content.Intent;

import com.arellomobile.mvp.MvpView;

public interface AuthorizeView extends MvpView {
    void sendActivityRequest(Intent intent,int requestCode);
    void showError(String error);
    void userAuthorized();
}
