package com.aditum.models.response_models;

import android.os.Parcel;
import android.os.Parcelable;

import com.aditum.models.Events;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

//*************************************************
public class MedicineEventResponse
    implements Parcelable
//*************************************************
{

    @SerializedName("events")
    @Expose
    private ArrayList<Events> events;
    @SerializedName("error")
    @Expose
    private String error;

    protected MedicineEventResponse(Parcel in) {
        events = in.createTypedArrayList(Events.CREATOR);
        error = in.readString();
    }

    public static final Creator<MedicineEventResponse> CREATOR = new Creator<MedicineEventResponse>() {
        @Override
        public MedicineEventResponse createFromParcel(Parcel in) {
            return new MedicineEventResponse(in);
        }

        @Override
        public MedicineEventResponse[] newArray(int size) {
            return new MedicineEventResponse[size];
        }
    };

    public ArrayList<Events> getEvents() { return events; }

    public void setEvents(ArrayList<Events> events) { this.events = events; }

    public String getError() { return error; }

    public void setError(String error) { this.error = error; }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(events);
        dest.writeString(error);
    }
}
