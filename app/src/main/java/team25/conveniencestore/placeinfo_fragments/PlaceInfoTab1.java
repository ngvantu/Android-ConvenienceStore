package team25.conveniencestore.placeinfo_fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import team25.conveniencestore.R;
import team25.conveniencestore.SqlProvider.GooglePlacesViewModel;
import team25.conveniencestore.models.GooglePlace;

public class PlaceInfoTab1 extends Fragment {

    private static final String TAG = "PlaceInfoTab1";
    TextView txtName;
    TextView txtAddr;
    TextView txtPhone;
    Button btnLike;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_info_tab1, container, false);
        txtName = view.findViewById(R.id.store_name);
        txtAddr = view.findViewById(R.id.placeinfo_address);
        txtPhone = view.findViewById(R.id.placeinfo_phone);
        btnLike = view.findViewById(R.id.btnLike);

        txtName.setText(getArguments().getString("STORE_NAME"));
        txtAddr.setText(getArguments().getString("STORE_ADDRESS"));
        txtPhone.setText(getArguments().getString("STORE_PHONE"));

        if(txtPhone.getText().toString().equals("")) {
            txtPhone.setText("(No information)");
        }
        else {
            TextView phone = (TextView) view.findViewById(R.id.placeinfo_icon_phone);
            phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phone = "tel:" + txtPhone.getText();

                    Intent callIntel = new Intent(Intent.ACTION_DIAL);
                    callIntel.setData(Uri.parse(phone));
                    startActivity(callIntel);
                }
            });
        }

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String placeID = getArguments().getString("PLACE_ID");
                String name = getArguments().getString("STORE_NAME");
                String address = getArguments().getString("STORE_ADDRESS");
                double lat = getArguments().getDouble("LAT");
                double lng = getArguments().getDouble("LNG");
                double rating = getArguments().getDouble("RATING");
                GooglePlace g = new GooglePlace(placeID,name,address,new LatLng(lat,lng),rating);
                GooglePlacesViewModel viewModel = ViewModelProviders.of(getActivity()).get(GooglePlacesViewModel.class);
                viewModel.insertPlace(g);
                Toast.makeText(getActivity(), "Đã lưu vào danh sách yêu thích!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}