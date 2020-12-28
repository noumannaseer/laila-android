package com.fantechlabs.lailaa.request_models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fantechlabs.lailaa.models.Events;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddEventRequest
    implements Parcelable
{
    @SerializedName("user_private_code")
    @Expose
    private String user_private_code;
    @SerializedName("events")
    @Expose
    private List<Events> events;


    public AddEventRequest(String user_private_code, List<Events> events)
    {
        this.user_private_code = user_private_code;
        this.events = events;
    }
    protected AddEventRequest(Parcel in) {
        user_private_code = in.readString();
        events = in.createTypedArrayList(Events.CREATOR);
    }


    public static final Creator<AddEventRequest> CREATOR = new Creator<AddEventRequest>() {
        @Override
        public AddEventRequest createFromParcel(Parcel in) {
            return new AddEventRequest(in);
        }

        @Override
        public AddEventRequest[] newArray(int size) {
            return new AddEventRequest[size];
        }
    };

    public String getUser_private_code() { return user_private_code; }

    public void setUser_private_code(String user_private_code) { this.user_private_code = user_private_code; }

    public List<Events> getEvents() { return events; }

    public void setEvents(List<Events> events) { this.events = events; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_private_code);
        dest.writeTypedList(events);
    }
}
