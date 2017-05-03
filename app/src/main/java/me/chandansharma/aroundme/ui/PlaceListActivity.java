package me.chandansharma.aroundme.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.chandansharma.aroundme.R;
import me.chandansharma.aroundme.adapter.PlaceListAdapter;
import me.chandansharma.aroundme.model.Place;
import me.chandansharma.aroundme.utils.AppController;
import me.chandansharma.aroundme.utils.GoogleApiUrl;

public class PlaceListActivity extends AppCompatActivity {

    public static final String TAG = PlaceListActivity.class.getSimpleName();

    /**
     * ArrayList to store the Near By Place List
     */
    private ArrayList<Place> mNearByPlaceArrayList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutManager;
    private PlaceListAdapter mPlaceListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);

        /**
         * get the intent and get the location Tag
         */
        String locationTag = getIntent().getStringExtra(GoogleApiUrl.LOCATION_TYPE_EXTRA_TEXT);
        String currentLocation = getPreferences(Context.MODE_PRIVATE).getString(
                GoogleApiUrl.CURRENT_LOCATION_DATA_KEY, null);

        String locationQueryStringUrl = GoogleApiUrl.BASE_URL + GoogleApiUrl.NEARBY_SEARCH_TAG + "/" +
                GoogleApiUrl.JSON_FORMAT_TAG + "?" + GoogleApiUrl.LOCATION_TAG + "=" +
                "20.609803,72.938786" + "&" + GoogleApiUrl.RADIUS_TAG + "=" +
                GoogleApiUrl.RADIUS_VALUE + "&" + GoogleApiUrl.PLACE_TYPE_TAG + "=" + locationTag +
                "&" + GoogleApiUrl.API_KEY_TAG + "=" + GoogleApiUrl.API_KEY;

        Log.d(TAG, locationQueryStringUrl);

        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(actionBar);
        String actionBarTitleText = getResources().getString(R.string.near_by_tag) +
                " " + locationTag.replace('_', ' ') + " List";
        setTitle(actionBarTitleText);
        actionBar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Method to required all details about place
        //getNearByPlaceDetails(locationQueryStringUrl);
        mNearByPlaceArrayList = getIntent().getParcelableArrayListExtra("Mydata");
        mRecyclerView = (RecyclerView) findViewById(R.id.place_list_recycler_view);

        if (mNearByPlaceArrayList.size() == 0) {
            findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            findViewById(R.id.empty_view).setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mGridLayoutManager = new GridLayoutManager(this, 1);
            mPlaceListAdapter = new PlaceListAdapter(this, mNearByPlaceArrayList);
            mRecyclerView.setLayoutManager(mGridLayoutManager);
            mRecyclerView.setAdapter(mPlaceListAdapter);
        }
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
                        mPlaceListAdapter.notifyDataSetChanged();
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
}
