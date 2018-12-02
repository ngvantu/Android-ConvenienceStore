package team25.conveniencestore.SqlProvider;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import team25.conveniencestore.models.GooglePlace;

@Dao
public interface GooglePlacesDAO {
    @Query("SELECT * FROM GooglePlace WHERE id = :placeid")
    GooglePlace getPlaceById(int placeid);

    @Query("SELECT * FROM GooglePlace")
    List<GooglePlace> getAllPlace();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPlace(GooglePlace place);

    @Delete
    void deletePlace(GooglePlace places);
}