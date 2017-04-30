package me.chandansharma.aroundme.model;

/**
 * Created by iamcs on 2017-04-29.
 */

public class Place {

    /**
     * All Reference Variable
     */
    private String mPlaceId;
    private Double mLatitude;
    private Double mLongitude;
    private String mPlaceName;
    private boolean mPlaceOpeningHourStatus;
    private Double mPlaceRating;
    private String mPlaceAddress;
    private String mNextPageToken;

    /**
     *
     * @param mPlaceId Place Id
     * @param mLatitude Place Latitude
     * @param mLongitude Place Longitude
     * @param mPlaceName Place Name
     * @param mPlaceOpeningHourStatus Place Opening Status Weather it is Open or Close
     * @param mPlaceRating Place rating example 4.5
     * @param mPlaceAddress Place Address
     * @param mNextPageToken Next page token for more places
     */
    public Place(String mPlaceId, Double mLatitude, Double mLongitude,
                 String mPlaceName, boolean mPlaceOpeningHourStatus,
                 Double mPlaceRating, String mPlaceAddress,
                 String mNextPageToken){

        this.mPlaceId = mPlaceId;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
        this.mPlaceName = mPlaceName;
        this.mPlaceOpeningHourStatus = mPlaceOpeningHourStatus;
        this.mPlaceRating = mPlaceRating;
        this.mPlaceAddress = mPlaceAddress;
        this.mNextPageToken = mNextPageToken;
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

    public boolean isPlaceOpeningHourStatus() {
        return mPlaceOpeningHourStatus;
    }

    public void setPlaceOpeningHourStatus(boolean placeOpeningHourStatus) {
        mPlaceOpeningHourStatus = placeOpeningHourStatus;
    }

    public Double getPlaceRating() {
        return mPlaceRating;
    }

    public void setPlaceRating(Double placeRating) {
        mPlaceRating = placeRating;
    }

    public String getPlaceAddress() {
        return mPlaceAddress;
    }

    public void setPlaceAddress(String placeAddress) {
        mPlaceAddress = placeAddress;
    }

    public String getNextPageToken() {
        return mNextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        mNextPageToken = nextPageToken;
    }
}
