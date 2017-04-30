package me.chandansharma.aroundme.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import me.chandansharma.aroundme.R;
import me.chandansharma.aroundme.model.PlaceDetails;

public class PlaceDetailActivity extends AppCompatActivity {

    public static final String TAG = PlaceDetailActivity.class.getSimpleName();

    /**
     * ArrayList to store the Near By Place List
     */
    private ArrayList<PlaceDetails> mPlaceDetailsArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);
    }
}
