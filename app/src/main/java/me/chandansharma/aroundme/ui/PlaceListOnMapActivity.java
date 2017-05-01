package me.chandansharma.aroundme.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.chandansharma.aroundme.R;
import me.chandansharma.aroundme.model.Place;
import me.chandansharma.aroundme.utils.AppController;
import me.chandansharma.aroundme.utils.GoogleApiUrl;

public class PlaceListOnMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String TAG = PlaceListOnMapActivity.class.getSimpleName();
    /**
     * ArrayList to store the Near By Place List
     */
    private ArrayList<Place> mNearByPlaceArrayList = new ArrayList<>();

    private GoogleMap mGoogleMap;
    boolean mMapReady = false;
    String mLocationTag;
    String mLocationQueryStringUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list_on_map);

        //get the reference of the map fragment
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /**
         * get the intent and get the location Tag
         */
        String locationTag = getIntent().getStringExtra(GoogleApiUrl.LOCATION_TYPE_EXTRA_TEXT);
        String mCurrentLocation = getPreferences(Context.MODE_PRIVATE).getString(
                GoogleApiUrl.CURRENT_LOCATION_DATA_KEY, null);

        mLocationQueryStringUrl = GoogleApiUrl.BASE_URL + GoogleApiUrl.NEARBY_SEARCH_TAG + "/" +
                GoogleApiUrl.JSON_FORMAT_TAG + "?" + GoogleApiUrl.LOCATION_TAG + "=" +
                "20.609803,72.938786" + "&" + GoogleApiUrl.RADIUS_TAG + "=" +
                GoogleApiUrl.RADIUS_VALUE + "&" + GoogleApiUrl.PLACE_TYPE_TAG + "=" + locationTag +
                "&" + GoogleApiUrl.API_KEY_TAG + "=" + GoogleApiUrl.API_KEY;

        Log.d(TAG, mLocationQueryStringUrl);
        //Method to required all details about place
    }

    private void getNearByPlaceDetails(String locationQueryStringUrl) {
        //Tag to cancel request
        String jsonArrayTag = "jsonArrayTag";
        JsonObjectRequest placeJsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                locationQueryStringUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray rootJsonArray = response.getJSONArray("results");
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
                            //to check if the next page token is available or not for more places
                            if (response.has("next_page_token")) {
                                String nextPageToken = response.getString("next_page_token");
                                String locationQueryStringUrl = GoogleApiUrl.BASE_URL +
                                        GoogleApiUrl.NEARBY_SEARCH_TAG + "/" +
                                        GoogleApiUrl.JSON_FORMAT_TAG + "?" +
                                        GoogleApiUrl.NEXT_PAGE_TOKEN_TAG + "=" + nextPageToken +
                                        "&" + GoogleApiUrl.API_KEY_TAG + "=" + GoogleApiUrl.API_KEY;
                                Log.d(TAG, locationQueryStringUrl);
                                getNearByPlaceDetails(locationQueryStringUrl);
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

        //retrying after timeout
        //Adding request to request queue
        AppController.getInstance().addToRequestQueue(placeJsonObjectRequest, jsonArrayTag);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mMapReady = true;

        //Set the camera position
        LatLng currentLocation = new LatLng(20.609803, 72.938786);
        CameraPosition cameraPosition = CameraPosition.builder()
                .target(currentLocation)
                .zoom(14)
                .bearing(0)
                .tilt(0)
                .build();
        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        mLocationQueryStringUrl = GoogleApiUrl.BASE_URL + GoogleApiUrl.NEARBY_SEARCH_TAG + "/" +
                GoogleApiUrl.JSON_FORMAT_TAG + "?" + GoogleApiUrl.LOCATION_TAG + "=" +
                "20.609803,72.938786" + "&" + GoogleApiUrl.RADIUS_TAG + "=" +
                GoogleApiUrl.RADIUS_VALUE + "&" + GoogleApiUrl.PLACE_TYPE_TAG + "=" + mLocationTag +
                "&" + GoogleApiUrl.API_KEY_TAG + "=" + GoogleApiUrl.API_KEY;

        //Tag to cancel request
        String jsonArrayTag = "jsonArrayTag";
        JsonObjectRequest placeJsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                mLocationQueryStringUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray rootJsonArray = response.getJSONArray("results");
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
                            //to check if the next page token is available or not for more places
                            if (response.has("next_page_token")) {
                                String nextPageToken = response.getString("next_page_token");
                                String locationQueryStringUrl = GoogleApiUrl.BASE_URL +
                                        GoogleApiUrl.NEARBY_SEARCH_TAG + "/" +
                                        GoogleApiUrl.JSON_FORMAT_TAG + "?" +
                                        GoogleApiUrl.NEXT_PAGE_TOKEN_TAG + "=" + nextPageToken +
                                        "&" + GoogleApiUrl.API_KEY_TAG + "=" + GoogleApiUrl.API_KEY;
                                Log.d(TAG, locationQueryStringUrl);
                                getNearByPlaceDetails(locationQueryStringUrl);
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

        //retrying after timeout
        //Adding request to request queue
        AppController.getInstance().addToRequestQueue(placeJsonObjectRequest, jsonArrayTag);

        //Set the marker on Google Map
        for (int i = 0; i < 1; i++) {
            Double currentLatitude = mNearByPlaceArrayList.get(i).getPlaceLatitude();
            Double currentLongitude = mNearByPlaceArrayList.get(i).getPlaceLongitude();
            LatLng currentLatLng = new LatLng(currentLatitude, currentLongitude);
            mGoogleMap.addMarker(new MarkerOptions()
                    .position(currentLatLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
        }

    }
}
