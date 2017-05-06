package me.chandansharma.aroundme.ui;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;

import me.chandansharma.aroundme.R;
import me.chandansharma.aroundme.adapter.FavouritePlaceCursorAdapter;
import me.chandansharma.aroundme.adapter.PlaceListAdapter;
import me.chandansharma.aroundme.data.PlaceDetailContract.PlaceDetailEntry;
import me.chandansharma.aroundme.model.Place;

public class FavouritePlaceListActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    public static final int FAVOURITE_PLACE_DETAIL_LOADER = 0;
    /**
     * ArrayList of the PlaceDetail
     */
    private ArrayList<Place> mFavouritePlaceArrayList = new ArrayList<>();
    private PlaceListAdapter mPlaceListAdapter;
    private RecyclerView mRecyclerView;

    //Adapter for RecyclerView to bind the data from Database using CursorAdapter
    private FavouritePlaceCursorAdapter mFavouritePlaceCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);

        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(actionBar);
        setTitle(getString(R.string.favourite_place_list_string));
        actionBar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.place_list_recycler_view);

        //setup an Adapter to create a list item for each row of the favouritePlace item in the cursor
        mFavouritePlaceCursorAdapter = new FavouritePlaceCursorAdapter(this, null);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        mRecyclerView.setAdapter(mFavouritePlaceCursorAdapter);

        //first time loader is initialize
        getLoaderManager().initLoader(FAVOURITE_PLACE_DETAIL_LOADER, null, this);
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
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {

        //define projection for the favouritePlace Details (No of Column)
        String[] projection = {
                PlaceDetailEntry._ID,
                PlaceDetailEntry.COLUMN_PLACE_ID,
                PlaceDetailEntry.COLUMN_PLACE_LATITUDE,
                PlaceDetailEntry.COLUMN_PLACE_LONGITUDE,
                PlaceDetailEntry.COLUMN_PLACE_NAME,
                PlaceDetailEntry.COLUMN_PLACE_OPENING_HOUR_STATUS,
                PlaceDetailEntry.COLUMN_PLACE_RATING,
                PlaceDetailEntry.COLUMN_PLACE_ADDRESS,
                PlaceDetailEntry.COLUMN_PLACE_PHONE_NUMBER,
                PlaceDetailEntry.COLUMN_PLACE_WEBSITE,
                PlaceDetailEntry.COLUMN_PLACE_SHARE_LINK
        };

        return new CursorLoader(this,
                PlaceDetailEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        //Swap the Cursor for loading new data
        ((FavouritePlaceCursorAdapter) mRecyclerView.getAdapter()).swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        ((FavouritePlaceCursorAdapter) mRecyclerView.getAdapter()).swapCursor(null);
    }
}
