package com.oleg.hubal.topfour.presentation.events;

import com.oleg.hubal.topfour.model.api.data.User;

/**
 * Created by User on 14.12.2016.
 */

public class LoadUserDataEvent {
    public final User mUser;

    public LoadUserDataEvent(User user) {
        mUser = user;
    }
}
