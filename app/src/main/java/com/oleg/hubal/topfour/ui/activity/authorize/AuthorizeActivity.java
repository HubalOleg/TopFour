package com.oleg.hubal.topfour.ui.activity.authorize;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.oleg.hubal.topfour.R;
import com.oleg.hubal.topfour.presentation.presenter.authorize.AuthorizePresenter;
import com.oleg.hubal.topfour.presentation.view.authorize.AuthorizeView;
import com.oleg.hubal.topfour.ui.activity.main.MainActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthorizeActivity extends MvpAppCompatActivity implements AuthorizeView {
    public static final String TAG = "AuthorizeActivity";

    @InjectPresenter
    AuthorizePresenter mAuthorizePresenter;

    @ProvidePresenter
    AuthorizePresenter provideAuthorizePresenter() {
        return new AuthorizePresenter(AuthorizeActivity.this);
    }

    public static Intent getIntent(final Context context) {
        return new Intent(context, AuthorizeActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorize);
        ButterKnife.bind(AuthorizeActivity.this);
    }

    @OnClick(R.id.btn_foursquare_login)
    public void OnFourSquareLoginClick() {
        mAuthorizePresenter.onFoursquareLoginClick();
    }

    @Override
    public void showError(String error) {
        Toast.makeText(AuthorizeActivity.this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendActivityRequest(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mAuthorizePresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void userAuthorized() {
        startActivity(MainActivity.getIntent(AuthorizeActivity.this));
        finish();
    }
}
