package team25.conveniencestore.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import team25.conveniencestore.R;

public class Tab1 extends Fragment {
    View mRootView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_tab1, container,false);

        editHome();
        return mRootView;
    }

    private void editHome() {
       /* mRootView.findViewById(R.id.home_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Địa chỉ nhà 1",Toast.LENGTH_SHORT).show();
            }
        });

        mRootView.findViewById(R.id.home_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Địa chỉ nhà 2",Toast.LENGTH_SHORT).show();
            }
        });
        */



    }
}
