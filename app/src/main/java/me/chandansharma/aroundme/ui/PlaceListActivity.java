package me.chandansharma.aroundme.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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
                GoogleApiUrl.CURRENT_LOCATION_DATA_KEY,null);

        String locationQueryStringUrl = GoogleApiUrl.BASE_URL + GoogleApiUrl.NEARBY_SEARCH_TAG + "/" +
                GoogleApiUrl.JSON_FORMAT_TAG + "?" + GoogleApiUrl.LOCATION_TAG + "=" +
                currentLocation + "&" + GoogleApiUrl.RADIUS_TAG + "=" +
                GoogleApiUrl.RADIUS_VALUE + "&" + GoogleApiUrl.PLACE_TYPE_TAG + "=" + locationTag +
                "&" + GoogleApiUrl.API_KEY_TAG + "=" + GoogleApiUrl.API_KEY;

        Log.d(TAG, locationQueryStringUrl);
        //Method to required all details about place
        getNearByPlaceDetails(locationQueryStringUrl);

        mRecyclerView = (RecyclerView) findViewById(R.id.place_list_recycler_view);
        mGridLayoutManager = new GridLayoutManager(this,1);
        mPlaceListAdapter = new PlaceListAdapter(this, mNearByPlaceArrayList);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(mPlaceListAdapter);
    }

    private void getNearByPlaceDetails(String locationQueryStringUrl) {
        //Tag to cancel request
        String jsonArrayTag = "jsonArrayTag";
        JsonObjectRequest placeJsonObject = new JsonObjectRequest(Request.Method.GET,
                locationQueryStringUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //to check if the next page token is available or not for more places
                        String nextPageToken = "";
                        if (response.has("next_page_token")) {
                            try {
                                nextPageToken = response.getString("next_page_token");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            JSONArray rootJsonArray = response.getJSONArray("results");
                            for (int i = 0; i < rootJsonArray.length(); i++) {
                                JSONObject singlePlaceJsonObject = (JSONObject) rootJsonArray.get(i);
                                Place singlePlaceDetail = new Place(
                                        singlePlaceJsonObject.getString("place_id"),
                                        singlePlaceJsonObject.getJSONObject("geometry")
                                                .getJSONObject("location").getDouble("lat"),
                                        singlePlaceJsonObject.getJSONObject("geometry")
                                                .getJSONObject("location").getDouble("lng"),
                                        singlePlaceJsonObject.getString("name"),
                                        singlePlaceJsonObject.getJSONObject("opening_hours")
                                                .getBoolean("open_now"),
                                        singlePlaceJsonObject.getDouble("rating"),
                                        singlePlaceJsonObject.getString("vicinity"),
                                        nextPageToken);
                                mNearByPlaceArrayList.add(singlePlaceDetail);
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

        //Adding request to request queue
        AppController.getInstance().addToRequestQueue(placeJsonObject, jsonArrayTag);
    }
}
