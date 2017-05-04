package me.chandansharma.aroundme.ui;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

import me.chandansharma.aroundme.R;
import me.chandansharma.aroundme.adapter.PlaceListAdapter;
import me.chandansharma.aroundme.data.PlaceDetailContract;
import me.chandansharma.aroundme.data.PlaceDetailContract.PlaceDetailEntry;
import me.chandansharma.aroundme.model.Place;

public class FavouritePlaceListActivity extends AppCompatActivity {

    /**
     * ArrayList of the PlaceDetail
     */
    private ArrayList<Place> mFavouritePlaceArrayList = new ArrayList<>();
    private PlaceListAdapter mPlaceListAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);

        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(actionBar);
        setTitle("Favourite Place List");
        actionBar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Method to required all details about place
        //getNearByPlaceDetails(locationQueryStringUrl);
        getPlaceListData();
        mRecyclerView = (RecyclerView) findViewById(R.id.place_list_recycler_view);

        if (mFavouritePlaceArrayList.size() == 0) {
            findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            findViewById(R.id.empty_view).setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mPlaceListAdapter = new PlaceListAdapter(this, mFavouritePlaceArrayList);
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
            mRecyclerView.setAdapter(mPlaceListAdapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFavouritePlaceArrayList.clear();
        getPlaceListData();
        if (mFavouritePlaceArrayList.size() == 0) {
            findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else
            mPlaceListAdapter.notifyDataSetChanged();
    }

    private void getPlaceListData() {

        //Uri for requesting data from Database
        Uri placeContentUri = PlaceDetailContract.PlaceDetailEntry.CONTENT_URI;

        //Cursor object to get resultSet from Database

        Cursor placeDetailDataCursor = getContentResolver().query(
                placeContentUri, null, null, null, null);

        if (placeDetailDataCursor.moveToFirst()) {
            do {
                Place singlePlaceDetail = new Place(
                        placeDetailDataCursor.getString(placeDetailDataCursor
                                .getColumnIndex(PlaceDetailEntry.COLUMN_PLACE_ID)),
                        placeDetailDataCursor.getDouble(placeDetailDataCursor
                                .getColumnIndex(PlaceDetailEntry.COLUMN_PLACE_LATITUDE)),
                        placeDetailDataCursor.getDouble(placeDetailDataCursor
                                .getColumnIndex(PlaceDetailEntry.COLUMN_PLACE_LONGITUDE)),
                        placeDetailDataCursor.getString(placeDetailDataCursor
                                .getColumnIndex(PlaceDetailEntry.COLUMN_PLACE_NAME)),
                        placeDetailDataCursor.getString(placeDetailDataCursor
                                .getColumnIndex(PlaceDetailEntry.COLUMN_PLACE_OPENING_HOUR_STATUS)),
                        placeDetailDataCursor.getDouble(placeDetailDataCursor
                                .getColumnIndex(PlaceDetailEntry.COLUMN_PLACE_RATING)),
                        placeDetailDataCursor.getString(placeDetailDataCursor
                                .getColumnIndex(PlaceDetailEntry.COLUMN_PLACE_ADDRESS)),
                        placeDetailDataCursor.getString(placeDetailDataCursor
                                .getColumnIndex(PlaceDetailEntry.COLUMN_PLACE_PHONE_NUMBER)),
                        placeDetailDataCursor.getString(placeDetailDataCursor
                                .getColumnIndex(PlaceDetailEntry.COLUMN_PLACE_WEBSITE)),
                        placeDetailDataCursor.getString(placeDetailDataCursor
                                .getColumnIndex(PlaceDetailEntry.COLUMN_PLACE_SHARE_LINK)));
                mFavouritePlaceArrayList.add(singlePlaceDetail);
            } while (placeDetailDataCursor.moveToNext());
        }
        placeDetailDataCursor.close();
    }
}
