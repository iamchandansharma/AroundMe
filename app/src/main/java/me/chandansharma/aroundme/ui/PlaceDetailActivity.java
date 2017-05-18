package me.chandansharma.aroundme.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.chandansharma.aroundme.R;
import me.chandansharma.aroundme.fragment.PlaceAboutDetail;
import me.chandansharma.aroundme.fragment.PlaceReviewDetail;
import me.chandansharma.aroundme.model.Place;
import me.chandansharma.aroundme.model.PlaceUserRating;
import me.chandansharma.aroundme.utils.AppController;
import me.chandansharma.aroundme.utils.GoogleApiUrl;

import static me.chandansharma.aroundme.R.id.viewpager;

public class PlaceDetailActivity extends AppCompatActivity {

    public static final String TAG = PlaceDetailActivity.class.getSimpleName();
    private String mCurrentPlaceDetailUrl;
    private ArrayList<PlaceUserRating> mPlaceUserRatingsArrayList = new ArrayList<>();
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private String mPlaceShareUrl;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        String currentPlaceDetailId = getIntent().getStringExtra(GoogleApiUrl.LOCATION_ID_EXTRA_TEXT);
        mCurrentPlaceDetailUrl = GoogleApiUrl.BASE_URL + GoogleApiUrl.LOCATION_DETAIL_TAG + "/" +
                GoogleApiUrl.JSON_FORMAT_TAG + "?" + GoogleApiUrl.LOCATION_PLACE_ID_TAG + "=" +
                currentPlaceDetailId + "&" + GoogleApiUrl.API_KEY_TAG + "=" + GoogleApiUrl.API_KEY;
        Log.d(TAG, mCurrentPlaceDetailUrl);

        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(actionBar);
        setTitle(R.string.app_name);
        actionBar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(viewpager);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);

        //Method to get the all details about place
        getCurrentPlaceAllDetails(mCurrentPlaceDetailUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menu.removeItem(R.id.search);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.share_icon:
                Intent sharePlaceUrlIntent = new Intent(Intent.ACTION_SEND);
                sharePlaceUrlIntent.setType("text/plain");
                sharePlaceUrlIntent.putExtra(Intent.EXTRA_TEXT, mPlaceShareUrl);
                startActivity(sharePlaceUrlIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void getCurrentPlaceAllDetails(final String currentPlaceDetailUrl) {
        String jsonArrayTag = "jsonArrayTag";
        JsonObjectRequest placeJsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                currentPlaceDetailUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mProgressBar.setVisibility(View.GONE);
                        try {
                            JSONObject rootJsonObject = response.getJSONObject("result");

                            String currentPlaceId = rootJsonObject.getString("place_id");
                            Double currentPlaceLatitude = rootJsonObject
                                    .getJSONObject("geometry").getJSONObject("location")
                                    .getDouble("lat");
                            Double currentPlaceLongitude = rootJsonObject
                                    .getJSONObject("geometry").getJSONObject("location")
                                    .getDouble("lng");
                            String currentPlaceName = rootJsonObject.getString("name");
                            String currentPlaceOpeningHourStatus = rootJsonObject
                                    .has("opening_hours") ? rootJsonObject.getJSONObject("opening_hours")
                                    .getString("open_now") : "Status Not Available";
                            if (currentPlaceOpeningHourStatus.equals("true"))
                                currentPlaceOpeningHourStatus = getString(R.string.open_now);
                            else if (currentPlaceOpeningHourStatus.equals("false"))
                                currentPlaceOpeningHourStatus = getString(R.string.closed);

                            Double currentPlaceRating = rootJsonObject.has("rating") ?
                                    rootJsonObject.getDouble("rating") : 0;
                            String currentPlaceAddress = rootJsonObject.has("formatted_address") ?
                                    rootJsonObject.getString("formatted_address") :
                                    "Address Not Available";
                            String currentPlacePhoneNumber = rootJsonObject
                                    .has("international_phone_number") ? rootJsonObject
                                    .getString("international_phone_number") :
                                    "Phone Number Not Registered";
                            String currentPlaceWebsite = rootJsonObject.has("website") ?
                                    rootJsonObject.getString("website") :
                                    "Website Not Registered";
                            String currentPlaceShareLink = rootJsonObject.getString("url");
                            mPlaceShareUrl = currentPlaceShareLink;

                            Place currentPlaceDetail = new Place(
                                    currentPlaceId,
                                    currentPlaceLatitude,
                                    currentPlaceLongitude,
                                    currentPlaceName,
                                    currentPlaceOpeningHourStatus,
                                    currentPlaceRating,
                                    currentPlaceAddress,
                                    currentPlacePhoneNumber,
                                    currentPlaceWebsite, currentPlaceShareLink);

                            if (rootJsonObject.has("reviews")) {

                                JSONArray reviewJsonArray = rootJsonObject.getJSONArray("reviews");
                                for (int i = 0; i < reviewJsonArray.length(); i++) {
                                    JSONObject currentUserRating = (JSONObject) reviewJsonArray.
                                            get(i);

                                    PlaceUserRating currentPlaceUserRating = new PlaceUserRating(
                                            currentUserRating.getString("author_name"),
                                            currentUserRating.getString("profile_photo_url"),
                                            currentUserRating.getDouble("rating"),
                                            currentUserRating.getString("relative_time_description"),
                                            currentUserRating.getString("text"));

                                    mPlaceUserRatingsArrayList.add(currentPlaceUserRating);
                                }
                            }

                            Bundle currentPlaceDetailData = new Bundle();
                            currentPlaceDetailData.putParcelable(
                                    GoogleApiUrl.CURRENT_LOCATION_DATA_KEY, currentPlaceDetail);
                            Bundle currentPlaceUserRatingDetail = new Bundle();
                            currentPlaceUserRatingDetail.putParcelableArrayList(
                                    GoogleApiUrl.CURRENT_LOCATION_USER_RATING_KEY,
                                    mPlaceUserRatingsArrayList);

                            setupViewPager(mViewPager, currentPlaceDetailData,
                                    currentPlaceUserRatingDetail);

                            mTabLayout.setupWithViewPager(mViewPager);
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

    private void setupViewPager(ViewPager viewPager, Bundle currentPlaceDetailData,
                                Bundle currentPlaceUserRatingDetail) {
        ViewPagerAdapter viewpagerAdapter = new ViewPagerAdapter(
                getSupportFragmentManager());
        viewpagerAdapter.setBundleData(currentPlaceDetailData, currentPlaceUserRatingDetail);
        viewPager.setAdapter(viewpagerAdapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        Bundle placeAboutFragmentBundle;
        Bundle placeReviewFragmentBundle;

        private ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    PlaceAboutDetail placeAboutDetailFragment = new PlaceAboutDetail();
                    placeAboutDetailFragment.setArguments(placeAboutFragmentBundle);
                    return placeAboutDetailFragment;
                case 1:
                    PlaceReviewDetail placeReviewDetailFragment = new PlaceReviewDetail();
                    placeReviewDetailFragment.setArguments(placeReviewFragmentBundle);
                    return placeReviewDetailFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        private void setBundleData(Bundle aboutFragmentBundle, Bundle reviewFragmentBundle) {
            placeAboutFragmentBundle = aboutFragmentBundle;
            placeReviewFragmentBundle = reviewFragmentBundle;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.about_string);
                case 1:
                    return getString(R.string.review_string);
            }
            return null;
        }
    }
}
