package team25.conveniencestore;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import team25.conveniencestore.models.GooglePlace;

public class PlaceParser {

    public List<GooglePlace> parseJson(String jsonData)
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

    private List<GooglePlace>getPlaces(JSONArray jsonArray)
    {
        int count = jsonArray.length();
        //List<HashMap<String, String>> placeList = new ArrayList<>();
        List<GooglePlace> placeList = new ArrayList<>();
        GooglePlace placeMap = null;

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

    private GooglePlace getPlace(JSONObject googlePlaceJson)
    {
        String placeID="";
        String placeName = "--NA--";
        String vicinity= "--NA--";
        double latitude= 0;
        double longitude= 0;
        double rating = 0;

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

            placeID = googlePlaceJson.getString("reference");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return new GooglePlace(placeID, placeName, vicinity, new LatLng(latitude, longitude), rating);
    }
}