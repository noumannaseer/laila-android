package com.aditum.models.pharmact_places;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Geometry
        implements Parcelable
{

    @SerializedName("location")
    @Expose
    private PlaceLocation location;
    public final static Creator<Geometry> CREATOR = new Creator<Geometry>()
    {


        @SuppressWarnings({
                                  "unchecked"
                          })
        public Geometry createFromParcel(Parcel in)
        {
            return new Geometry(in);
        }

        public Geometry[] newArray(int size)
        {
            return (new Geometry[size]);
        }

    };

    protected Geometry(Parcel in)
    {
        this.location = ((PlaceLocation)in.readValue((PlaceLocation.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     */
    public Geometry()
    {
    }

    /**
     * @param location
     */
    public Geometry(PlaceLocation location)
    {
        super();
        this.location = location;
    }

    public PlaceLocation getLocation()
    {
        return location;
    }

    public void setLocation(PlaceLocation location)
    {
        this.location = location;
    }

    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeValue(location);
    }

    public int describeContents()
    {
        return 0;
    }

}