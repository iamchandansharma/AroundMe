package me.chandansharma.aroundme.model;

import java.util.ArrayList;

/**
 * Created by iamcs on 2017-04-29.
 */

public class PlaceDetails {

    /**
     * All Reference Variable
     */
    private String mPlaceId;
    private Double mLatitude;
    private Double mLongitude;
    private String mPlaceName;
    private String mPlaceAddress;
    private String mPlacePhoneNumber;
    private ArrayList<String> mPlaceWeekDaysTime;
    private boolean mPlaceOpeningHourStatus;
    private String mPlaceWebsite;

    /**
     *
     * @param mPlaceId Place Id
     * @param mLatitude Place Latitude
     * @param mLongitude Place Longitude
     * @param mPlaceName Place Name
     * @param mPlaceAddress Place Address
     * @param mPlacePhoneNumber Place Phone number
     * @param mPlaceWeekDaysTime Place all days open timing
     * @param mPlaceOpeningHourStatus Place open status
     * @param mPlaceWebsite Place Website
     */
    public PlaceDetails(String mPlaceId, Double mLatitude, Double mLongitude,
                        String mPlaceName, String mPlaceAddress, String mPlacePhoneNumber,
                        ArrayList<String> mPlaceWeekDaysTime, boolean mPlaceOpeningHourStatus,
                        String mPlaceWebsite) {

        this.mPlaceId = mPlaceId;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
        this.mPlaceName = mPlaceName;
        this.mPlaceAddress = mPlaceAddress;
        this.mPlacePhoneNumber = mPlacePhoneNumber;
        this.mPlaceWeekDaysTime = mPlaceWeekDaysTime;
        this.mPlaceOpeningHourStatus = mPlaceOpeningHourStatus;
        this.mPlaceWebsite = mPlaceWebsite;
    }

    public String getPlaceId() {
        return mPlaceId;
    }

    public void setPlaceId(String placeId) {
        mPlaceId = placeId;
    }

    public Double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(Double latitude) {
        mLatitude = latitude;
    }

    public Double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(Double longitude) {
        mLongitude = longitude;
    }

    public String getPlaceName() {
        return mPlaceName;
    }

    public void setPlaceName(String placeName) {
        mPlaceName = placeName;
    }

    public String getPlaceAddress() {
        return mPlaceAddress;
    }

    public void setPlaceAddress(String placeAddress) {
        mPlaceAddress = placeAddress;
    }

    public String getPlacePhoneNumber() {
        return mPlacePhoneNumber;
    }

    public void setPlacePhoneNumber(String placePhoneNumber) {
        mPlacePhoneNumber = placePhoneNumber;
    }

    public ArrayList<String> getPlaceWeekDaysTime() {
        return mPlaceWeekDaysTime;
    }

    public void setPlaceWeekDaysTime(ArrayList<String> placeWeekDaysTime) {
        mPlaceWeekDaysTime = placeWeekDaysTime;
    }

    public boolean isPlaceOpeningHourStatus() {
        return mPlaceOpeningHourStatus;
    }

    public void setPlaceOpeningHourStatus(boolean placeOpeningHourStatus) {
        mPlaceOpeningHourStatus = placeOpeningHourStatus;
    }

    public String getPlaceWebsite() {
        return mPlaceWebsite;
    }

    public void setPlaceWebsite(String placeWebsite) {
        mPlaceWebsite = placeWebsite;
    }
}
