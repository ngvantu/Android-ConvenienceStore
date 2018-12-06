package team25.conveniencestore;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import team25.conveniencestore.interfaces.SearchStoresListener;

public class PlaceNearbySearch {

    private static final String NEARBY_SEARCH_PLACE_URL_API = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    //private static String GOOGLE_API_KEY = resources
    private static final String TYPE_NEARBY_PLACE = "convenience_store";
    private static final String DEFAULT_KEYWORD = "cửa hàng tiện lợi";
    private static final int PROXIMITY_RADIUS = 500;
    private double latitude, longtitude;
    private String keyWord;
    private SearchStoresListener searchStoresListener;
    private Context context;

    public PlaceNearbySearch(Context context, double latitude, double longtitude, String keyWord, SearchStoresListener searchStoresListener){
        this.context = context;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.keyWord = (keyWord.equals("Tất cả"))? DEFAULT_KEYWORD : keyWord;
        this.searchStoresListener = searchStoresListener;
    }

    public void execute() throws UnsupportedEncodingException {
        String url = createURL();
        searchStoresListener.onSearchStoresStart();
        new DownloadRawData().execute(url);
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
                + "&key=" + context.getResources().getString(R.string.google_maps_key);
    }

    private class DownloadRawData extends AsyncTask<Object, String, String> {

        @Override
        protected String doInBackground(Object... objects) {
            String link = (String) objects[0];

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
            PlaceParser placeParser = new PlaceParser();
            searchStoresListener.onSearchStoresSuccess(placeParser.parseJson(jsonString));
        }

    }
}
