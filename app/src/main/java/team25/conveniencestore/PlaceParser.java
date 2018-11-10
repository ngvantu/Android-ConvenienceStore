package team25.conveniencestore;

import android.util.Log;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import team25.conveniencestore.models.PlaceInfo;

public class PlaceParser {

    public List<PlaceInfo> parseJson(String jsonData)
    {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        Log.d("json data", jsonData);

        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getPlaces(jsonArray);
    }

    private List<PlaceInfo>getPlaces(JSONArray jsonArray)
    {
        int count = jsonArray.length();
        //List<HashMap<String, String>> placeList = new ArrayList<>();
        List<PlaceInfo> placeList = new ArrayList<>();
        PlaceInfo placeMap = null;

        for(int i = 0; i<count;i++)
        {
            try {
                placeMap = getPlace((JSONObject) jsonArray.get(i));
                placeList.add(placeMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return placeList;
    }

    private PlaceInfo getPlace(JSONObject googlePlaceJson)
    {
        String placeName = "--NA--";
        String vicinity= "--NA--";
        double latitude= 0;
        double longitude= 0;
        double rating = 0;
        String reference="";

        Log.d("DataParser","jsonobject ="+googlePlaceJson.toString());


        try {
            if (!googlePlaceJson.isNull("name")) {
                placeName = googlePlaceJson.getString("name");
            }
            if (!googlePlaceJson.isNull("vicinity")) {
                vicinity = googlePlaceJson.getString("vicinity");
            }

            if (!googlePlaceJson.isNull("rating")){
                rating = googlePlaceJson.getDouble("rating");
            }

            latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getDouble("lng");

            reference = googlePlaceJson.getString("reference");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return new PlaceInfo(reference, placeName, vicinity, new LatLng(latitude, longitude), rating);
    }
}