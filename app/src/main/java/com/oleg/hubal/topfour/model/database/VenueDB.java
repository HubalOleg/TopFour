package com.oleg.hubal.topfour.model.database;

import com.oleg.hubal.topfour.model.VenueItem;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by User on 12.12.2016.
 */

@Table(database = AppDatabase.class)
public class VenueDB extends BaseModel implements VenueItem {

    @Column
    @PrimaryKey
    private String id;
    @Column
    private String name;
    @Column
    private String address;
    @Column
    private String city;
    @Column
    private String country;
    @Column
    private String photoUrl;

    private boolean isCached = false;

    public VenueDB() {
    }

    public VenueDB(String id, String name, String address, String city, String country, String photoUrl) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.country = country;
        this.photoUrl = photoUrl;
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

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

    public void saveToDatabase() {
        save();
    }
}
