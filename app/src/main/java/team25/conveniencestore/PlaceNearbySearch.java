package team25.conveniencestore;

import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import team25.conveniencestore.models.GooglePlace;

public class PlaceNearbySearch {

    private static final String NEARBY_SEARCH_PLACE_URL_API = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    private static final String GOOGLE_API_KEY = "AIzaSyCB0faLt9sjgmFeAv4MeLQUE3yKovTMWjw";
    private static final String TYPE_NEARBY_PLACE = "convenience_store";
    private static final String DEFAULT_KEYWORD = "cửa hàng tiện lợi";
    private static final int PROXIMITY_RADIUS = 500;
    private double latitude, longtitude;
    private String keyWord;
    private GoogleMap mMap;

    public PlaceNearbySearch(GoogleMap mMap, double latitude, double longtitude, String keyWord){
        this.mMap = mMap;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.keyWord = keyWord;
    }

    public void execute() throws UnsupportedEncodingException {
        String url = createURL();
        new DownloadRawData().execute(mMap, url);
    }

    private String createURL() throws UnsupportedEncodingException {
        //String urlNearbyPlace = URLEncoder.encode(TYPE_NEARBY_PLACE, "utf-8");
        String urlKeyWord = URLEncoder.encode(keyWord, "utf-8");

        /*
        return NEARBY_SEARCH_PLACE_URL_API + "location=" + latitude + "," + longtitude
                + "&radius=" + PROXIMITY_RADIUS
                + "&type=" + TYPE_NEARBY_PLACE
                + "&keyword=" + urlKeyWord
                + "&key=" +GOOGLE_API_KEY;
        */
        /*
        https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=10.7624218,106.6790126&rankby=distance&type=convenience_store&keyword=c%E1%BB%ADa%20h%C3%A0ng%20ti%E1%BB%87n%20l%E1%BB%A3i&key=AIzaSyCB0faLt9sjgmFeAv4MeLQUE3yKovTMWjw
         */
        return NEARBY_SEARCH_PLACE_URL_API + "location=" + latitude + "," + longtitude
                + "&rankby=distance"
                + "&type=" + TYPE_NEARBY_PLACE
                + "&keyword=" + urlKeyWord
                + "&key=" +GOOGLE_API_KEY;
    }

    private class DownloadRawData extends AsyncTask<Object, String, String> {

        private GoogleMap mMap;

        @Override
        protected String doInBackground(Object... objects) {
            mMap = (GoogleMap) objects[0];
            String link = (String) objects[1];

            try {
                URL url = new URL(link);
                InputStream is = url.openConnection().getInputStream();
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonString) {
            List<GooglePlace> nearbyPlaceList;
            PlaceParser placeParser = new PlaceParser();
            nearbyPlaceList = placeParser.parseJson(jsonString);
            showNearbyPlaces(nearbyPlaceList);
        }

        private void showNearbyPlaces(List<GooglePlace> nearbyPlaceList) {
            for (int i = 0; i < nearbyPlaceList.size(); i++) {
                MarkerOptions markerOptions = new MarkerOptions();
                GooglePlace googlePlace = nearbyPlaceList.get(i);

                String placeName = googlePlace.getName();
                String vicinity = googlePlace.getVicinity();
                LatLng latLng = googlePlace.getLatLng();
                String placeID = googlePlace.getId();

                markerOptions.position(latLng);
                markerOptions.title(placeName + " : " + vicinity);
                markerOptions.snippet(placeID);

                makeMarkerIconForStore(markerOptions, placeName);

                mMap.addMarker(markerOptions);
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longtitude), 15f));
        }

        void makeMarkerIconForStore(MarkerOptions markerOptions, String storeName)
        {
            if(storeName.toLowerCase().contains("family"))
            {
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.markerfamily));
            }
            else if(storeName.toLowerCase().contains("circle"))
            {
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.markerk));
            }
            else if(storeName.toLowerCase().contains("mini"))
            {
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.markermini));
            }
            else if(storeName.toLowerCase().contains("b's"))
            {
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.markerbmart));
            }
            else if(storeName.toLowerCase().contains("vinmart"))
            {
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.markervin));
            }
            else
            {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            }
        }
    }
}
