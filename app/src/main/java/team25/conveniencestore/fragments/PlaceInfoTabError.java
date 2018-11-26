package team25.conveniencestore.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import team25.conveniencestore.R;

public class PlaceInfoTabError extends Fragment {

    private static final String TAG = "PlaceInfoTabError";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_info_expired, container, false);
        TextView txtStatus = (TextView) view.findViewById(R.id.placeinfo_api_expired_status);
        TextView txtMessage = (TextView) view.findViewById(R.id.placeinfo_api_expired_message);

        txtStatus.setText(getArguments().getString("ERROR_STATUS"));
        txtMessage.setText(getArguments().getString("ERROR_MESSAGE"));

        return view;
    }
}
