package me.chandansharma.aroundme.model;

/**
 * Created by iamcs on 2017-04-29.
 */

public class Place {

    /**
     * All Reference Variable
     */
    private String mPlaceId;
    private Double mPlaceLatitude;
    private Double mPlaceLongitude;
    private String mPlaceName;
    private String mPlaceOpeningHourStatus;
    private Double mPlaceRating;
    private String mPlaceAddress;

    /**
     * @param mPlaceId                Place Id
     * @param mPlaceLatitude          Place Latitude
     * @param mPlaceLongitude         Place Longitude
     * @param mPlaceName              Place Name
     * @param mPlaceOpeningHourStatus Place Opening Status Weather it is Open or Close
     * @param mPlaceRating            Place rating example 4.5
     * @param mPlaceAddress           Place Address
     */

    public Place(String mPlaceId, Double mPlaceLatitude, Double mPlaceLongitude,
                 String mPlaceName, String mPlaceOpeningHourStatus,
                 Double mPlaceRating, String mPlaceAddress) {

        this.mPlaceId = mPlaceId;
        this.mPlaceLatitude = mPlaceLatitude;
        this.mPlaceLongitude = mPlaceLongitude;
        this.mPlaceName = mPlaceName;
        this.mPlaceOpeningHourStatus = mPlaceOpeningHourStatus;
        this.mPlaceRating = mPlaceRating;
        this.mPlaceAddress = mPlaceAddress;
    }

    public Place(String mPlaceId, Double mPlaceLatitude, Double mPlaceLongitude){
        this.mPlaceId = mPlaceId;
        this.mPlaceLatitude = mPlaceLatitude;
        this.mPlaceLongitude = mPlaceLongitude;
    }

    public String getPlaceId() {
        return mPlaceId;
    }

    public void setPlaceId(String placeId) {
        mPlaceId = placeId;
    }

    public Double getPlaceLatitude() {
        return mPlaceLatitude;
    }

    public void setPlaceLatitude(Double placeLatitude) {
        mPlaceLatitude = placeLatitude;
    }

    public Double getPlaceLongitude() {
        return mPlaceLongitude;
    }

    public void setPlaceLongitude(Double placeLongitude) {
        mPlaceLongitude = placeLongitude;
    }

    public String getPlaceName() {
        return mPlaceName;
    }

    public void setPlaceName(String placeName) {
        mPlaceName = placeName;
    }

    public String getPlaceOpeningHourStatus() {
        return mPlaceOpeningHourStatus;
    }

    public void setPlaceOpeningHourStatus(String placeOpeningHourStatus) {
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

}
