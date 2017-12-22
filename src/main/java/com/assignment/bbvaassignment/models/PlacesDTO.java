package com.assignment.bbvaassignment.models;

import java.io.Serializable;

public class PlacesDTO implements Serializable{

    private String name;
    private String vicinity;
    private double Latitude;
    private double Longitude;
    private double CurrentLatitude;
    private double CurrentLongitude;
    private String Place_ID;
    private String PlaceRating;
    private String formatted_address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getCurrentLatitude() {
        return CurrentLatitude;
    }

    public void setCurrentLatitude(double currentLatitude) {
        CurrentLatitude = currentLatitude;
    }

    public double getCurrentLongitude() {
        return CurrentLongitude;
    }

    public void setCurrentLongitude(double currentLongitude) {
        CurrentLongitude = currentLongitude;
    }

    public String getPlace_ID() {
        return Place_ID;
    }

    public void setPlace_ID(String place_ID) {
        Place_ID = place_ID;
    }

    public String getPlaceRating() {
        return PlaceRating;
    }

    public void setPlaceRating(String placeRating) {
        PlaceRating = placeRating;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }
}
