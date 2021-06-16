package com.aditum.models.updates.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseEvent implements Parcelable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
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
    private Integer contactId;
    @SerializedName("delivery_type")
    @Expose
    private String deliveryType;
    @SerializedName("start_date")
    @Expose
    private long startDate;
    @SerializedName("end_date")
    @Expose
    private long endDate;
    @SerializedName("frequency")
    @Expose
    private Integer frequency;
    @SerializedName("timeSchedule")
    @Expose
    private String timeSchedule;
    @SerializedName("created_DateTime")
    @Expose
    private long createdDateTime;

    public ResponseEvent() {
    }

    protected ResponseEvent(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        if (in.readByte() == 0) {
            userId = null;
        } else {
            userId = in.readInt();
        }
        type = in.readString();
        eventTitle = in.readString();
        if (in.readByte() == 0) {
            medicationId = null;
        } else {
            medicationId = in.readString();
        }
        if (in.readByte() == 0) {
            contactId = null;
        } else {
            contactId = in.readInt();
        }
        deliveryType = in.readString();
        startDate = in.readLong();
        endDate = in.readLong();
        if (in.readByte() == 0) {
            frequency = null;
        } else {
            frequency = in.readInt();
        }
        timeSchedule = in.readString();
        createdDateTime = in.readLong();
    }

    public static final Creator<ResponseEvent> CREATOR = new Creator<ResponseEvent>() {
        @Override
        public ResponseEvent createFromParcel(Parcel in) {
            return new ResponseEvent(in);
        }

        @Override
        public ResponseEvent[] newArray(int size) {
            return new ResponseEvent[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        if (userId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(userId);
        }
        dest.writeString(type);
        dest.writeString(eventTitle);
        if (medicationId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeString(medicationId);
        }
        if (contactId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(contactId);
        }
        dest.writeString(deliveryType);
        dest.writeLong(startDate);
        dest.writeLong(endDate);
        if (frequency == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(frequency);
        }
        dest.writeString(timeSchedule);
        dest.writeLong(createdDateTime);
    }
}
