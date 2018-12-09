package team25.conveniencestore.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import team25.conveniencestore.R;
import team25.conveniencestore.SqlProvider.GooglePlacesViewModel;
import team25.conveniencestore.activitys.PlaceInfoActivity;
import team25.conveniencestore.adapter.ResultStoresAdapter;
import team25.conveniencestore.models.GooglePlace;
import team25.conveniencestore.models.SharedViewModel;

public class FavoriteStoresFragment extends Fragment {
    private static String TAG = "FavoriteStoresFragment";
    public static String LIST_FAVORITES = "LIST_FAVORITES";
    private List<GooglePlace> favoriteStores;
    private SharedViewModel sharedViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result_stores, container, false);
        RecyclerView rcvResultStores = view.findViewById(R.id.rcv_result_stores);
        rcvResultStores.setHasFixedSize(true);

//        if (getArguments() != null) {
//            favoriteStores = getArguments().getParcelableArrayList(FavoriteStoresFragment.LIST_FAVORITES);
//        }

        GooglePlacesViewModel viewModel = ViewModelProviders.of(this).get(GooglePlacesViewModel.class);
        favoriteStores = viewModel.getAll();
        ResultStoresAdapter rsAdapter = new ResultStoresAdapter(favoriteStores, new ResultStoresAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                LatLng latLng = favoriteStores.get(position).getLatLng();
                sharedViewModel.setText(latLng.latitude + "," + latLng.longitude);
                /*
                Intent i = new Intent(getActivity(), PlaceInfoActivity.class);
                i.putExtra("PLACE_ID", favoriteStores.get(position).getId());
                startActivity(i);
                */
            }
        });
        rcvResultStores.setAdapter(rsAdapter);
        rcvResultStores.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvResultStores.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        sharedViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
    }
}
