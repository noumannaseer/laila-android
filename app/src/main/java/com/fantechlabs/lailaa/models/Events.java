package com.fantechlabs.lailaa.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//***************************************
public class Events
    implements Parcelable
//***************************************
{
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_private_code")
    @Expose
    private String userPrivateCode;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("event_title")
    @Expose
    private String eventTitle;
    @SerializedName("medication_id")
    @Expose
    private Integer medicationId;
    @SerializedName("contact_id")
    @Expose
    private Integer contactId;
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
    private String frequency;
    @SerializedName("alarm_type")
    @Expose
    private String alarmType;
    @SerializedName("timeSchedule")
    @Expose
    private String timeSchedule;
    @SerializedName("no_of_pills")
    @Expose
    private Integer noOfPills;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("followup_id")
    @Expose
    private Integer followupId;

    public Events(){}

    protected Events(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        userPrivateCode = in.readString();
        type = in.readString();
        eventTitle = in.readString();
        if (in.readByte() == 0) {
            medicationId = null;
        } else {
            medicationId = in.readInt();
        }
        if (in.readByte() == 0) {
            contactId = null;
        } else {
            contactId = in.readInt();
        }
        deliveryType = in.readString();
        startDate = in.readString();
        endDate = in.readString();
        frequency = in.readString();
        alarmType = in.readString();
        timeSchedule = in.readString();
        if (in.readByte() == 0) {
            noOfPills = null;
        } else {
            noOfPills = in.readInt();
        }
        error = in.readString();
        result = in.readString();
        if (in.readByte() == 0) {
            followupId = null;
        } else {
            followupId = in.readInt();
        }
    }

    public static final Creator<Events> CREATOR = new Creator<Events>() {
        @Override
        public Events createFromParcel(Parcel in) {
            return new Events(in);
        }

        @Override
        public Events[] newArray(int size) {
            return new Events[size];
        }
    };

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getUserPrivateCode() { return userPrivateCode; }

    public void setUserPrivateCode(String userPrivateCode) { this.userPrivateCode = userPrivateCode; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public String getEventTitle() { return eventTitle; }

    public void setEventTitle(String eventTitle) { this.eventTitle = eventTitle; }

    public Integer getMedicationId() { return medicationId; }

    public void setMedicationId(Integer medicationId) { this.medicationId = medicationId; }

    public Integer getContactId() { return contactId; }

    public void setContactId(Integer contactId) { this.contactId = contactId; }

    public String getDeliveryType() { return deliveryType; }

    public void setDeliveryType(String deliveryType) { this.deliveryType = deliveryType; }

    public String getStartDate() { return startDate; }

    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }

    public void setEndDate(String endDate) { this.endDate = endDate; }

    public String getFrequency() { return frequency; }

    public void setFrequency(String frequency) { this.frequency = frequency; }

    public String getAlarmType() { return alarmType; }

    public void setAlarmType(String alarmType) { this.alarmType = alarmType; }

    public String getTimeSchedule() { return timeSchedule; }

    public void setTimeSchedule(String timeSchedule) { this.timeSchedule = timeSchedule; }

    public Integer getNoOfPills() { return noOfPills; }

    public void setNoOfPills(Integer noOfPills) { this.noOfPills = noOfPills; }

    public String getError() { return error; }

    public void setError(String error) { this.error = error; }

    public String getResult() { return result; }

    public void setResult(String result) { this.result = result; }


    public Integer getFollowupId() { return followupId; }

    public void setFollowupId(Integer followupId) { this.followupId = followupId; }


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
        dest.writeString(userPrivateCode);
        dest.writeString(type);
        dest.writeString(eventTitle);
        if (medicationId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(medicationId);
        }
        if (contactId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(contactId);
        }
        dest.writeString(deliveryType);
        dest.writeString(startDate);
        dest.writeString(endDate);
        dest.writeString(frequency);
        dest.writeString(alarmType);
        dest.writeString(timeSchedule);
        if (noOfPills == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(noOfPills);
        }
        dest.writeString(error);
        dest.writeString(result);
        if (followupId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(followupId);
        }
    }
}