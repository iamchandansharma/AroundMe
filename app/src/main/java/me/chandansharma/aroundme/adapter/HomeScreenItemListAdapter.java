package me.chandansharma.aroundme.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.chandansharma.aroundme.R;
import me.chandansharma.aroundme.ui.PlaceListActivity;
import me.chandansharma.aroundme.utils.GoogleApiUrl;

/**
 * Created by iamcs on 2017-04-29.
 */

public class HomeScreenItemListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //Context of the activity
    private Context mContext;
    private String [] mPlacesListTag;
    private String mCurrentLocation;

    public HomeScreenItemListAdapter(Context context, String [] placesListTag, String currentLocation){
        mContext = context;
        mPlacesListTag = placesListTag;
        mCurrentLocation = currentLocation;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HomeScreenItemListHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.home_screen_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((HomeScreenItemListHolder)holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return mContext.getResources().getStringArray(R.array.input).length;
    }


    private class HomeScreenItemListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mItemTextView;
        private int mItemPosition;

        private HomeScreenItemListHolder(View itemView) {
            super(itemView);
            mItemTextView = (TextView) itemView.findViewById(R.id.item_text_view);

            mItemTextView.setOnClickListener(this);
        }

        private void bindView(int position){
            mItemTextView.setText(mPlacesListTag[position]);
            mItemPosition = position;
        }

        @Override
        public void onClick(View v) {
            /**
             * get the tag for query parameter like Atm, Bank etc.
             */
            String locationTag = mPlacesListTag[mItemPosition].toLowerCase();
            /**
             * Intent to start Place list activity with locationTag as extra data.
             */
            Intent placeTagIntent = new Intent(mContext, PlaceListActivity.class);
            placeTagIntent.putExtra(GoogleApiUrl.LOCATION_TYPE_EXTRA_TEXT, locationTag);
            placeTagIntent.putExtra(GoogleApiUrl.LOCATION_EXTRA_TEXT,mCurrentLocation);
            mContext.startActivity(placeTagIntent);
        }
    }
}
