package com.oleg.hubal.topfour.ui.activity.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.oleg.hubal.topfour.ui.activity.authorize.AuthorizeActivity;
import com.oleg.hubal.topfour.ui.activity.topfour.TopFourActivity;
import com.oleg.hubal.topfour.utils.PreferenceManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public static Intent getIntent(final Context context) {
        Intent intent = new Intent(context, MainActivity.class);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkAuthorization();
    }

    private void checkAuthorization() {
        boolean isAuthorized = !TextUtils.isEmpty(PreferenceManager.getToken(MainActivity.this));
        if (isAuthorized) {
            launchTopFourActivity();
        } else {
            launchAuthorizeActivity();
        }
    }

    private void launchTopFourActivity() {
        Intent intent = new Intent(MainActivity.this, TopFourActivity.class);
        startActivity(intent);
    }

    private void launchAuthorizeActivity() {
        Intent intent = new Intent(MainActivity.this, AuthorizeActivity.class);
        startActivity(intent);
    }

}
