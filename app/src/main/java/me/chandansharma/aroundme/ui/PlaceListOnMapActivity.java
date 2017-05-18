package me.chandansharma.aroundme.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.chandansharma.aroundme.R;
import me.chandansharma.aroundme.model.Place;
import me.chandansharma.aroundme.utils.AppController;
import me.chandansharma.aroundme.utils.GoogleApiUrl;

public class PlaceListOnMapActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {

    public static final String TAG = PlaceListOnMapActivity.class.getSimpleName();
    /**
     * ArrayList to store the Near By Place List
     */
    private ArrayList<Place> mNearByPlaceArrayList = new ArrayList<>();

    private GoogleMap mGoogleMap;
    private boolean mMapReady = false;
    private String mLocationTag;
    private String mLocationName;
    private String mLocationQueryStringUrl;
    private MapFragment mMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list_on_map);

        /**
         * Get the reference of the map fragment
         */
        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);

        /**
         * get the intent and get the location Tag
         */
        mLocationTag = getIntent().getStringExtra(GoogleApiUrl.LOCATION_TYPE_EXTRA_TEXT);
        mLocationName = getIntent().getStringExtra(GoogleApiUrl.LOCATION_NAME_EXTRA_TEXT);

        String currentLocation = getSharedPreferences(
                GoogleApiUrl.CURRENT_LOCATION_SHARED_PREFERENCE_KEY, 0)
                .getString(GoogleApiUrl.CURRENT_LOCATION_DATA_KEY, null);

        mLocationQueryStringUrl = GoogleApiUrl.BASE_URL + GoogleApiUrl.NEARBY_SEARCH_TAG + "/" +
                GoogleApiUrl.JSON_FORMAT_TAG + "?" + GoogleApiUrl.LOCATION_TAG + "=" +
                currentLocation + "&" + GoogleApiUrl.RADIUS_TAG + "=" +
                GoogleApiUrl.RADIUS_VALUE + "&" + GoogleApiUrl.PLACE_TYPE_TAG + "=" + mLocationTag +
                "&" + GoogleApiUrl.API_KEY_TAG + "=" + GoogleApiUrl.API_KEY;

        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(actionBar);
        String actionBarTitleText = getResources().getString(R.string.near_by_tag) +
                " " + mLocationName + " " + getString(R.string.list_tag);
        setTitle(actionBarTitleText);
        actionBar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((TextView) findViewById(R.id.place_list_placeholder_text_view))
                .setText(getResources().getString(R.string.near_by_tag) + " " + mLocationName +
                        " " + getString(R.string.list_tag));

        findViewById(R.id.place_list_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent placeDetailTransferIntent = new Intent(PlaceListOnMapActivity.this, PlaceListActivity.class);
                placeDetailTransferIntent.putParcelableArrayListExtra(
                        GoogleApiUrl.ALL_NEARBY_LOCATION_KEY, mNearByPlaceArrayList);
                placeDetailTransferIntent.putExtra(GoogleApiUrl.LOCATION_TYPE_EXTRA_TEXT, mLocationTag);
                placeDetailTransferIntent.putExtra(GoogleApiUrl.LOCATION_NAME_EXTRA_TEXT, mLocationName);
                startActivity(placeDetailTransferIntent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_in);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mMapReady = true;

        /**
         * Helper Method to put marker on Google map
         */
        getLocationOnGoogleMap(mLocationQueryStringUrl);
    }

    private void getLocationOnGoogleMap(String locationQueryStringUrl) {
        //Tag to cancel request
        String jsonArrayTag = "jsonArrayTag";
        JsonObjectRequest placeJsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                locationQueryStringUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray rootJsonArray = response.getJSONArray("results");
                            if (rootJsonArray.length() == 0)
                                ((TextView) findViewById(R.id.place_list_placeholder_text_view))
                                        .setText(getResources().getString(R.string.no_near_by_tag)
                                                + " " + mLocationName + " " + getString(R.string.found));
                            else {
                                for (int i = 0; i < rootJsonArray.length(); i++) {
                                    JSONObject singlePlaceJsonObject = (JSONObject) rootJsonArray.get(i);

                                    String currentPlaceId = singlePlaceJsonObject.getString("place_id");
                                    Double currentPlaceLatitude = singlePlaceJsonObject
                                            .getJSONObject("geometry").getJSONObject("location")
                                            .getDouble("lat");
                                    Double currentPlaceLongitude = singlePlaceJsonObject
                                            .getJSONObject("geometry").getJSONObject("location")
                                            .getDouble("lng");
                                    String currentPlaceName = singlePlaceJsonObject.getString("name");
                                    String currentPlaceOpeningHourStatus = singlePlaceJsonObject
                                            .has("opening_hours") ? singlePlaceJsonObject
                                            .getJSONObject("opening_hours")
                                            .getString("open_now") : "Status Not Available";
                                    Double currentPlaceRating = singlePlaceJsonObject.has("rating") ?
                                            singlePlaceJsonObject.getDouble("rating") : 0;
                                    String currentPlaceAddress = singlePlaceJsonObject.has("vicinity") ?
                                            singlePlaceJsonObject.getString("vicinity") :
                                            "Address Not Available";
                                    Place singlePlaceDetail = new Place(
                                            currentPlaceId,
                                            currentPlaceLatitude,
                                            currentPlaceLongitude,
                                            currentPlaceName,
                                            currentPlaceOpeningHourStatus,
                                            currentPlaceRating,
                                            currentPlaceAddress);
                                    mNearByPlaceArrayList.add(singlePlaceDetail);
                                }
                                if (mMapReady) {
                                    //Set the camera position
                                    String currentLocationString = getSharedPreferences(
                                            GoogleApiUrl.CURRENT_LOCATION_SHARED_PREFERENCE_KEY, 0)
                                            .getString(GoogleApiUrl.CURRENT_LOCATION_DATA_KEY, null);
                                    String currentPlace[] = currentLocationString.split(",");
                                    LatLng currentLocation = new LatLng(Double.valueOf(currentPlace[0])
                                            , Double.valueOf(currentPlace[1]));
                                    CameraPosition cameraPosition = CameraPosition.builder()
                                            .target(currentLocation)
                                            .zoom(15)
                                            .bearing(0)
                                            .tilt(0)
                                            .build();
                                    mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                                            1500, null);
                                    /**
                                     *  Set the marker on Google Map
                                     */
                                    for (int i = 0; i < mNearByPlaceArrayList.size(); i++) {
                                        Double currentLatitude = mNearByPlaceArrayList.get(i).getPlaceLatitude();
                                        Double currentLongitude = mNearByPlaceArrayList.get(i).getPlaceLongitude();
                                        LatLng currentLatLng = new LatLng(currentLatitude, currentLongitude);
                                        mGoogleMap.addMarker(new MarkerOptions()
                                                .position(currentLatLng)
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_on_map)));
                                    }

                                    mGoogleMap.addMarker(new MarkerOptions()
                                            .position(currentLocation)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_current_location)));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.getMessage());
                    }
                });
        //Adding request to request queue
        AppController.getInstance().addToRequestQueue(placeJsonObjectRequest, jsonArrayTag);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(this, marker.getTitle(), Toast.LENGTH_SHORT).show();
        return false;
    }
}
