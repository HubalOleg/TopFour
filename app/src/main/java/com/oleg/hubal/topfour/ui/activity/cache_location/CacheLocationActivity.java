package com.oleg.hubal.topfour.ui.activity.cache_location;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.oleg.hubal.topfour.R;
import com.oleg.hubal.topfour.presentation.presenter.cache_location.CacheLocationPresenter;
import com.oleg.hubal.topfour.presentation.view.cache_location.CacheLocationView;
import com.oleg.hubal.topfour.ui.activity.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CacheLocationActivity extends MvpAppCompatActivity implements CacheLocationView {
    public static final String TAG = "CacheLocationActivity";

    private ProgressDialog mProgressDialog;

    @BindView(R.id.tv_ask_location)
    TextView mAskLocationTextView;
    @BindView(R.id.et_location)
    EditText mLocationEditText;

    @InjectPresenter
    CacheLocationPresenter mCacheLocationPresenter;

    @ProvidePresenter
    CacheLocationPresenter provideCachePlacesPresenter() {
        return new CacheLocationPresenter(CacheLocationActivity.this);
    }

    public static Intent getIntent(final Context context) {
        Intent intent = new Intent(context, CacheLocationActivity.class);

        return intent;
    }

    @OnClick(R.id.btn_location_correct)
    public void onLocationCorrectClick() {
        String location = mLocationEditText.getText().toString();
        mCacheLocationPresenter.onLocationCorrectClick(location);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache_location);
        ButterKnife.bind(CacheLocationActivity.this);
        mCacheLocationPresenter.onReadyToLoadUserData();
    }

    @Override
    public void showLocation(String location) {
        mAskLocationTextView.setText(String.format(getString(R.string.ask_location), location));
    }

    @Override
    public void askAnotherLocation() {
        mAskLocationTextView.setText(R.string.incorrect_location);
        mLocationEditText.setVisibility(View.VISIBLE);
    }

    @Override
    public void showTopFourActivity() {
        startActivity(MainActivity.getIntent(CacheLocationActivity.this));
        finish();
    }
}
