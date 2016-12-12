package com.oleg.hubal.topfour.global;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by User on 12.12.2016.
 */

public class TopFourApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(new FlowConfig.Builder(TopFourApplication.this).build());
    }
}
