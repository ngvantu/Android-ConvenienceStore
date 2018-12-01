package team25.conveniencestore.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class GooglePlace implements Parcelable{
    private String id;
    private String name;
    private String vicinity;
    private LatLng latLng;
    private double rating;

    public GooglePlace(String id, String name, String vicinity, LatLng latLng, double rating) {
        this.id = id;
        this.name = name;
        this.vicinity = vicinity;
        this.latLng = latLng;
        this.rating = rating;
    }

    protected GooglePlace(Parcel in) {
        id = in.readString();
        name = in.readString();
        vicinity = in.readString();
        latLng = in.readParcelable(LatLng.class.getClassLoader());
        rating = in.readDouble();
    }

    public static final Creator<GooglePlace> CREATOR = new Creator<GooglePlace>() {
        @Override
        public GooglePlace createFromParcel(Parcel in) {
            return new GooglePlace(in);
        }

        @Override
        public GooglePlace[] newArray(int size) {
            return new GooglePlace[size];
        }
    };

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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(vicinity);
        dest.writeParcelable(latLng, flags);
        dest.writeDouble(rating);
    }
}
