package team25.conveniencestore.interfaces;

import java.util.List;

import team25.conveniencestore.models.Route;

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
