package com.fantechlabs.lailaa.models.pharmact_places;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class NearByPlaces
        implements Parcelable
{

    @SerializedName("results")
    @Expose
    private List<Result> results = null;
    @SerializedName("status")
    @Expose
    private String status;
    public final static Creator<NearByPlaces> CREATOR = new Creator<NearByPlaces>()
    {

        @SuppressWarnings({
                                  "unchecked"
                          })
        public NearByPlaces createFromParcel(Parcel in)
        {
            return new NearByPlaces(in);
        }

        public NearByPlaces[] newArray(int size)
        {
            return (new NearByPlaces[size]);
        }

    };

    protected NearByPlaces(Parcel in)
    {
        in.readList(this.results, (Result.class.getClassLoader()));
        this.status = ((String)in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     */
    public NearByPlaces()
    {
    }

    /**
     * @param results
     * @param status
     */
    public NearByPlaces(List<Result> results, String status)
    {
        super();
        this.results = results;
        this.status = status;
    }

    public List<Result> getResults()
    {
        return results;
    }

    public void setResults(List<Result> results)
    {
        this.results = results;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeList(results);
        dest.writeValue(status);
    }

    public int describeContents()
    {
        return 0;
    }

}