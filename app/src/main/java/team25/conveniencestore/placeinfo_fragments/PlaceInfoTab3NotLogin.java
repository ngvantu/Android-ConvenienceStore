package team25.conveniencestore.placeinfo_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

import team25.conveniencestore.R;
import team25.conveniencestore.activitys.MapsActivity;
import team25.conveniencestore.activitys.PlaceInfoActivity;

import static android.app.Activity.RESULT_OK;

public class PlaceInfoTab3NotLogin extends Fragment {

    private static final int RC_SIGN_IN = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_info_tab3_notlogin, container, false);
        Button btn = (Button) view.findViewById(R.id.place_info_login_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallLogin();
            }
        });
        return view;
    }

    void CallLogin()
    {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setLogo(R.drawable.logo_login)
                        .setAvailableProviders(providers)
                        .build(), RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(getContext(), MapsActivity.class);
                startActivity(intent);
            } else {

            }
        }
    }

}
