package team25.conveniencestore.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import team25.conveniencestore.R;
import team25.conveniencestore.SqlProvider.FavoritePlaces;
import team25.conveniencestore.SqlProvider.FavoritePlacesViewModel;
import team25.conveniencestore.activitys.PlaceInfoActivity;
import team25.conveniencestore.adapter.FavoriteStoresAdapter;
import team25.conveniencestore.adapter.ResultStoresAdapter;
import team25.conveniencestore.models.GooglePlace;

public class FavoriteStoresFragment extends Fragment {
    private static String TAG = "FavoriteStoresFragment";

    private List<FavoritePlaces> favoriteStores;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result_stores, container, false);
        RecyclerView rcvResultStores = view.findViewById(R.id.rcv_result_stores);
        rcvResultStores.setHasFixedSize(true);

        FavoritePlacesViewModel model = ViewModelProviders.of(this).get(FavoritePlacesViewModel.class);
        favoriteStores = model.getAllWords();

        FavoriteStoresAdapter rsAdapter = new FavoriteStoresAdapter(favoriteStores);
        rcvResultStores.setAdapter(rsAdapter);
        rcvResultStores.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }
}