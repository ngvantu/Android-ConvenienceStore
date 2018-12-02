package team25.conveniencestore.activitys;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;
import team25.conveniencestore.R;
import team25.conveniencestore.SqlProvider.GooglePlacesRepository;
import team25.conveniencestore.SqlProvider.GooglePlacesViewModel;
import team25.conveniencestore.models.GooglePlace;

public class LoadingActivity extends AppCompatActivity {

    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;


    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    protected void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<String>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        // exit the app if one permission is not granted
                        Toast.makeText(this, "Required permission '" + permissions[index]
                                + "' not granted, exiting", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                }
                // all permissions were granted
                Intent i = new Intent(LoadingActivity.this, MapsActivity.class);
                i.putParcelableArrayListExtra("favoriteList", (ArrayList<? extends Parcelable>) listFavorite);
                finish();
                startActivity(i);
                break;
        }
    }

    private GooglePlacesViewModel mViewModel;
    private List<GooglePlace> listFavorite = new ArrayList<>();
    private GooglePlacesRepository favoritePlaces;
    private static final int SPASH_TIME_OUT = 4000;
    private RingProgressBar ringProgressBar;
    private Handler myHandler;
    int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        ringProgressBar = findViewById(R.id.ringProgressBar);
        setRingProgress();
        favoritePlaces = new GooglePlacesRepository(this.getApplication());

        //Test

//        Log.d("chatluong", listFavorite.toString());

//        favoritePlaces.getALlPlace().observe(this, new Observer<List<FavoritePlaces>>() {
//            @Override
//            public void onChanged(@Nullable final List<FavoritePlaces> places) {
//                // Update the cached copy of the words in the adapter.
//                adapter.setWords(words);
//            }
//        });
    }

    @SuppressLint("HandlerLeak")
    private void setRingProgress() {
        ringProgressBar.setOnProgressListener(new RingProgressBar.OnProgressListener() {
            @Override
            public void progressToComplete() {
                checkPermissions();
            }
        });

        myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    if (progress < 100) {
                        progress++;
                        ringProgressBar.setProgress(progress);
                    }
                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    try {
                        Thread.sleep(SPASH_TIME_OUT/100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    myHandler.sendEmptyMessage(0);
                }
            }
        }).start();
    }
}
