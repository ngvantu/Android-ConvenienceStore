package team25.conveniencestore.interfaces;

import java.util.List;

import team25.conveniencestore.models.GooglePlace;

public interface SearchStoresListener {
    void onSearchStoresStart();
    void onSearchStoresSuccess(List<GooglePlace> results);
}
