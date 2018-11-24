package team25.conveniencestore.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import team25.conveniencestore.R;
import team25.conveniencestore.adapter.CommentAdapter;

public class PlaceInfoTab2 extends Fragment {

    private static final String TAG = "PlaceInfoTab2";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_info_tab2, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_google_comment);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        List<String> input = new ArrayList<>();
        for(int i = 0; i < 20; i++) {
            input.add("Test " + i);
        }
        mAdapter = new CommentAdapter(input);
        recyclerView.setAdapter(mAdapter);
        return view;
    }
}