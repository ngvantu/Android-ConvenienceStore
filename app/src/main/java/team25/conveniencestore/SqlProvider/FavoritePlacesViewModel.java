package team25.conveniencestore.SqlProvider;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import java.util.List;

public class FavoritePlacesViewModel extends AndroidViewModel {

    private FavoritePlacesRepository mRepository;

    private List<FavoritePlaces> mAllPlace;

    public FavoritePlacesViewModel (Application application) {
        super(application);
        mRepository = new FavoritePlacesRepository(application);
        mAllPlace = mRepository.getALlPlace();
    }

    public List<FavoritePlaces> getAllWords() { return mAllPlace; }

    public FavoritePlaces getPlaceById(int id) { return mRepository.getPlaceById(id); }


    public void insertPlace(FavoritePlaces place) { mRepository.insertPlace(place); }

    public void deletePlace(FavoritePlaces place) { mRepository.deletePlace(place); }
}