package team25.conveniencestore.models;

import com.google.android.gms.maps.model.LatLng;

public class PlaceInfo {
    private String id;
    private String name;
    private String vicinity;
    private LatLng latLng;
    private float rating;

    public PlaceInfo(String id, String name, String vicinity, LatLng latLng, float rating) {
        this.id = id;
        this.name = name;
        this.vicinity = vicinity;
        this.latLng = latLng;
        this.rating = rating;
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

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
