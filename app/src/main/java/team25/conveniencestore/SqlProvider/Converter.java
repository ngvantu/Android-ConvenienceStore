package team25.conveniencestore.SqlProvider;

import android.arch.persistence.room.TypeConverter;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import team25.conveniencestore.models.GooglePlace;

public class Converter {
    @TypeConverter
    public LatLng StringToLatLng(String value) {
        if (value == null)
            return null;
        String[] latlng = value.split(",");
        return new LatLng(Double.parseDouble(latlng[0]), Double.parseDouble(latlng[1]));
    }

    @TypeConverter
    public String LatLngToString(LatLng latLng) {
        return (latLng == null) ? null : latLng.latitude + ", " + latLng.longitude;
    }
}
