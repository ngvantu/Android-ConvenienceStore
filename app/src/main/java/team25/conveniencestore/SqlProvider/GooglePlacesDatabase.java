package team25.conveniencestore.SqlProvider;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import team25.conveniencestore.models.GooglePlace;


@Database(entities = {GooglePlace.class}, version = 1)
@TypeConverters({Converter.class})
public abstract class GooglePlacesDatabase extends RoomDatabase {

    private static GooglePlacesDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final GooglePlacesDAO mDao;

        PopulateDbAsync(GooglePlacesDatabase db) {
            mDao = db.placesDAO();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mDao.insertPlace( new GooglePlace(String.valueOf(1),"Cửa hàng 1","2a Trần Phú, Phường 4, Quận 5, Tp HCM", new LatLng(10.764006, 106.679985),4.6));
            mDao.insertPlace( new GooglePlace(String.valueOf(2),"Cửa hàng 2","Nguyễn Thị Minh Khai, Tp HCM", new LatLng(10.766620, 106.683096),4.8));
            mDao.insertPlace( new GooglePlace(String.valueOf(3),"Cửa hàng 3","179 Nguyễn Cư Trinh, Quận 1, Tp HCM", new LatLng(10.762615, 106.687130),3.9));
            return null;
        }
    }

    private static volatile GooglePlacesDatabase INSTANCE;

    public abstract GooglePlacesDAO placesDAO();


    public static GooglePlacesDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (GooglePlacesDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            GooglePlacesDatabase.class, "favorite_database")
                            .addCallback(sRoomDatabaseCallback)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}