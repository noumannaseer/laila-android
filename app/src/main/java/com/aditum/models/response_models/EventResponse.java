package com.aditum.models.response_models;

import android.os.Parcel;
import android.os.Parcelable;

import com.aditum.models.Events;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//*************************************************
public class EventResponse
    implements Parcelable
//*************************************************
{

    @SerializedName("event")
    @Expose
    private Events event;
    @SerializedName("error")
    @Expose
    private String error;

    protected EventResponse(Parcel in) {
        event = in.readParcelable(Events.class.getClassLoader());
        error = in.readString();
    }

    public static final Creator<EventResponse> CREATOR = new Creator<EventResponse>() {
        @Override
        public EventResponse createFromParcel(Parcel in) {
            return new EventResponse(in);
        }

        @Override
        public EventResponse[] newArray(int size) {
            return new EventResponse[size];
        }
    };

    public Events getEvent() { return event; }

    public void setEvent(Events event) { this.event = event; }

    public String getError() { return error; }

    public void setError(String error) { this.error = error; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(event, flags);
        dest.writeString(error);
    }
}
