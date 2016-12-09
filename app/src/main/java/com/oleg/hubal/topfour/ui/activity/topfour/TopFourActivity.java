package com.oleg.hubal.topfour.ui.activity.topfour;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.oleg.hubal.topfour.R;
import com.oleg.hubal.topfour.presentation.presenter.topfour.TopFourPresenter;
import com.oleg.hubal.topfour.presentation.view.topfour.TopFourView;

public class TopFourActivity extends MvpAppCompatActivity implements TopFourView {
    public static final String TAG = "TopFourActivity";
    @InjectPresenter
    TopFourPresenter mTopFourPresenter;

    public static Intent getIntent(final Context context) {
        Intent intent = new Intent(context, TopFourActivity.class);

        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_four);
    }
}
