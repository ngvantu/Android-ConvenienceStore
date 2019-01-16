package team25.conveniencestore.activitys;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import team25.conveniencestore.R;
import team25.conveniencestore.placeinfo_fragments.PlaceInfoTab1;
import team25.conveniencestore.placeinfo_fragments.PlaceInfoTab2;
import team25.conveniencestore.placeinfo_fragments.PlaceInfoTab3;
import team25.conveniencestore.placeinfo_fragments.PlaceInfoTab3NotLogin;
import team25.conveniencestore.placeinfo_fragments.PlaceInfoTabError;
import team25.conveniencestore.placeinfo_fragments.SectionsPageAdapter;

public class PlaceInfoActivity extends AppCompatActivity {

    private static final String TAG = "PlaceInfoActivity";
    private ViewPager mViewPager;

    FirebaseUser user;
    private static final int RC_SIGN_IN = 2;

    String API_KEY = "AIzaSyBKTFVy1RkiGaN8mmHuvlBXoFKf3DgNVpw";
    String placeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_info);

        Bundle bundle = getIntent().getExtras();
        placeID = bundle.getString("PLACE_ID");

        if (placeID != null) {
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "https://maps.googleapis.com/maps/api/place/details/json?"
                            + "key=" + API_KEY
                            + "&placeid=" + placeID;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonRes = new JSONObject(response);

                        mViewPager = findViewById(R.id.container);
                        setupViewPager(mViewPager, jsonRes);

                        TabLayout tabLayout = findViewById(R.id.tabs);
                        tabLayout.setupWithViewPager(mViewPager);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Error here
                }
            });
            queue.add(stringRequest);
        }
    }

    private void setupViewPager(ViewPager mViewPager, JSONObject jsonRes) throws JSONException {

        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        Fragment tab1, tab2, tab3;
        Bundle bundle = new Bundle();

        if (jsonRes.get("status").toString().equalsIgnoreCase("OK")) {
            jsonRes = jsonRes.getJSONObject("result");
            JSONObject jsonGeo = jsonRes.getJSONObject("geometry").getJSONObject("location");

            String jsonStringReviews = jsonRes.getJSONArray("reviews").toString();;

            tab1 = new PlaceInfoTab1();
            tab2 = new PlaceInfoTab2();

            bundle.putString("STORE_NAME", jsonRes.get("name").toString());
            bundle.putString("STORE_ADDRESS", jsonRes.get("formatted_address").toString());

            if(!jsonRes.isNull("formatted_phone_number")) {
                bundle.putString("STORE_PHONE", jsonRes.get("formatted_phone_number").toString());
            } else {
                bundle.putString("STORE_PHONE", "");
            }

            bundle.putString("REVIEWS", jsonStringReviews);
            bundle.putDouble("RATING", jsonRes.getDouble("rating"));
            bundle.putString("PLACE_ID", placeID);
            bundle.putDouble("LAT", jsonGeo.getDouble("lat"));
            bundle.putDouble("LNG", jsonGeo.getDouble("lng"));

            tab1.setArguments(bundle);
            tab2.setArguments(bundle);

            if(FirebaseAuth.getInstance().getCurrentUser() != null) {
                tab3 = new PlaceInfoTab3();
                tab3.setArguments(bundle);
            } else {
                tab3 = new PlaceInfoTab3NotLogin();
            }

            adapter.addFragment(tab1, "Tổng quan");
            adapter.addFragment(tab2, "Dữ liệu Google");
            adapter.addFragment(tab3, "Đánh giá");
        } else {
            tab1 = new PlaceInfoTabError();

            bundle.putString("ERROR_STATUS", jsonRes.get("status").toString());
            bundle.putString("ERROR_MESSAGE", jsonRes.get("error_message").toString());

            tab1.setArguments(bundle);

            adapter.addFragment(tab1, "Error");
        }

        mViewPager.setAdapter(adapter);
    }
}
