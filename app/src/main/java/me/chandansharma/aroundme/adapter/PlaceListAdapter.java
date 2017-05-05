package me.chandansharma.aroundme.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import me.chandansharma.aroundme.R;
import me.chandansharma.aroundme.model.Place;
import me.chandansharma.aroundme.ui.PlaceDetailActivity;
import me.chandansharma.aroundme.utils.GoogleApiUrl;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created by iamcs on 2017-04-29.
 */

public class PlaceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //Context of the activity
    private Context mContext;
    private ArrayList<Place> mNearByPlaceArrayList = new ArrayList<>();

    public PlaceListAdapter(Context context, ArrayList<Place> nearByPlaceArrayList) {
        mContext = context;
        mNearByPlaceArrayList = nearByPlaceArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PlaceListAdapterViewHolder(LayoutInflater
                .from(mContext).inflate(R.layout.place_list_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((PlaceListAdapterViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return mNearByPlaceArrayList.size();
    }

    private class PlaceListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        int mItemPosition;
        //reference of the views
        private TextView mPlaceNameTextView;
        private TextView mPlaceAddressTextView;
        private TextView mPlaceOpenStatusTextView;
        private MaterialRatingBar mPlaceRating;
        private ImageView mLocationIcon;

        private PlaceListAdapterViewHolder(View itemView) {
            super(itemView);

            mPlaceNameTextView = (TextView) itemView.findViewById(R.id.place_name);
            mPlaceAddressTextView = (TextView) itemView.findViewById(R.id.place_address);
            mPlaceOpenStatusTextView = (TextView) itemView.findViewById(R.id.place_open_status);
            mPlaceRating = (MaterialRatingBar) itemView.findViewById(R.id.place_rating);
            mLocationIcon = (ImageView) itemView.findViewById(R.id.location_icon);

            itemView.setOnClickListener(this);
        }

        private void bindView(int position) {
            mItemPosition = position;

            mPlaceNameTextView.setText(mNearByPlaceArrayList.get(mItemPosition).getPlaceName());
            mPlaceNameTextView.setTypeface(Typeface.createFromAsset(mContext.getAssets(),
                    "Roboto-Regular.ttf"));
            mPlaceAddressTextView.setText(mNearByPlaceArrayList.get(mItemPosition).getPlaceAddress());
            mPlaceAddressTextView.setTypeface(Typeface.createFromAsset(mContext.getAssets(),
                    "Roboto-Regular.ttf"));
            if (mNearByPlaceArrayList.get(mItemPosition).getPlaceOpeningHourStatus().equals("true")) {
                mPlaceOpenStatusTextView.setText(R.string.open_now);
                mPlaceOpenStatusTextView.setTypeface(Typeface.createFromAsset(mContext.getAssets(),
                        "Roboto-Regular.ttf"));
            } else if (mNearByPlaceArrayList.get(mItemPosition).getPlaceOpeningHourStatus().equals("false")) {
                mPlaceOpenStatusTextView.setText(R.string.closed);
                mPlaceOpenStatusTextView.setTypeface(Typeface.createFromAsset(mContext.getAssets(),
                        "Roboto-Regular.ttf"));
            } else {
                mPlaceOpenStatusTextView.setText(mNearByPlaceArrayList.get(mItemPosition)
                        .getPlaceOpeningHourStatus());
                mPlaceOpenStatusTextView.setTypeface(Typeface.createFromAsset(mContext.getAssets(),
                        "Roboto-Regular.ttf"));
            }
            mPlaceRating.setRating(Float.parseFloat(String.valueOf(mNearByPlaceArrayList.get(mItemPosition)
                    .getPlaceRating())));

            mLocationIcon.setColorFilter(ContextCompat.getColor(mContext, R.color.color_divider));
        }

        @Override
        public void onClick(View v) {
            Intent currentLocationDetailIntent = new Intent(mContext, PlaceDetailActivity.class);
            currentLocationDetailIntent.putExtra(GoogleApiUrl.LOCATION_ID_EXTRA_TEXT,
                    mNearByPlaceArrayList.get(mItemPosition).getPlaceId());
            mContext.startActivity(currentLocationDetailIntent);
        }
    }
}
