package com.oleg.hubal.topfour.ui.activity.cache_places;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.oleg.hubal.topfour.R;
import com.oleg.hubal.topfour.presentation.presenter.cache_places.CachePlacesPresenter;
import com.oleg.hubal.topfour.presentation.view.cache_places.CachePlacesView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CachePlacesActivity extends MvpAppCompatActivity implements CachePlacesView {
    public static final String TAG = "CachePlacesActivity";

    private ProgressDialog mProgressDialog;

    @BindView(R.id.tv_ask_location)
    TextView mAskLocationTextView;
    @BindView(R.id.btn_is_location_correct)
    Button mIsCorrectLocationButton;

    @InjectPresenter
    CachePlacesPresenter mCachePlacesPresenter;

    @ProvidePresenter
    CachePlacesPresenter provideCachePlacesPresenter() {
        return new CachePlacesPresenter(CachePlacesActivity.this);
    }

    public static Intent getIntent(final Context context) {
        Intent intent = new Intent(context, CachePlacesActivity.class);

        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache_places);
        ButterKnife.bind(CachePlacesActivity.this);
        mCachePlacesPresenter.onReadyToLoadUserData();
    }

    @Override
    public void showProgressDialog() {
        mProgressDialog = new ProgressDialog(CachePlacesActivity.this);
        mProgressDialog.setTitle("Downloading data");
        mProgressDialog.setMessage("Please wait few seconds");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
    }

    @Override
    public void dismissProgressDialog() {
        mProgressDialog.dismiss();
    }

    @Override
    public void showLocation(String location) {
        mAskLocationTextView.setText("Is " + location + " your current location?");
    }
}
