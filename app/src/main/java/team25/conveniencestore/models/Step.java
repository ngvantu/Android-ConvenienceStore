package team25.conveniencestore.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class Step implements Parcelable {
    public Distance distance;
    public Duration duration;
    public LatLng startLocation;
    public LatLng endLocation;
    public String instructions;

    public Step() {

    }

    public Step(Parcel in) {
        startLocation = in.readParcelable(LatLng.class.getClassLoader());
        endLocation = in.readParcelable(LatLng.class.getClassLoader());
        instructions = in.readString();
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(startLocation, flags);
        dest.writeParcelable(endLocation, flags);
        dest.writeString(instructions);
    }
}
