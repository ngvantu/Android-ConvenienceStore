package team25.conveniencestore.interfaces;

import team25.conveniencestore.models.GooglePlace;

public interface FindPlaceListener {
    void onFindPlaceStart();
    void onFindPlaceSuccess(GooglePlace googlePlace);
}
