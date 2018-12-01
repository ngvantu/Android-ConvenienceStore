package team25.conveniencestore.placeinfo_fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import team25.conveniencestore.R;

public class PlaceInfoTab1 extends Fragment {

    private static final String TAG = "PlaceInfoTab1";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_info_tab1, container, false);
        TextView txtName = (TextView) view.findViewById(R.id.store_name);
        TextView txtAddr = (TextView) view.findViewById(R.id.placeinfo_address);
        TextView txtPhone = (TextView) view.findViewById(R.id.placeinfo_phone);

        txtName.setText(getArguments().getString("STORE_NAME"));
        txtAddr.setText(getArguments().getString("STORE_ADDRESS"));
        txtPhone.setText(getArguments().getString("STORE_PHONE"));

        return view;
    }
}