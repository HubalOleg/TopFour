package com.oleg.hubal.topfour.model;

/**
 * Created by User on 13.12.2016.
 */

public interface VenueItem {
    String getId();
    void setId(String id);
    String getName();
    void setName(String name);
    String getAddress();
    void setAddress(String address);
    String getCity();
    void setCity(String city);
    String getCountry();
    void setCountry(String country);
    String getPhotoUrl();
    void setPhotoUrl(String photoUrl);
    void setCached(boolean isCached);
    boolean isCached();
    void saveToDatabase();
}
