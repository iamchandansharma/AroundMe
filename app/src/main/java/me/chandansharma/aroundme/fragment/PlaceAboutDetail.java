package me.chandansharma.aroundme.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import me.chandansharma.aroundme.R;
import me.chandansharma.aroundme.model.Place;
import me.chandansharma.aroundme.utils.GoogleApiUrl;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceAboutDetail extends Fragment implements OnMapReadyCallback {


    /**
     * All references of the views
     */

    private GoogleMap mGoogleMap;
    private boolean mMapReady = false;
    private Place mCurrentPlace;

    private TextView mLocationAddressTextView;
    private TextView mLocationPhoneTextView;
    private TextView mLocationWebsiteTextView;
    private TextView mLocationOpeningStatusTextView;

    public PlaceAboutDetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_place_about_detail, container, false);

        MapFragment mapFragment = (MapFragment) getActivity()
                .getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ((ImageView) rootView.findViewById(R.id.location_phone_icon)).
                setColorFilter(ContextCompat.getColor(getActivity(),R.color.color_primary));
        ((ImageView) rootView.findViewById(R.id.location_website_icon))
                .setColorFilter(ContextCompat.getColor(getActivity(),R.color.color_primary));
        ((ImageView) rootView.findViewById(R.id.location_favourite_icon))
                .setColorFilter(ContextCompat.getColor(getActivity(),R.color.color_primary));

        mLocationAddressTextView = (TextView) rootView.findViewById(R.id.location_address_text_view);
        mLocationPhoneTextView = (TextView) rootView.findViewById(R.id.location_phone_number_text_view);
        mLocationWebsiteTextView = (TextView) rootView.findViewById(R.id.location_website_text_view);
        mLocationOpeningStatusTextView = (TextView) rootView.findViewById(R.id.location_status_text_view);

        ((ImageView) rootView.findViewById(R.id.small_location_icon))
                .setColorFilter(ContextCompat.getColor(getActivity(),R.color.color_primary));
        ((ImageView) rootView.findViewById(R.id.small_phone_icon))
                .setColorFilter(ContextCompat.getColor(getActivity(),R.color.color_primary));
        ((ImageView) rootView.findViewById(R.id.small_website_icon))
                .setColorFilter(ContextCompat.getColor(getActivity(),R.color.color_primary));
        ((ImageView) rootView.findViewById(R.id.small_location_status_icon))
                .setColorFilter(ContextCompat.getColor(getActivity(),R.color.color_primary));

        mCurrentPlace = getArguments().getParcelable(GoogleApiUrl.CURRENT_LOCATION_DATA_KEY);
        if (mCurrentPlace != null) {
            mLocationAddressTextView.setText(mCurrentPlace.getPlaceAddress());
            mLocationPhoneTextView.setText(mCurrentPlace.getPlacePhoneNumber());
            mLocationWebsiteTextView.setText(mCurrentPlace.getPlaceWebsite());
            mLocationOpeningStatusTextView.setText(mCurrentPlace.getPlaceOpeningHourStatus());
        }

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
