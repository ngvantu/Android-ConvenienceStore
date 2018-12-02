package team25.conveniencestore.SqlProvider;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;


@Database(entities = {FavoritePlaces.class}, version = 1)
public abstract class FavoritePlacesDatabase extends RoomDatabase {

    private static FavoritePlacesDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final FavoritePlacesDao mDao;

        PopulateDbAsync(FavoritePlacesDatabase db) {
            mDao = db.placesDAO();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mDao.insertPlace( new FavoritePlaces(1,"test place",10,10,"he he",5));
            mDao.insertPlace( new FavoritePlaces(2,"test place",10,10,"he he",5));
            mDao.insertPlace( new FavoritePlaces(3,"test place",10,10,"he he",5));
            mDao.insertPlace( new FavoritePlaces(4,"test place",10,10,"he he",5));
            return null;
        }
    }

    private static volatile FavoritePlacesDatabase INSTANCE;

    public abstract FavoritePlacesDao placesDAO();


    public static FavoritePlacesDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (FavoritePlacesDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FavoritePlacesDatabase.class, "favorite_database")
                            .addCallback(sRoomDatabaseCallback)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}