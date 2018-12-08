package team25.conveniencestore.placeinfo_fragments;

import android.media.Rating;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import team25.conveniencestore.R;
import team25.conveniencestore.adapter.FirebaseCommentAdapter;

public class PlaceInfoTab3 extends Fragment {

    private static final String TAG = "PlaceInfoTab3";

    Button btnSend;
    RatingBar userRatingBar;
    RatingBar ratingBar;
    TextView ratingPoint;
    EditText userComment;

    FirebaseUser user;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_info_tab3, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("reviews");

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        // mAdapter = new FirebaseCommentAdapter();
        recyclerView.setAdapter(mAdapter);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mAdapter = new FirebaseCommentAdapter(dataSnapshot);
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        btnSend = (Button) view.findViewById(R.id.userSendButton);
        ratingBar = (RatingBar) view.findViewById(R.id.firebaseRatingBar);
        ratingPoint = (TextView) view.findViewById(R.id.firebaseRatingPoint);
        userRatingBar = (RatingBar) view.findViewById(R.id.userRatingBar);
        userComment = (EditText) view.findViewById(R.id.userComment);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("reviews").push();

                myRef.child("comment").setValue(userComment.getText().toString());
                myRef.child("point").setValue(userRatingBar.getRating());
                myRef.child("placeid").setValue(getArguments().getString("PLACE_ID"));
                myRef.child("name").setValue(user.getDisplayName());
                myRef.child("photo").setValue(user.getPhotoUrl().toString());

                userComment.setText("");
                userRatingBar.setRating(0);

                Toast.makeText(getActivity(), "Gửi đánh giá thành công!", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}