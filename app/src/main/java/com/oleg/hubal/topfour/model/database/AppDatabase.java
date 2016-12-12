package com.oleg.hubal.topfour.model.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by User on 12.12.2016.
 */

@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {
    public static final String NAME = "TopFourDatabase";
    public static final int VERSION = 1;
}
