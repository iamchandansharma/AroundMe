package me.chandansharma.aroundme.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import me.chandansharma.aroundme.R;
import me.chandansharma.aroundme.model.Place;

/**
 * Created by iamcs on 2017-04-29.
 */

public class PlaceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    //Context of the activity
    private Context mContext;
    private ArrayList<Place> mNearByPlaceArrayList = new ArrayList<>();

    public PlaceListAdapter(Context context, ArrayList<Place> nearByPlaceArrayList){
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
        ((PlaceListAdapterViewHolder)holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return mNearByPlaceArrayList.size();
    }

    private class PlaceListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        //reference of the views
        private TextView mPlaceNameTextView;
        private TextView mPlaceAddressTextView;
        private TextView mPlaceOpenStatusTextView;
        private TextView mPlaceRatingTextView;
        int mItemPosition;

        private PlaceListAdapterViewHolder(View itemView) {
            super(itemView);

            mPlaceNameTextView = (TextView) itemView.findViewById(R.id.place_name);
            mPlaceAddressTextView = (TextView) itemView.findViewById(R.id.place_address);
            mPlaceOpenStatusTextView = (TextView) itemView.findViewById(R.id.place_open_status);
            mPlaceRatingTextView = (TextView) itemView.findViewById(R.id.place_rating);

            itemView.setOnClickListener(this);
        }

        private void bindView(int position){
            mItemPosition = position;

            mPlaceNameTextView.setText(mNearByPlaceArrayList.get(mItemPosition).getPlaceName());
            mPlaceAddressTextView.setText(mNearByPlaceArrayList.get(mItemPosition).getPlaceAddress());
            if(mNearByPlaceArrayList.get(mItemPosition).getPlaceOpeningHourStatus().equals("true"))
                mPlaceOpenStatusTextView.setText("Open Now");
            else if(mNearByPlaceArrayList.get(mItemPosition).getPlaceOpeningHourStatus().equals("false"))
                mPlaceOpenStatusTextView.setText("Closed");
            else
                mPlaceOpenStatusTextView.setText(mNearByPlaceArrayList.get(mItemPosition)
                        .getPlaceOpeningHourStatus());
            mPlaceRatingTextView.setText(String.valueOf(mNearByPlaceArrayList.get(mItemPosition)
                    .getPlaceRating()));
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(mContext,"Hello",Toast.LENGTH_SHORT).show();
        }
    }
}
