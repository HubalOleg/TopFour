package com.oleg.hubal.topfour.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 12.12.2016.
 */

public class UserData {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("relationship")
    @Expose
    private String relationship;
    @SerializedName("canonicalUrl")
    @Expose
    private String canonicalUrl;
    @SerializedName("photo")
    @Expose
    private Photo photo;
    @SerializedName("homeCity")
    @Expose
    private String homeCity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getCanonicalUrl() {
        return canonicalUrl;
    }

    public void setCanonicalUrl(String canonicalUrl) {
        this.canonicalUrl = canonicalUrl;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public String getHomeCity() {
        return homeCity;
    }

    public void setHomeCity(String homeCity) {
        this.homeCity = homeCity;
    }
}
