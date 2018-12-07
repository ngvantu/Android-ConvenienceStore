package team25.conveniencestore;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

import team25.conveniencestore.interfaces.FindPlaceListener;
import team25.conveniencestore.models.GooglePlace;

public class FindPlace {

    private static final String URL_API = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=";
    //private static final String GOOGLE_API_KEY = "AIzaSyCB0faLt9sjgmFeAv4MeLQUE3yKovTMWjw";
    private String keyWord;
    private GoogleMap mMap;
    private Context context;
    private FindPlaceListener findPlaceListener;

    public FindPlace(Context context, GoogleMap mMap, String keyWord, FindPlaceListener findPlaceListener){
        this.context = context;
        this.mMap = mMap;
        this.keyWord = keyWord;
        this.findPlaceListener = findPlaceListener;
    }

    private String createURL() throws UnsupportedEncodingException {
        String urlKeyWord = URLEncoder.encode(keyWord, "utf-8");
        return URL_API + urlKeyWord
                + "&inputtype=textquery&fields=formatted_address,name,geometry&key="
                + context.getResources().getString(R.string.google_maps_key);
    }

    public void execute() throws UnsupportedEncodingException {
        String url = createURL();
        findPlaceListener.onFindPlaceStart();
        new DownloadRawData().execute(mMap, url);
    }

    private class DownloadRawData extends AsyncTask<Object, String, String>{

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
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonString){
            try {
                Log.d("DataParser","jsonobject ="+jsonString);
                GooglePlace googlePlace = null;
                JSONObject jsonCandidates = null;
                if (jsonString != null) {
                    jsonCandidates = new JSONObject(jsonString);
                    if (jsonCandidates.get("status").toString().equalsIgnoreCase("OK")) {
                        JSONArray jsonArray = jsonCandidates.getJSONArray("candidates");
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        double lat = jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                        double lng = jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                        String name = jsonObject.getString("name");
                        String address = jsonObject.getString("formatted_address");

                        googlePlace = new GooglePlace("0", name, address, new LatLng(lat, lng), 0);
                    }
                }
                findPlaceListener.onFindPlaceSuccess(googlePlace);
            } catch (JSONException e) {
                Toast.makeText(context.getApplicationContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
