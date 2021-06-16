package com.aditum.request_models;

import android.os.Parcel;
import android.os.Parcelable;

import com.aditum.models.Events;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//**************************************
public class EventRequest
    implements Parcelable
//**************************************
{
    @SerializedName("event")
    @Expose
    private Events event;
    @SerializedName("error")
    @Expose
    private String error;

    public EventRequest(){}

    public EventRequest(Events event)
    {
        this.event = event;
    }

    protected EventRequest(Parcel in) {
        event = in.readParcelable(Events.class.getClassLoader());
    }


    public static final Creator<EventRequest> CREATOR = new Creator<EventRequest>() {
        @Override
        public EventRequest createFromParcel(Parcel in) {
            return new EventRequest(in);
        }

        @Override
        public EventRequest[] newArray(int size) {
            return new EventRequest[size];
        }
    };

    public Events getEvent() { return event; }

    public void setEvent(Events event) { this.event = event; }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getError() { return error; }

    public void setError(String error) { this.error = error; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(event, flags);
    }
}
