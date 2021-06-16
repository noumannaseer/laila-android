package com.aditum.models.updates.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Event implements Parcelable {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("event_title")
    @Expose
    private String eventTitle;
    @SerializedName("medication_id")
    @Expose
    private String medicationId;
    @SerializedName("contact_id")
    @Expose
    private String contactId;
    @SerializedName("delivery_type")
    @Expose
    private String deliveryType;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("frequency")
    @Expose
    private Integer frequency;
    @SerializedName("timeSchedule")
    @Expose
    private String timeSchedule;

    public Event() {
    }

    protected Event(Parcel in) {
        type = in.readString();
        eventTitle = in.readString();
        if (in.readByte() == 0) {
            medicationId = null;
        } else {
            medicationId = in.readString();
        }
        contactId = in.readString();
        deliveryType = in.readString();
        startDate = in.readString();
        endDate = in.readString();
        if (in.readByte() == 0) {
            frequency = null;
        } else {
            frequency = in.readInt();
        }
        timeSchedule = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(eventTitle);
        if (medicationId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeString(medicationId);
        }
        dest.writeString(contactId);
        dest.writeString(deliveryType);
        dest.writeString(startDate);
        dest.writeString(endDate);
        if (frequency == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(frequency);
        }
        dest.writeString(timeSchedule);
    }
}
