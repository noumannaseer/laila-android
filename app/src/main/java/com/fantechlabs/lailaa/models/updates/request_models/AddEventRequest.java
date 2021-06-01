package com.fantechlabs.lailaa.models.updates.request_models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fantechlabs.lailaa.models.updates.models.Event;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddEventRequest implements Parcelable {
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("events")
    @Expose
    private List<Event> events = null;

    public AddEventRequest() {
    }

    //******************************************************
    public static AddEventRequest Builder()
    //******************************************************
    {
        return new AddEventRequest();
    }

    protected AddEventRequest(Parcel in) {
        if (in.readByte() == 0) {
            userId = null;
        } else {
            userId = in.readInt();
        }
        token = in.readString();
        events = in.createTypedArrayList(Event.CREATOR);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (userId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(userId);
        }
        dest.writeString(token);
        dest.writeTypedList(events);
    }
}
