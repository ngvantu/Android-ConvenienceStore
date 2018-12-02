package team25.conveniencestore.SqlProvider;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "FavoritePlaces")
public class FavoritePlaces implements Parcelable {
    @PrimaryKey()
    public int id;
    @ColumnInfo(name = "lat")
    public double lat;
    @ColumnInfo(name = "lng")
    public double lng;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "vicinity")
    public String vicinity;
    @ColumnInfo(name = "rating")
    public double rating;

    public FavoritePlaces() {
    }

    @Override
    public String toString() {
        StringBuilder strbd = new StringBuilder();
        strbd.append("Id: ");
        strbd.append(id);
        strbd.append("; Name: ");
        strbd.append(name);
        strbd.append("; Rating: ");
        strbd.append(rating);
        return strbd.toString();
    }

    public FavoritePlaces(int _id, String _name, double _lat, double _long, String _vicinity, double _rating) {
        id = _id;
        name = _name;
        lat = _lat;
        lng = _long;
        vicinity = _vicinity;
        rating = _rating;
    }

    protected FavoritePlaces(Parcel in) {
        id = in.readInt();
        lat = in.readDouble();
        lng = in.readDouble();
        name = in.readString();
        vicinity = in.readString();
        rating = in.readDouble();
    }

    public static final Creator<FavoritePlaces> CREATOR = new Creator<FavoritePlaces>() {
        @Override
        public FavoritePlaces createFromParcel(Parcel in) {
            return new FavoritePlaces(in);
        }

        @Override
        public FavoritePlaces[] newArray(int size) {
            return new FavoritePlaces[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(vicinity);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
        dest.writeDouble(rating);
    }
}