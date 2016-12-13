package com.oleg.hubal.topfour.model.api.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.oleg.hubal.topfour.model.CacheableItem;
import com.oleg.hubal.topfour.model.VenueItem;
import com.oleg.hubal.topfour.model.database.VenueDB;

/**
 * Created by User on 12.12.2016.
 */

public class Venue implements VenueItem, CacheableItem {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("location")
    @Expose
    private Location location;

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
    public String getCity() {
        return location.getCity();
    }

    @Override
    public String getCountry() {
        return location.getCountry();
    }

    @Override
    public String getCrossStreet() {
        return location.getCrossStreet();
    }

    @Override
    public void saveToDatabase() {
        new VenueDB(id, name, getAddress(), getCrossStreet(), getCity(), getCountry())
                .save();
    }
}
