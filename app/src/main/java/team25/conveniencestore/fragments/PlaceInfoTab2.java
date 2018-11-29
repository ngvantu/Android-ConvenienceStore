package team25.conveniencestore.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import team25.conveniencestore.R;
import team25.conveniencestore.adapter.CommentAdapter;

public class PlaceInfoTab2 extends Fragment {

    private static final String TAG = "PlaceInfoTab2";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private TextView txtRating;
    private RatingBar ratingBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_info_tab2, container, false);

        txtRating = (TextView) view.findViewById(R.id.googleRatingText);
        ratingBar = (RatingBar) view.findViewById(R.id.googleRatingBar);

        txtRating.setText(String.valueOf(getArguments().getDouble("RATING")) + "/5");
        ratingBar.setRating((float) getArguments().getDouble("RATING"));

        try {

            JSONArray jsonReviews = new JSONArray(getArguments().getString("REVIEWS"));

            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_google_comment);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(this.getActivity());
            recyclerView.setLayoutManager(layoutManager);
            mAdapter = new CommentAdapter(jsonReviews);
            recyclerView.setAdapter(mAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }
}