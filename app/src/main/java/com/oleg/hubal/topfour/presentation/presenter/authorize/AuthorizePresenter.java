package com.oleg.hubal.topfour.presentation.presenter.authorize;


import android.content.Context;
import android.content.Intent;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.foursquare.android.nativeoauth.FoursquareCancelException;
import com.foursquare.android.nativeoauth.FoursquareDenyException;
import com.foursquare.android.nativeoauth.FoursquareInvalidRequestException;
import com.foursquare.android.nativeoauth.FoursquareOAuth;
import com.foursquare.android.nativeoauth.FoursquareOAuthException;
import com.foursquare.android.nativeoauth.FoursquareUnsupportedVersionException;
import com.foursquare.android.nativeoauth.model.AccessTokenResponse;
import com.foursquare.android.nativeoauth.model.AuthCodeResponse;
import com.oleg.hubal.topfour.BuildConfig;
import com.oleg.hubal.topfour.presentation.view.authorize.AuthorizeView;
import com.oleg.hubal.topfour.utils.PreferenceManager;

@InjectViewState
public class AuthorizePresenter extends MvpPresenter<AuthorizeView> {

    private static final String CLIENT_SECRET = BuildConfig.CLIENT_SECRET;
    private static final String CLIENT_ID = BuildConfig.CLIENT_ID;

    private static final int REQUEST_CODE_FSQ_TOKEN_EXCHANGE = 201;
    private static final int REQUEST_CODE_FSQ_CONNECT = 200;

    private final Context mContext;

    public AuthorizePresenter(Context context) {
        mContext = context;
    }

    public void onFoursquareLoginClick() {
        Intent intent = FoursquareOAuth.getConnectIntent(mContext, CLIENT_ID);

        if (FoursquareOAuth.isPlayStoreIntent(intent)) {
            getViewState().showError("Foursquare not installed");
            mContext.startActivity(intent);
        } else {
            getViewState().sendActivityRequest(intent, REQUEST_CODE_FSQ_CONNECT);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_FSQ_CONNECT:
                onCompleteConnect(resultCode, data);
                break;
            case REQUEST_CODE_FSQ_TOKEN_EXCHANGE:
                AccessTokenResponse tokenResponse = FoursquareOAuth.getTokenFromResult(resultCode, data);
                saveUserToken(tokenResponse.getAccessToken());
                getViewState().userAuthorized();
                break;
        }
    }

    private void onCompleteConnect(int resultCode, Intent data) {
        AuthCodeResponse codeResponse = FoursquareOAuth.getAuthCodeFromResult(resultCode, data);
        Exception exception = codeResponse.getException();

        if (exception == null) {
            String code = codeResponse.getCode();
            Intent intent = FoursquareOAuth.getTokenExchangeIntent(mContext, CLIENT_ID, CLIENT_SECRET, code);
            getViewState().sendActivityRequest(intent, REQUEST_CODE_FSQ_TOKEN_EXCHANGE);
        } else {
            handleException(exception);
        }
    }

    private void saveUserToken(String token) {
        PreferenceManager.setToken(mContext, token);
    }

    private void handleException(Exception exception) {
        if (exception instanceof FoursquareCancelException) {
            getViewState().showError("Canceled");
        } else if (exception instanceof FoursquareDenyException) {
            getViewState().showError("Denied");
        } else if (exception instanceof FoursquareOAuthException) {
            String errorMessage = exception.getMessage();
            String errorCode = ((FoursquareOAuthException) exception).getErrorCode();
            getViewState().showError(errorMessage + " [" + errorCode + "]");
        } else if (exception instanceof FoursquareUnsupportedVersionException) {
            getViewState().showError(exception.toString());
        } else if (exception instanceof FoursquareInvalidRequestException) {
            getViewState().showError(exception.toString());
        } else {
            getViewState().showError(exception.toString());
        }
    }
}
