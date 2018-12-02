package team25.conveniencestore.activitys;

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

import org.json.JSONException;
import org.json.JSONObject;

import team25.conveniencestore.R;
import team25.conveniencestore.placeinfo_fragments.PlaceInfoTab1;
import team25.conveniencestore.placeinfo_fragments.PlaceInfoTab2;
import team25.conveniencestore.placeinfo_fragments.PlaceInfoTab3;
import team25.conveniencestore.placeinfo_fragments.PlaceInfoTabError;
import team25.conveniencestore.placeinfo_fragments.SectionsPageAdapter;

public class PlaceInfoActivity extends AppCompatActivity {

    private static final String TAG = "PlaceInfoActivity";
    private ViewPager mViewPager;

    String API_KEY = "AIzaSyDVFmBKLQHSndjqgqRfU_7JpWS-imp2n40";

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

        Bundle bundle = new Bundle();

//        Fragment tab1 = new PlaceInfoTab1();
//        Fragment tab2 = new PlaceInfoTab2();
//        Fragment tab3 = new PlaceInfoTab3();
//
//        Bundle bundle = new Bundle();
//
//        if (jsonRes.get("status").toString().equalsIgnoreCase("OK")) {
//            jsonRes = jsonRes.getJSONObject("result");
//
//            bundle.putString("STORE_NAME", jsonRes.get("name").toString());
//            bundle.putString("STORE_ADDRESS", jsonRes.get("formatted_address").toString());
//            bundle.putString("STORE_PHONE", jsonRes.get("formatted_phone_number").toString());
//        } else {
//            bundle.putString("STORE_NAME", "NOT_AVAILABLE");
//            bundle.putString("STORE_ADDRESS", "NOT_AVAILABLE");
//            bundle.putString("STORE_PHONE", "NOT_AVAILABLE");
//        }
//
//        tab1.setArguments(bundle);
//
//        adapter.addFragment(tab1, "Tổng quan");
//        adapter.addFragment(tab2, "Dữ liệu Google");
//        adapter.addFragment(tab3, "Đánh giá");

        if (jsonRes.get("status").toString().equalsIgnoreCase("OK")) {
            jsonRes = jsonRes.getJSONObject("result");
            String jsonStringReviews = jsonRes.getJSONArray("reviews").toString();

            Fragment tab1 = new PlaceInfoTab1();
            Fragment tab2 = new PlaceInfoTab2();
            Fragment tab3 = new PlaceInfoTab3();

            bundle.putString("STORE_NAME", jsonRes.get("name").toString());
            bundle.putString("STORE_ADDRESS", jsonRes.get("formatted_address").toString());
            bundle.putString("STORE_PHONE", jsonRes.get("formatted_phone_number").toString());

            bundle.putString("REVIEWS", jsonStringReviews);
            bundle.putDouble("RATING", jsonRes.getDouble("rating"));

            tab1.setArguments(bundle);
            tab2.setArguments(bundle);

            adapter.addFragment(tab1, "Tổng quan");
            adapter.addFragment(tab2, "Dữ liệu Google");
            adapter.addFragment(tab3, "Đánh giá");
        } else {
            Fragment tab1 = new PlaceInfoTabError();

            bundle.putString("ERROR_STATUS", jsonRes.get("status").toString());
            bundle.putString("ERROR_MESSAGE", jsonRes.get("error_message").toString());

            tab1.setArguments(bundle);

            adapter.addFragment(tab1, "Error");
        }

        mViewPager.setAdapter(adapter);
    }

}
