package team25.conveniencestore;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import team25.conveniencestore.models.DirectionFinder;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class PlaceInfoActivity extends FragmentActivity {

    private TextView txtName;
    private TextView txtAddress;
    private TextView txtPhone;
    private TextView txtRating;
    private TextView txtError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placeinfo);

        txtName = (TextView) findViewById(R.id.placeinfo_name);
        txtAddress = (TextView) findViewById(R.id.placeinfo_address);
        txtPhone = (TextView) findViewById(R.id.placeinfo_phone);
        txtRating = (TextView) findViewById(R.id.placeinfo_rating);
        txtError = (TextView) findViewById(R.id.error_message);

        String API_KEY = "AIzaSyCB0faLt9sjgmFeAv4MeLQUE3yKovTMWjw";
        Bundle bundle = getIntent().getExtras();
        String placeID = bundle.getString("PLACE_ID");

        if(placeID != null) {
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "https://maps.googleapis.com/maps/api/place/details/json?"
                    + "key=" + API_KEY
                    + "&placeid=" + placeID;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.v("responsefromrequest", response);
                    try {
                        JSONObject jsonRes = new JSONObject(response);

                        if (jsonRes.get("status").toString().equalsIgnoreCase("OK")) {
                            // Fetch thanh cong
                            jsonRes = jsonRes.getJSONObject("result");

                            String formattedAddress = jsonRes.get("formatted_address").toString();
                            String storeName = jsonRes.get("name").toString();
                            String formattedPhone = jsonRes.get("formatted_phone_number").toString();
                            String ratingPoint = jsonRes.get("rating").toString();

                            txtName.setText(storeName);
                            txtAddress.setText(formattedAddress);
                            txtPhone.setText(formattedPhone);
                            txtRating.setText(ratingPoint + "/5");
                        } else {
                            // Fetch that bai, API Key bi gioi han thoi gian su dung trong 1 ngay
                            txtError.setText("Error: " + jsonRes.get("error_message").toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            queue.add(stringRequest);
        }
    }
}
