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

import org.w3c.dom.Text;

public class PlaceInfoActivity extends FragmentActivity {

    private TextView txtName;
    private TextView txtAddress;
    private TextView txtPhone;
    private TextView txtRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placeinfo);

        txtName = (TextView) findViewById(R.id.placeinfo_name);
        txtAddress = (TextView) findViewById(R.id.placeinfo_address);
        txtPhone = (TextView) findViewById(R.id.placeinfo_phone);
        txtRating = (TextView) findViewById(R.id.placeinfo_rating);

        String API_KEY = DirectionFinder.GOOGLE_API_KEY;
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
                    txtAddress.setText(response.substring(0, 100));
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
