package com.fantechlabs.lailaa.bodyreading.repository.storge.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


//******************************************************
public class HealthData implements Parcelable
//******************************************************
{

    @SerializedName("from")
    @Expose
    private Integer from;
    @SerializedName("to")
    @Expose
    private Integer to;
    @SerializedName("max_heart_rate")
    @Expose
    private Integer maxHeartRate;
    @SerializedName("min_heart_rate")
    @Expose
    private Integer minHeartRate;
    @SerializedName("max_bp_systolic")
    @Expose
    private Integer maxBpSystolic;
    @SerializedName("min_bp_systolic")
    @Expose
    private Integer minBpSystolic;
    @SerializedName("max_bp_diastolic")
    @Expose
    private Integer maxBpDiastolic;
    @SerializedName("min_bp_diastolic")
    @Expose
    private Integer minBpDiastolic;
    @SerializedName("max_glucose")
    @Expose
    private Integer maxGlucose;
    @SerializedName("min_glucose")
    @Expose
    private Integer minGlucose;


    protected HealthData(Parcel in) {
        if (in.readByte() == 0) {
            from = null;
        } else {
            from = in.readInt();
        }
        if (in.readByte() == 0) {
            to = null;
        } else {
            to = in.readInt();
        }
        if (in.readByte() == 0) {
            maxHeartRate = null;
        } else {
            maxHeartRate = in.readInt();
        }
        if (in.readByte() == 0) {
            minHeartRate = null;
        } else {
            minHeartRate = in.readInt();
        }
        if (in.readByte() == 0) {
            maxBpSystolic = null;
        } else {
            maxBpSystolic = in.readInt();
        }
        if (in.readByte() == 0) {
            minBpSystolic = null;
        } else {
            minBpSystolic = in.readInt();
        }
        if (in.readByte() == 0) {
            maxBpDiastolic = null;
        } else {
            maxBpDiastolic = in.readInt();
        }
        if (in.readByte() == 0) {
            minBpDiastolic = null;
        } else {
            minBpDiastolic = in.readInt();
        }
        if (in.readByte() == 0) {
            maxGlucose = null;
        } else {
            maxGlucose = in.readInt();
        }
        if (in.readByte() == 0) {
            minGlucose = null;
        } else {
            minGlucose = in.readInt();
        }
    }

    public static final Creator<HealthData> CREATOR = new Creator<HealthData>() {
        @Override
        public HealthData createFromParcel(Parcel in) {
            return new HealthData(in);
        }

        @Override
        public HealthData[] newArray(int size) {
            return new HealthData[size];
        }
    };

    public Integer getFrom() { return from; }

    public void setFrom(Integer from) { this.from = from; }

    public Integer getTo() { return to; }

    public void setTo(Integer to) { this.to = to; }

    public Integer getMaxHeartRate() { return maxHeartRate; }

    public void setMaxHeartRate(Integer maxHeartRate) { this.maxHeartRate = maxHeartRate; }

    public Integer getMinHeartRate() { return minHeartRate; }

    public void setMinHeartRate(Integer minHeartRate) { this.minHeartRate = minHeartRate; }

    public Integer getMaxBpSystolic() { return maxBpSystolic; }

    public void setMaxBpSystolic(Integer maxBpSystolic) { this.maxBpSystolic = maxBpSystolic; }

    public Integer getMinBpSystolic() { return minBpSystolic; }

    public void setMinBpSystolic(Integer minBpSystolic) { this.minBpSystolic = minBpSystolic; }

    public Integer getMaxBpDiastolic() { return maxBpDiastolic; }

    public void setMaxBpDiastolic(Integer maxBpDiastolic) { this.maxBpDiastolic = maxBpDiastolic; }

    public Integer getMinBpDiastolic() { return minBpDiastolic; }

    public void setMinBpDiastolic(Integer minBpDiastolic) { this.minBpDiastolic = minBpDiastolic; }

    public Integer getMaxGlucose() { return maxGlucose; }

    public void setMaxGlucose(Integer maxGlucose) { this.maxGlucose = maxGlucose; }

    public Integer getMinGlucose() { return minGlucose; }

    public void setMinGlucose(Integer minGlucose) { this.minGlucose = minGlucose; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (from == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(from);
        }
        if (to == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(to);
        }
        if (maxHeartRate == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(maxHeartRate);
        }
        if (minHeartRate == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(minHeartRate);
        }
        if (maxBpSystolic == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(maxBpSystolic);
        }
        if (minBpSystolic == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(minBpSystolic);
        }
        if (maxBpDiastolic == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(maxBpDiastolic);
        }
        if (minBpDiastolic == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(minBpDiastolic);
        }
        if (maxGlucose == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(maxGlucose);
        }
        if (minGlucose == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(minGlucose);
        }
    }
}
