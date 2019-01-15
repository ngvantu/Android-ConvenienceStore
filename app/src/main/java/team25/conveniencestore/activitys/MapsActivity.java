package team25.conveniencestore.activitys;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;

import android.util.Log;

import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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
import java.util.List;

import java.util.Locale;


import team25.conveniencestore.DirectionFinder;
import team25.conveniencestore.FindPlace;
import team25.conveniencestore.PlaceNearbySearch;
import team25.conveniencestore.R;
import team25.conveniencestore.SqlProvider.GooglePlacesViewModel;
import team25.conveniencestore.adapter.InfoWindowAdapter;
import team25.conveniencestore.adapter.PlaceAutoCompleteAdapter;
import team25.conveniencestore.adapter.StoreAutoCompleteAdapter;
import team25.conveniencestore.fragments.DialogResultStores;
import team25.conveniencestore.fragments.ResultStoresFragment;
import team25.conveniencestore.interfaces.DirectionFinderListener;
import team25.conveniencestore.interfaces.FindPlaceListener;
import team25.conveniencestore.interfaces.SearchStoresListener;
import team25.conveniencestore.models.GooglePlace;
import team25.conveniencestore.models.Route;
import team25.conveniencestore.models.SharedViewModel;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        DirectionFinderListener, LocationListener {

    private DrawerLayout drawerLayout;
    private GoogleMap mMap;
    private ImageButton btnFindPath;
    private ImageButton btnDeleteMarker;
    private FloatingActionButton btnSearchNearMe;
    private FloatingActionButton btnResult, btnFeedback;
    private Button btnDeleteInputSearchStore;
    private FloatingActionButton floatingBTN, floatBtn_Result, floatBtn_FeedBack, floatBtn_Nearby;
    private Animation Move_Left, Back_Left, Move_Above, Back_Above, Move_Middle, Back_Middle,Rotate_X,Rotate_Plus;
    private AutoCompleteTextView mSearchText;
    private Marker clickingMarker;
    private Marker currentMarker;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private List<GooglePlace> resultStores = new ArrayList<>();
    private List<GooglePlace> favoriteStores = new ArrayList<>();
    private List<Marker> resultMarkers = new ArrayList<>();
    private List<Marker> favoriteMarkers = new ArrayList<>();

    private ProgressDialog progressDialog;

    private Button btnFindPlace;

    private LinearLayout linearLayoutFindPlace;
    private GoogleApiClient googleApiClient;
    private PlaceNearbySearch placeNearbySearch;

    private Location currentLocation;
    private LatLng pickingLocation = null;

    private SharedViewModel sharedViewModel;
    private DialogResultStores dialogResultStores;

    private boolean moveBack = false;

    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(8.1790665, 102.14441), new LatLng(23.393395, 114.3337595)
    );

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private LocationRequest locationRequest;
    private static final long UPDATE_INTERNAL = 5000, FASTEST_INTERNAL = 5000; // 5 seconds

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

        loadLocale();
        super.onCreate(savedInstanceState);
        Initalize();

        setNavigation();

        sharedViewModel = ViewModelProviders.of(this).get(SharedViewModel.class);
        sharedViewModel.getText().observe(this, new android.arch.lifecycle.Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Log.d("SharedViewModel", s);
                dialogResultStores.dismiss();
                String origin = null;
                if (pickingLocation != null) {
                    origin = pickingLocation.latitude + "," + pickingLocation.longitude;
                } else if (currentLocation != null){
                    origin = currentLocation.getLatitude() + "," + currentLocation.getLongitude();
                } else {
                    Toast.makeText(MapsActivity.this, "Không xác định được vị trí bắt đầu!", Toast.LENGTH_SHORT).show();
                    return;
                }

                sendRequest(origin, s);
            }
        });
    }

    private void sendRequest(String origin, String destination) {
        if (origin.isEmpty()) {
            Toast.makeText(this, "Please enter origin address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination.isEmpty()) {
            Toast.makeText(this, "Please enter destination address!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            new DirectionFinder(this, this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void searchPlacesNearMe() {
        String keyWord = mSearchText.getText().toString().trim();
        if (keyWord.isEmpty()) {
            keyWord = "Cửa hàng tiện lợi";
        }
        hideKeyboard(this.getCurrentFocus());

        try {
            if (currentLocation != null) {
                placeNearbySearch = new PlaceNearbySearch(this, currentLocation.getLatitude(), currentLocation.getLongitude(), keyWord, new SearchStoresListener() {
                    @Override
                    public void onSearchStoresStart() {
                        progressDialog = ProgressDialog.show(MapsActivity.this, "Vui lòng đợi!", "Đang tìm các cửa hàng gần bạn...!", true);
                        deletePolylinePaths();
                        resultStores.clear();
                        for (Marker marker : resultMarkers) {
                            marker.remove();
                        }
                    }

                    @Override
                    public void onSearchStoresSuccess(List<GooglePlace> results) {
                        resultStores.addAll(results);
                        showNearbyPlaces(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Tìm thấy " + resultStores.size() + " cửa hàng!", Toast.LENGTH_SHORT).show();
                        showDialogResultStores();
                    }
                });
                placeNearbySearch.execute();
            } else {
                Toast.makeText(getApplicationContext(), "Không tìm thấy GPS", Toast.LENGTH_SHORT).show();
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
        hideKeyboard(this.getCurrentFocus());

        try {
            if (pickingLocation != null) {
                placeNearbySearch = new PlaceNearbySearch(this, pickingLocation.latitude, pickingLocation.longitude, keyWord, new SearchStoresListener() {
                    @Override
                    public void onSearchStoresStart() {
                        progressDialog = ProgressDialog.show(MapsActivity.this, "Vui lòng đợi!", "Đang tìm các cửa hàng gần vị trí đã chọn...!", true);
                        resultStores.clear();
                        for (Marker marker : resultMarkers) {
                            marker.remove();
                        }
                        deletePolylinePaths();
                    }

                    @Override
                    public void onSearchStoresSuccess(List<GooglePlace> results) {
                        resultStores.addAll(results);
                        showNearbyPlaces(pickingLocation);
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Tìm thấy " + resultStores.size() + " cửa hàng!", Toast.LENGTH_SHORT).show();
                        showDialogResultStores();
                    }
                });
                placeNearbySearch.execute();
            } else {
                Toast.makeText(getApplicationContext(), "Chưa chọn địa điểm", Toast.LENGTH_SHORT).show();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void showNearbyPlaces(LatLng pickingLocation) {
        for (int i = 0; i < resultStores.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            GooglePlace googlePlace = resultStores.get(i);

            String placeName = googlePlace.getName();
            String vicinity = googlePlace.getVicinity();
            LatLng latLng = googlePlace.getLatLng();
            String placeID = googlePlace.getId();

            if (!makeMarkerIconForStore(markerOptions, placeName)) {
                resultStores.remove(i--);
                continue;
            }

            markerOptions.position(latLng);
            markerOptions.title(placeName + " : " + vicinity);
            markerOptions.snippet(placeID);

            resultMarkers.add(mMap.addMarker(markerOptions));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pickingLocation, 15f));
    }

    boolean makeMarkerIconForStore(MarkerOptions markerOptions, String storeName) {
        if (storeName.toLowerCase().contains("family")) {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.markerfamily));
        } else if (storeName.toLowerCase().contains("circle")) {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.markerk));
        } else if (storeName.toLowerCase().contains("mini")) {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.markermini));
        } else if (storeName.toLowerCase().contains("b's")) {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.markerbmart));
        } else if (storeName.toLowerCase().contains("vinmart")) {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.markervin));
        } else if (storeName.toLowerCase().contains("7-eleven")) {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker7eleven));
        } else if (storeName.toLowerCase().contains("bách hóa xanh")) {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.markerbachhoaxanh));
        } else if (storeName.toLowerCase().contains("shop")) {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.markershopgo));
        } else {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.markerunknown));
            return false;
        }
        return true;
    }

    void findPlaceByText(String input) {
        FindPlace findPlace = new FindPlace(getApplicationContext(), mMap, input, new FindPlaceListener() {
            @Override
            public void onFindPlaceStart() {
                progressDialog = ProgressDialog.show(MapsActivity.this, "Vui lòng đợi!", "Đang tìm địa điểm...!", true);
                deletePolylinePaths();
                if (pickingLocation != null) {
                    currentMarker.remove();
                }
            }

            @Override
            public void onFindPlaceSuccess(GooglePlace googlePlace) {
                if (googlePlace == null) {
                    Toast.makeText(MapsActivity.this, "Không tìm thấy địa điểm!", Toast.LENGTH_SHORT).show();
                } else {
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(googlePlace.getLatLng());
                    markerOptions.title(googlePlace.getName());
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.markerfrom));
                    currentMarker = mMap.addMarker(markerOptions);

                    pickingLocation = currentMarker.getPosition();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pickingLocation, 15f));
                }

                progressDialog.dismiss();
            }
        });
        try {
            findPlace.execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void showFavoriteStores() {
        GooglePlacesViewModel viewModel = ViewModelProviders.of(this).get(GooglePlacesViewModel.class);
        favoriteStores.clear();
        favoriteStores = viewModel.getAll();

        for (int i = 0; i < favoriteStores.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            GooglePlace googlePlace = favoriteStores.get(i);

            String placeName = googlePlace.getName();
            String vicinity = googlePlace.getVicinity();
            LatLng latLng = googlePlace.getLatLng();
            String placeID = googlePlace.getId();

            markerOptions.position(latLng);
            markerOptions.title(placeName + " : " + vicinity);
            markerOptions.snippet(placeID);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.markerstar));
            favoriteMarkers.add(mMap.addMarker(markerOptions));
        }
    }

    private void showDialogResultStores() {
        dialogResultStores = new DialogResultStores();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ResultStoresFragment.LIST_RESULTS, (ArrayList<? extends Parcelable>) resultStores);
        //bundle.putParcelableArrayList(FavoriteStoresFragment.LIST_FAVORITES, (ArrayList<? extends Parcelable>) favoriteStores);
        dialogResultStores.setArguments(bundle);
        dialogResultStores.show(getSupportFragmentManager(), "DialogResultStores");
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (clickingMarker != null && latLng != clickingMarker.getPosition()) {
                    btnFindPath.setVisibility(View.INVISIBLE);
                    btnDeleteMarker.setVisibility(View.INVISIBLE);
                    clickingMarker = null;
                }
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.d("ClickMarker", marker.getPosition().toString());

                mMap.setInfoWindowAdapter(new InfoWindowAdapter(MapsActivity.this));

                if (currentMarker != null && marker.getPosition().latitude == currentMarker.getPosition().latitude
                        && marker.getPosition().longitude == currentMarker.getPosition().longitude) {
                    btnDeleteMarker.setVisibility(View.VISIBLE);
                    btnFindPath.setVisibility(View.INVISIBLE);
                } else {
                    btnFindPath.setVisibility(View.VISIBLE);
                    btnDeleteMarker.setVisibility(View.INVISIBLE);
                }
                clickingMarker = marker;
                return false;
            }
        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (pickingLocation != null && marker.getPosition().latitude == pickingLocation.latitude && marker.getPosition().longitude == pickingLocation.longitude)
                    return;
                Intent i = new Intent(MapsActivity.this, PlaceInfoActivity.class);
                i.putExtra("PLACE_ID", marker.getSnippet());
                startActivity(i);
            }
        });
    }

    private void mappingController() {
        btnFindPlace = (Button) findViewById(R.id.btnFindPlace);

        btnFindPath = (ImageButton) findViewById(R.id.btnFindPath);
        btnDeleteMarker = (ImageButton) findViewById(R.id.btnDeleteMarker);
        btnSearchNearMe = (FloatingActionButton) findViewById(R.id.btnSearchNearMe);
        btnResult = (FloatingActionButton) findViewById(R.id.btnResult);
        btnFeedback = (FloatingActionButton) findViewById(R.id.btnFeedback);

        btnDeleteInputSearchStore = (Button) findViewById(R.id.btnDeleteInputSearchStore);
        mSearchText = (AutoCompleteTextView) findViewById(R.id.input_search);

        floatingBTN = (FloatingActionButton) findViewById(R.id.floatingBTN);
        Move_Left = AnimationUtils.loadAnimation(this, R.anim.move_bottom);
        Back_Left = AnimationUtils.loadAnimation(this, R.anim.back_bottom);
        Move_Above = AnimationUtils.loadAnimation(this, R.anim.move_above);
        Back_Above = AnimationUtils.loadAnimation(this, R.anim.back_above);
        Move_Middle = AnimationUtils.loadAnimation(this, R.anim.move_middle);
        Back_Middle = AnimationUtils.loadAnimation(this, R.anim.back_middle);
        Rotate_X = AnimationUtils.loadAnimation(this, R.anim.rotate_x);
        Rotate_Plus = AnimationUtils.loadAnimation(this, R.anim.rotate_plus);

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
                    if (pickingLocation == null) {
                        searchPlacesNearMe();
                    } else {
                        searchPlacesNearPosition();
                    }
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
                if (pickingLocation == null) {
                    searchPlacesNearMe();
                } else {
                    searchPlacesNearPosition();
                }
            }
        });

        btnFindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String origin = null;
                if (pickingLocation != null) {
                    origin = pickingLocation.latitude + "," + pickingLocation.longitude;
                } else if (currentLocation != null){
                    origin = currentLocation.getLatitude() + "," + currentLocation.getLongitude();
                } else {
                    Toast.makeText(MapsActivity.this, "Không xác định được vị trí bắt đầu!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String destination = clickingMarker.getPosition().latitude + "," + clickingMarker.getPosition().longitude;
                sendRequest(origin, destination);
            }
        });

        btnDeleteMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentMarker.remove();
                currentMarker = null;
                pickingLocation = null;
            }
        });

        btnFindPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                View view = getLayoutInflater().inflate(R.layout.dialog_findplace, null);
                builder.setView(view);
                builder.setCancelable(true);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                final AutoCompleteTextView actvFindPlace = view.findViewById(R.id.actvFindPlace);
                ImageButton imgbtnFindPlace = view.findViewById(R.id.imgbtnFindPlace);
                Button btnFind = view.findViewById(R.id.dlg_FindPlace_btnFind);
                Button btnClose = view.findViewById(R.id.dlg_FindPlace_btnClose);

                PlaceAutoCompleteAdapter placeAutoCompleteAdapter = new PlaceAutoCompleteAdapter(getApplicationContext(), googleApiClient, LAT_LNG_BOUNDS, null);
                actvFindPlace.setAdapter(placeAutoCompleteAdapter);
                actvFindPlace.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        boolean handle = false;
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            String input = actvFindPlace.getText().toString().trim();
                            if (input.isEmpty()) {
                                Toast.makeText(getApplicationContext(), "Chưa nhập địa điểm", Toast.LENGTH_SHORT).show();
                                return true;
                            }
                            hideKeyboard(v);
                            alertDialog.dismiss();
                            findPlaceByText(input);
                            handle = true;
                        }
                        return handle;
                    }
                });

                imgbtnFindPlace.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "Chọn trên bản đồ", Toast.LENGTH_SHORT).show();

                    }
                });
                btnFind.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String input = actvFindPlace.getText().toString().trim();
                        if (input.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Chưa nhập địa điểm", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        hideKeyboard(v);
                        alertDialog.dismiss();
                        findPlaceByText(input);

                        // search immediately if having search text
                        if (!mSearchText.getText().toString().trim().isEmpty()) {
                            searchPlacesNearPosition();
                        }
                    }
                });
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideKeyboard(v);
                        alertDialog.dismiss();
                    }
                });
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
                showDialogResultStores();
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

        floatingBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!moveBack) {
                    Show();
                    moveBack = !moveBack;
                } else {
                    Hide();
                    moveBack = !moveBack;
                }
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
        progressDialog = ProgressDialog.show(this, "Vui lòng đợi!",
                "Đang tìm đường đi...!", true);

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
            /*
            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(route.endAddress)
                    .position(route.endLocation)));
            */
            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(12);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }

    private void Show() {
        floatingBTN.startAnimation(Rotate_X);
        //btnFeedback.startAnimation(Move_Left);
        btnResult.startAnimation(Move_Left);
        //btnSearchNearMe.startAnimation(Move_Above);
    }

    private void Hide() {
        floatingBTN.startAnimation(Rotate_Plus);
        btnResult.startAnimation(Back_Left);
        //btnSearchNearMe.startAnimation(Back_Above);
        //btnFeedback.startAnimation(Back_Left);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        startLocationUpdates();

        if (currentLocation != null) {
            LatLng latLngCurrent = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngCurrent, 15f));
        } else {
            LatLng hcmus = new LatLng(10.762683, 106.682108);
            //mMap.addMarker(new MarkerOptions().position(hcmus).title("Khoa học tự nhiên"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hcmus, 15f));
        }
        showFavoriteStores();
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

    private void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void deletePolylinePaths() {
        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }
    }

    private void setNavigation() {

        drawerLayout =  findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        //menuItem.setChecked(true);
                        // close drawer when item is tapped

                        switch (menuItem.getItemId())
                        {
                            case R.id.tab1:

                                break;
                            case R.id.tab2:
                                Intent intent1 = new Intent(MapsActivity.this, FeedbackActivity.class);
                                startActivity(intent1);
                                break;
                            case R.id.tab3:

                                break;
                            case R.id.tab4:
                                // change language

                                showChangeLaguageDialog();
                                break;
                            case R.id.tab5:
                                initview();
                                break;
                        }
                        drawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });

    }

    private void showChangeLaguageDialog() {

        final  String[] listname = {"English"};
        AlertDialog.Builder builder =  new AlertDialog.Builder(MapsActivity.this);

        builder.setTitle("Choose your language...");
        builder.setSingleChoiceItems(listname, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which)
                {
                    case 0:
                        setlocate("en");
                        recreate();
                        break;
                }
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        // show dialog
        dialog.show();
    }

    //for nav-tab4
    private void setlocate(String en) {
        Locale locale = new Locale(en);
        locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

        //save data to shared

        SharedPreferences.Editor editor = getSharedPreferences("setting", MODE_PRIVATE).edit();
        editor.putString("My_English", en);
        editor.apply();
    }

    // Load language

    public void loadLocale()
    {
        SharedPreferences preferences = getSharedPreferences("setting", MapsActivity.MODE_PRIVATE);
        String language = preferences.getString("My_English", "");

        setlocate(language);
    }

    //for nav-tab5
    private void initview() {

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        alertDialog.setMessage("Konbini Map\n" +
                "Bản đồ cửa hàng tiện lợi \n" + "\n"+
                "* Phiên bản: 1.0.0 \n" +
                "* Tác giả: Tú Nguyễn, Nghĩa Nguyễn, Tiến Nguyễn, Mập Nguyễn, Đức Tài \n" +
                "- ĐH Khoa Học Tự Nhiên, ĐHQG Tp. Hồ Chí Minh \n" +
                "* Ứng dụng được phát triển nhằm đáp ứng như cầu tìm kiếm của hàng tiện lợi nhanh chóng hơn, hiệu quả hơn");

        alertDialog.setButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE, "Đóng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        });

        alertDialog.show();
    }
}
