package com.assignment.bbvaassignment.ui;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.assignment.bbvaassignment.R;
import com.assignment.bbvaassignment.adapters.RecyclerAdapter;
import com.assignment.bbvaassignment.apis.APIRequestHandler;
import com.assignment.bbvaassignment.apis.GetAsync;
import com.assignment.bbvaassignment.constants.MyConstants;
import com.assignment.bbvaassignment.listeners.OnAsyncTaskCompleteListener;
import com.assignment.bbvaassignment.listeners.RecyclerTouchListener;
import com.assignment.bbvaassignment.models.PlacesDTO;
import com.assignment.bbvaassignment.utils.Utility;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends BaseActivity implements View.OnClickListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, Callback<Map<String,Object>>
{

    //    Key for nearby places json from google
    private final String GEOMETRY = "geometry";
    private final String LOCATION = "location";
    private final String LATITUDE = "lat";
    private final String LONGITUDE = "lng";
    private final String ICON = "icon";
    private final String SUPERMARKET_ID = "id";
    private final String NAME = "name";
    private final String FORMATTED_ADDRESS = "formatted_address";
    private final String PLACE_ID = "place_id";
    private final String REFERENCE = "reference";
    private final String VICINITY = "vicinity";
    private final String PLACE_NAME = "place_name";
    private final String TAG = "gplaces";
    private final String RESULTS = "results";
    private final String STATUS = "status";
    private final String OK = "OK";
    private final String ZERO_RESULTS = "ZERO_RESULTS";
    private final String REQUEST_DENIED = "REQUEST_DENIED";
    private final String OVER_QUERY_LIMIT = "OVER_QUERY_LIMIT";
    private final String UNKNOWN_ERROR = "UNKNOWN_ERROR";
    private final String INVALID_REQUEST = "INVALID_REQUEST";
    private final String RATING = "rating";

    private static final float MAP_ZOOM_LEVEL = 15.0f;
    private SupportMapFragment mapFragment;
    private GoogleMap mGoogleMap;
    private MapsActivity mActivity;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private boolean refreshLocation = true;
    private Location mCurLocation;
    private Map<Marker, PlacesDTO> markers;
    private ArrayList<PlacesDTO> placesListMain;
    private String query = "BBVA Compass";
    private Menu menu;
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private LinearLayout mapLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mActivity = this;

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        placesListMain = new ArrayList<>();

        if (Utility.checkPlayServices(MapsActivity.this)) {
            GoogleClientBuild();
            mGoogleApiClient.connect();
        }

        initializeViews();

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (refreshLocation) {
            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_maps, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
            break;
            case R.id.action_toggle:
            if (item.getTitle().equals(getString(R.string.action_list))) {
                item.setTitle(getString(R.string.action_map));
                //Dummy visible list
                recyclerView.setVisibility(View.VISIBLE);
                mapLayout.setVisibility(View.GONE);
            } else if (item.getTitle().equals(getString(R.string.action_map))) {
                item.setTitle(getString(R.string.action_list));
                //Dummy visible map
                recyclerView.setVisibility(View.GONE);
                mapLayout.setVisibility(View.VISIBLE);
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeViews() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        recyclerView = findViewById(R.id.recycler_view);
        mapLayout = findViewById(R.id.mapLayout);
        adapter = new RecyclerAdapter(this, placesListMain);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(MapsActivity.this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                PlacesDTO placesDTO = placesListMain.get(position);
                Intent intent = new Intent(MapsActivity.this, DetailedViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(DetailedViewActivity.EXTRA_MODEL, placesDTO);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null && refreshLocation) {
            refreshLocation = false;
            mCurLocation = location;

            if(mGoogleMap != null)
                zoomToCurrentLocation();

            //Dummy get near by places
            loadNearByPlaces(mCurLocation.getLatitude(), mCurLocation.getLongitude());

        }
    }

    private void zoomToCurrentLocation() {

        LatLng curLatlong = new LatLng(mCurLocation.getLatitude(), mCurLocation.getLongitude());
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(curLatlong, MAP_ZOOM_LEVEL);
        mGoogleMap.animateCamera(yourLocation);
    }

    /*private void visibleAllMarkers() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        Set<Marker> allEqiptMarker = mEquipmentModelMap.keySet();
        for (Marker marker : allEqiptMarker) {
            builder.include(marker.getPosition());
        }
        if (centerMarker != null)
            builder.include(centerMarker.getPosition());

        LatLngBounds bounds = builder.build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
    }*/

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onConnected(Bundle bundle) {
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdate();
        }
    }

    private void startLocationUpdate() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private synchronized void GoogleClientBuild() {
        mGoogleApiClient = new GoogleApiClient.Builder(mActivity).addApi(LocationServices.API).
                addConnectionCallbacks(this).
                addApi(AppIndex.API).
                addApi(AppIndex.API).
                addOnConnectionFailedListener(this).build();

        createLocRequest();
    }

    public void createLocRequest() {
        mLocationRequest = new LocationRequest().setInterval(10000).
                setFastestInterval(5000).
                setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).
                setSmallestDisplacement(500);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.setMyLocationEnabled(true);
//        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
        //Dummy commented due to unused
//        mGoogleMap.setOnMarkerClickListener(this);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setCompassEnabled(true);
        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                marker.hideInfoWindow();
                PlacesDTO placesDTO = markers.get(marker);
                Intent intent = new Intent(MapsActivity.this, DetailedViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(DetailedViewActivity.EXTRA_MODEL, placesDTO);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        if(mCurLocation != null)
            zoomToCurrentLocation();
    }


    private void loadNearByPlaces(double latitude, double longitude) {
        /*StringBuilder googlePlacesUrl =
                new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?");
        googlePlacesUrl.append("query="+ URLEncoder.encode(query));
        googlePlacesUrl.append("&location=").append(latitude).append(",").append(longitude);
        googlePlacesUrl.append("&radius=10000");
        googlePlacesUrl.append("&key=" + getString(R.string.google_api_key));

        GetAsync getAsync = new GetAsync(MyConstants.CONN_TIMEOUT, MyConstants.SOCKET_TIMEOUT, new OnAsyncTaskCompleteListener() {
            @Override
            public void onComplete(String result) {
                Utility.hideProgressDialog();
                if (result == null || result.isEmpty()) {
                    Utility.showAlert(mActivity, null, getString(R.string.noResponseGoogle));
                    return;
                }

                try {
                    JSONObject object = new JSONObject(result);
                    parseLocationResult(object);
                } catch (JSONException e) {
                    Utility.showAlert(mActivity, null, getString(R.string.invalidResponseGoogle));
                }

            }
        });
        //Utility.showProgressDialog(mActivity);
        //getAsync.execute(googlePlacesUrl.toString());*/


        HashMap<String, String> paramsMap= new HashMap<String,String>();
        paramsMap.put("query",String.valueOf(URLEncoder.encode(query)));
        paramsMap.put("location", latitude+","+longitude);
        paramsMap.put("radius",String.valueOf(10000));
        paramsMap.put("key",getString(R.string.google_api_key));

        Call<Map<String,Object>> call = APIRequestHandler.getInstance().getPlaces(paramsMap);
        call.enqueue(MapsActivity.this);


    }



    private void parseLocationResult(JSONObject result) {

//        String id, place_id, placeName = null, reference, icon, vicinity = null;
        double latitude, longitude;

        try {
            Gson gson = new Gson();
            List<PlacesDTO> placeList = new ArrayList<>();
            JSONArray jsonArray = result.getJSONArray("results");

            if (result.getString(STATUS).equalsIgnoreCase(OK)) {

                mGoogleMap.clear();

                markers = new HashMap<>();

                for (int i = 0; i < jsonArray.length(); i++) {

                    PlacesDTO dto = gson.fromJson(jsonArray.getJSONObject(i).toString(), PlacesDTO.class);
                    JSONObject place = jsonArray.getJSONObject(i);
                    latitude = place.getJSONObject(GEOMETRY).getJSONObject(LOCATION)
                            .getDouble(LATITUDE);
                    longitude = place.getJSONObject(GEOMETRY).getJSONObject(LOCATION)
                            .getDouble(LONGITUDE);

                    dto.setLatitude(latitude);
                    dto.setLongitude(longitude);
                    dto.setCurrentLatitude(mCurLocation.getLatitude());
                    dto.setCurrentLongitude(mCurLocation.getLongitude());
                    if (!place.isNull(PLACE_ID)) {
                        dto.setPlace_ID(place.getString(PLACE_ID));
                    }
                    if (!place.isNull(RATING))
                        dto.setPlaceRating(place.getString(RATING));

//                    id = place.getString(SUPERMARKET_ID);

                    dto.setPlace_ID(place.getString(PLACE_ID));

                    if (!place.isNull(NAME)) {
                        dto.setName(place.getString(NAME));
                    }

                    if (!place.isNull(VICINITY)) {
                        dto.setVicinity(place.getString(VICINITY));
                    }

                    if (!place.isNull(FORMATTED_ADDRESS)) {
                        dto.setFormatted_address(place.getString(FORMATTED_ADDRESS));
                    }
//                    reference = place.getString(REFERENCE);
//                    icon = place.getString(ICON);

                    placeList.add(dto);

                    MarkerOptions markerOptions = new MarkerOptions();
                    LatLng latLng = new LatLng(latitude, longitude);
                    markerOptions.position(latLng);
                    markerOptions.title(dto.getName() + " : Rating (" + dto.getPlaceRating()+")");
                    markerOptions.snippet("Address : "+dto.getFormatted_address());
                    markerOptions.icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.ic_bank_marker));
                    markers.put(mGoogleMap.addMarker(markerOptions), dto);
                }

                placesListMain.clear();
                placesListMain.addAll(placeList);

                //Dummy commented due to not using
                adapter.notifyDataSetChanged();

                // mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (Marker marker : markers.keySet()) {
                    builder.include(marker.getPosition());
                }
                LatLngBounds bounds = builder.build();

                int padding = 50; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                mGoogleMap.animateCamera(cu);

                Toast.makeText(mActivity, jsonArray.length() + " " + query + " " + getString(R.string.nearby_txtfound),
                        Toast.LENGTH_LONG).show();
            } else if (result.getString(STATUS).equalsIgnoreCase(ZERO_RESULTS)) {
                Toast.makeText(mActivity, getString(R.string.nearby_txtno) + " " + query + " " + getString(R.string.nearby_txtfoundnear),
                        Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            Utility.showAlert(mActivity, null, getString(R.string.invalidResponseGoogle));
            // e.printStackTrace();
            // Log.e(TAG, "parseLocationResult: Error=" + e.getMessage());
            placesListMain.clear();
            //Dummy commented due to not using
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResponse(Call<Map<String,Object>> call, Response<Map<String,Object>> response) {
        if(response!=null){
            Map<String,Object> response2 = response.body();
            try {
                JSONObject jsonObject = new JSONObject(response2);
                parseLocationResult(jsonObject);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(Call<Map<String,Object>> call, Throwable t) {
            t.printStackTrace();
    }
}
