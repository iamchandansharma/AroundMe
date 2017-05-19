package me.chandansharma.aroundme.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import me.chandansharma.aroundme.R;
import me.chandansharma.aroundme.ui.PlaceListOnMapActivity;
import me.chandansharma.aroundme.utils.GoogleApiUrl;
import me.chandansharma.aroundme.utils.PlaceDetailProvider;

/**
 * Created by iamcs on 2017-04-29.
 */

public class HomeScreenItemListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //Context of the activity
    private Context mContext;
    private String[] mPlacesListTag;

    public HomeScreenItemListAdapter(Context context, String[] placesListTag) {
        mContext = context;
        mPlacesListTag = placesListTag;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HomeScreenItemListHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.home_screen_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((HomeScreenItemListHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return mPlacesListTag.length;
    }


    private class HomeScreenItemListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mPlaceTextView;
        private ImageView mPlaceImageView;
        private int mItemPosition;

        private HomeScreenItemListHolder(View itemView) {
            super(itemView);
            mPlaceTextView = (TextView) itemView.findViewById(R.id.place_text_view);
            mPlaceImageView = (ImageView) itemView.findViewById(R.id.place_icon);

            mPlaceImageView.setOnClickListener(this);
        }

        private void bindView(int position) {
            mPlaceTextView.setText(mPlacesListTag[position]);

            mPlaceImageView.setImageDrawable(ContextCompat.getDrawable(mContext,
                    PlaceDetailProvider.popularPlaceTagIcon[position]));

            mPlaceTextView.setTypeface(Typeface.createFromAsset(mContext.getAssets(),
                    "Roboto-Regular.ttf"));
            int[] colorArray = PlaceDetailProvider.accentColor;

            int randomColor = colorArray[new Random().nextInt(colorArray.length)];

            ((GradientDrawable) mPlaceImageView.getBackground())
                    .setColor(ContextCompat.getColor(mContext, randomColor));

            mItemPosition = position;
        }

        @Override
        public void onClick(View v) {

            if (isNetworkAvailable()) {
                /*
             * get the tag for query parameter like Atm, Bank etc.
             */
                String locationTag = mPlacesListTag[mItemPosition];

                if (locationTag.equals("Bus Stand"))
                    locationTag = "bus_station";
                else if (locationTag.equals("Government Office"))
                    locationTag = "local_government_office";
                else if (locationTag.equals("Railway Station"))
                    locationTag = "train_station";
                else if (locationTag.equals("Hotel"))
                    locationTag = "restaurant";
                else if (locationTag.equals("Police Station"))
                    locationTag = "police";
                else
                    locationTag = locationTag.replace(' ', '_').toLowerCase();
                /**
                 * Intent to start Place list activity with locationTag as extra data.
                 */
                Intent placeTagIntent = new Intent(mContext, PlaceListOnMapActivity.class);
                placeTagIntent.putExtra(GoogleApiUrl.LOCATION_NAME_EXTRA_TEXT,
                        PlaceDetailProvider.popularPlaceTagName[mItemPosition]);
                placeTagIntent.putExtra(GoogleApiUrl.LOCATION_TYPE_EXTRA_TEXT, locationTag);
                mContext.startActivity(placeTagIntent);
            } else
                Snackbar.make(mPlaceImageView, R.string.no_connection_string,
                        Snackbar.LENGTH_SHORT).show();
        }

        private boolean isNetworkAvailable() {
            ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
        }
    }
}
