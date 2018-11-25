package team25.conveniencestore;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import team25.conveniencestore.adapter.ResultStoresAdapter;
import team25.conveniencestore.adapter.ResultStoresAdapter.OnItemClickListener;
import team25.conveniencestore.models.DirectionFinder;
import team25.conveniencestore.models.DirectionFinderListener;
import team25.conveniencestore.models.GooglePlace;
import team25.conveniencestore.models.Route;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        DirectionFinderListener, LocationListener {

    private GoogleMap mMap;
    private Button btnFindPath;
    private Button btnSearch, btnSearchNearMe;
    private Button btnResult, btnFeedback;
    private Button btnDeleteInputSearchStore;
    private AutoCompleteTextView mSearchText;
    private AutoCompleteTextView etDestination;
    private Marker marker;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private List<GooglePlace> resultStores = new ArrayList<>();
    private ProgressDialog progressDialog;

    private Button btnFindPlace;
    private Button btnDeleteInputSearchPlace;

    private LinearLayout linearLayoutFindPlace;
    private GoogleApiClient googleApiClient;
    private PlaceNearbySearch placeNearbySearch;

    private Location currentLocation;
    private LatLng pickingLocation = null;

    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(8.1790665, 102.14441), new LatLng(23.393395, 114.3337595)
    );

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private LocationRequest locationRequest;
    private static final long UPDATE_INTERNAL = 5000, FASTEST_INTERNAL = 5000; // 5 seconds

    /**
     * permissions request code
     */
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;

    /**
     * Permissions that need to be explicitly requested from end user.
     */
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
                Initalize();
                break;
        }
    }

    protected void Initalize() {
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        onServicesReady();

        mappingController();
        settingController();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();
    }

    private void sendRequest() {
        String origin = mSearchText.getText().toString();
        String destination = etDestination.getText().toString();
        if (origin.isEmpty()) {
            Toast.makeText(this, "Please enter origin address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination.isEmpty()) {
            Toast.makeText(this, "Please enter destination address!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void searchPlacesNearMe() {
        String keyWord = mSearchText.getText().toString().trim();
        if (keyWord.isEmpty()) {
            keyWord = "Cửa hàng tiện lợi";
        }
        try {
            if (currentLocation != null) {
                placeNearbySearch = new PlaceNearbySearch(mMap, currentLocation.getLatitude(), currentLocation.getLongitude(), keyWord, resultStores);
                placeNearbySearch.execute();
            } else {
                Toast.makeText(getApplicationContext(), "Chưa tìm thấy GPS", Toast.LENGTH_SHORT).show();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void searchPlacesNearPosition() {
        String keyWord = mSearchText.getText().toString().trim();
        if (keyWord.isEmpty()) {
            keyWord = "Cửa hàng tiện lợi";
        }

        try {
            if (pickingLocation != null) {
                placeNearbySearch = new PlaceNearbySearch(mMap, pickingLocation.latitude, pickingLocation.longitude, keyWord, resultStores);
                placeNearbySearch.execute();
            } else {
                Toast.makeText(getApplicationContext(), "Chưa chọn địa điểm", Toast.LENGTH_SHORT).show();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng hcmus = new LatLng(10.762683, 106.682108);
        //mMap.addMarker(new MarkerOptions().position(hcmus).title("Khoa học tự nhiên"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hcmus, 15f));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent i = new Intent(MapsActivity.this, PlaceInfoActivity.class);
                i.putExtra("PLACE_ID", marker.getSnippet());
                startActivity(i);
            }
        });
    }

    private void mappingController() {
        btnFindPlace = (Button) findViewById(R.id.btnFindPlace);
        linearLayoutFindPlace = (LinearLayout) findViewById(R.id.relLayout2);

        btnFindPath = (Button) findViewById(R.id.btnFindPath);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearchNearMe = (Button) findViewById(R.id.btnSearchNearMe);
        btnResult = (Button) findViewById(R.id.btnResult);
        btnFeedback = (Button) findViewById(R.id.btnFeedback);

        btnDeleteInputSearchStore = (Button) findViewById(R.id.btnDeleteInputSearchStore);
        mSearchText = (AutoCompleteTextView) findViewById(R.id.input_search);
        btnDeleteInputSearchPlace = (Button) findViewById(R.id.btnDeleteInputSearchPlace);
        etDestination = (AutoCompleteTextView) findViewById(R.id.etDestination);
    }

    private void settingController() {
        StoreAutoCompleteAdapter storeAutoCompleteAdapter = new StoreAutoCompleteAdapter(this);
        mSearchText.setThreshold(1);
        mSearchText.setAdapter(storeAutoCompleteAdapter);
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handle = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchPlacesNearMe();
                    handle = true;
                }
                return handle;
            }
        });
        // Show all suggestions on first click
        mSearchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchText.setText(" ");
            }
        });
        // Reset if dismiss and no entered text
        mSearchText.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
            @Override
            public void onDismiss() {
                String input = mSearchText.getText().toString().trim();
                if (input.isEmpty()) {
                    mSearchText.setText("");
                }
            }
        });
        mSearchText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchPlacesNearPosition();
            }
        });

        final PlaceAutoCompleteAdapter placeAutoCompleteAdapter = new PlaceAutoCompleteAdapter(this, googleApiClient, LAT_LNG_BOUNDS, null);
        etDestination.setAdapter(placeAutoCompleteAdapter);
        etDestination.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FindPlace findPlace = new FindPlace(mMap, etDestination.getText().toString());
                try {
                    findPlace.execute();
                    pickingLocation = mMap.getCameraPosition().target;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        btnFindPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //btnFindPlace.setVisibility(View.GONE);
                //linearLayoutFindPlace.setVisibility(View.VISIBLE);
                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                View view = getLayoutInflater().inflate(R.layout.dialog_findplace, null);
                builder.setView(view);
                builder.setCancelable(true);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                final AutoCompleteTextView actvFindPlace = (AutoCompleteTextView) view.findViewById(R.id.actvFindPlace);
                ImageButton imgbtnFindPlace = (ImageButton) view.findViewById(R.id.imgbtnFindPlace);
                Button btnFind = (Button) view.findViewById(R.id.dlg_FindPlace_btnFind);
                Button btnClose = (Button) view.findViewById(R.id.dlg_FindPlace_btnClose);

                actvFindPlace.setAdapter(placeAutoCompleteAdapter);

                imgbtnFindPlace.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "Chọn trên bản đồ", Toast.LENGTH_SHORT).show();
                    }
                });
                btnFind.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String input = actvFindPlace.getText().toString();
                        if (input.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Chưa nhập địa điểm", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        FindPlace findPlace = new FindPlace(mMap, input);
                        try {
                            findPlace.execute();
                            pickingLocation = mMap.getCameraPosition().target;
                            alertDialog.dismiss();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                });
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });

        btnFindPath.setEnabled(false);
        btnFindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPlacesNearPosition();
            }
        });

        btnSearchNearMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPlacesNearMe();
            }
        });

        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MapsActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_result_stores, null);
                RecyclerView rcvResultStores = (RecyclerView) mView.findViewById(R.id.rcv_result_stores);
                rcvResultStores.setHasFixedSize(true);

                ResultStoresAdapter rsAdapter = new ResultStoresAdapter(resultStores, new OnItemClickListener() {
                    @Override
                    public void OnItemClick(int position) {
                        Intent i = new Intent(MapsActivity.this, PlaceInfoActivity.class);
                        i.putExtra("PLACE_ID", resultStores.get(position).getId());
                        startActivity(i);
                    }
                });
                rcvResultStores.setAdapter(rsAdapter);
                rcvResultStores.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                mBuilder.setView(mView);
                mBuilder.setCancelable(true);
                AlertDialog alertDialog = mBuilder.create();
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();
            }
        });

        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MapsActivity.this, FeedbackActivity.class);
                startActivity(i);
            }
        });

        btnDeleteInputSearchStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchText.setText("");
            }
        });

        btnDeleteInputSearchPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etDestination.setText("");
                pickingLocation = null;
                linearLayoutFindPlace.setVisibility(View.GONE);
                btnFindPlace.setVisibility(View.VISIBLE);
            }
        });
    }

    private void onServicesReady() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .enableAutoManage(this, this)
                    .build();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!checkPlayServices()) {
            Toast.makeText(this, "You need to install Google Play Services!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (googleApiClient != null && googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        startLocationUpdates();
    }

    private void startLocationUpdates() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERNAL);
        locationRequest.setFastestInterval(FASTEST_INTERNAL);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You need enable permissions to display location", Toast.LENGTH_SHORT).show();
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }
    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
    }

}
