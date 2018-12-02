package team25.conveniencestore.SqlProvider;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import java.util.List;

import team25.conveniencestore.models.GooglePlace;

public class GooglePlacesViewModel extends AndroidViewModel {

    private GooglePlacesRepository mRepository;

    private List<GooglePlace> mAllPlace;

    public GooglePlacesViewModel(Application application) {
        super(application);
        mRepository = new GooglePlacesRepository(application);
        mAllPlace = mRepository.getAllPlace();
    }

    public List<GooglePlace> getAllWords() { return mAllPlace; }

    public GooglePlace getPlaceById(int id) { return mRepository.getPlaceById(id); }

    public void insertPlace(GooglePlace place) { mRepository.insertPlace(place); }

    public void deletePlace(GooglePlace place) { mRepository.deletePlace(place); }
}