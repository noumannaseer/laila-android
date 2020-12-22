package com.fantechlabs.lailaa.request_models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddMedicineEventRequest
    implements Parcelable
{
    @SerializedName("events")
    @Expose
    private AddEventRequest events;


    public AddMedicineEventRequest(){}

    public AddMedicineEventRequest(AddEventRequest event)
    {
        this.events = event;
    }

    protected AddMedicineEventRequest(Parcel in) {
        events = in.readParcelable(AddEventRequest.class.getClassLoader());
    }


    public static final Creator<AddMedicineEventRequest> CREATOR = new Creator<AddMedicineEventRequest>() {
        @Override
        public AddMedicineEventRequest createFromParcel(Parcel in) {
            return new AddMedicineEventRequest(in);
        }

        @Override
        public AddMedicineEventRequest[] newArray(int size) {
            return new AddMedicineEventRequest[size];
        }
    };

    public AddEventRequest getEvents() { return events; }

    public void setEvents(AddEventRequest events) { this.events = events; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(events, flags);
    }
}
