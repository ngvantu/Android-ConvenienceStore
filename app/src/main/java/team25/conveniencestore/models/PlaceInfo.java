package team25.conveniencestore.models;

import com.google.android.gms.maps.model.LatLng;

public class PlaceInfo {

    private String placeID;
    private LatLng latLng;
    private String name;
    private String address;
    private String phoneNumber;
    private double rating;

    public PlaceInfo(String placeID, LatLng latLng, String name, String address, String phoneNumber, double rating) {
        this.placeID = placeID;
        this.latLng = latLng;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.rating = rating;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
