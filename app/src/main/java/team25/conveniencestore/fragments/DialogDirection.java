package team25.conveniencestore.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import team25.conveniencestore.R;
import team25.conveniencestore.adapter.DirectionAdapter;
import team25.conveniencestore.models.Step;

public class DialogDirection extends DialogFragment {

    private static String TAG = "DialogDirection";
    public static String LIST_STEPS = "LIST_STEPS";
    private List<Step> steps;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_direction, container, false);

        RecyclerView rcvDirection = view.findViewById(R.id.rcv_direction);
        rcvDirection.setHasFixedSize(true);

        if (getArguments() != null) {
            steps = getArguments().getParcelableArrayList(DialogDirection.LIST_STEPS);
        }
        DirectionAdapter directionAdapter = new DirectionAdapter(steps);
        rcvDirection.setAdapter(directionAdapter);
        rcvDirection.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvDirection.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        return view;
    }
}
