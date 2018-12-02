package team25.conveniencestore.SqlProvider;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import team25.conveniencestore.models.GooglePlace;


@Database(entities = {GooglePlace.class}, version = 1)
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
            mDao.insertPlace( new GooglePlace(String.valueOf(1),"test place 1","he he", new LatLng(10.762683, 106.682108),4.6));
            mDao.insertPlace( new GooglePlace(String.valueOf(2),"test place 2","he he", new LatLng(10.762683, 106.682108),4.8));
            mDao.insertPlace( new GooglePlace(String.valueOf(3),"test place 3","he he", new LatLng(10.762683, 106.682108),3.9));
            mDao.insertPlace( new GooglePlace(String.valueOf(4),"test place 4","he he", new LatLng(10.762683, 106.682108),4.3));
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