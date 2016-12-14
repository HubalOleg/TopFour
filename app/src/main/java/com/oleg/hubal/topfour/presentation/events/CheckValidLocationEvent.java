package com.oleg.hubal.topfour.presentation.events;

/**
 * Created by User on 14.12.2016.
 */

public class CheckValidLocationEvent {
    public final boolean isValid;

    public CheckValidLocationEvent(boolean isValid) {
        this.isValid = isValid;
    }
}
