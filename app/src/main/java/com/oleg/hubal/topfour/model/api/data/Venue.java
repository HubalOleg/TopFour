package com.oleg.hubal.topfour.model.api.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.oleg.hubal.topfour.model.DatabaseItem;
import com.oleg.hubal.topfour.model.VenueItem;
import com.oleg.hubal.topfour.model.database.VenueDB;

/**
 * Created by User on 12.12.2016.
 */

public class Venue implements VenueItem, DatabaseItem {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("location")
    @Expose
    private Location location;
    private String photoUrl = "";
    private boolean isCached = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String getAddress() {
        return location.getAddress();
    }

    @Override
    public void setAddress(String address) {
        location.setAddress(address);
    }

    @Override
    public String getCity() {
        return location.getCity();
    }

    @Override
    public void setCity(String city) {
        location.setCity(city);
    }

    @Override
    public String getCountry() {
        return location.getCountry();
    }

    @Override
    public void setCountry(String country) {
        location.setCountry(country);
    }

    @Override
    public String getPhotoUrl() {
        return photoUrl;
    }

    @Override
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Override
    public boolean isCached() {
        return isCached;
    }

    @Override
    public void setCached(boolean cached) {
        isCached = cached;
    }

    @Override
    public void saveToDatabase() {
        new VenueDB(id, name, getAddress(), getCity(), getCountry(), photoUrl)
                .save();
    }
}
