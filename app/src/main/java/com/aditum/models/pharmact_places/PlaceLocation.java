package com.aditum.models.pharmact_places;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlaceLocation
        implements Parcelable
{

    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lng")
    @Expose
    private Double lng;
    public final static Creator<PlaceLocation> CREATOR = new Creator<PlaceLocation>()
    {


        @SuppressWarnings({
                                  "unchecked"
                          })
        public PlaceLocation createFromParcel(Parcel in)
        {
            return new PlaceLocation(in);
        }

        public PlaceLocation[] newArray(int size)
        {
            return (new PlaceLocation[size]);
        }

    };

    protected PlaceLocation(Parcel in)
    {
        this.lat = ((Double)in.readValue((Double.class.getClassLoader())));
        this.lng = ((Double)in.readValue((Double.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     */
    public PlaceLocation()
    {
    }

    /**
     * @param lng
     * @param lat
     */
    public PlaceLocation(Double lat, Double lng)
    {
        super();
        this.lat = lat;
        this.lng = lng;
    }

    public Double getLat()
    {
        return lat;
    }

    public void setLat(Double lat)
    {
        this.lat = lat;
    }

    public Double getLng()
    {
        return lng;
    }

    public void setLng(Double lng)
    {
        this.lng = lng;
    }

    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeValue(lat);
        dest.writeValue(lng);
    }

    public int describeContents()
    {
        return 0;
    }

}