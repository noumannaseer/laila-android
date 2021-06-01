package com.fantechlabs.lailaa.models.updates.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//*******************************************
public class HealthAverages implements Parcelable
//*******************************************
{
    @SerializedName("heart_rate")
    @Expose
    private Double heartRate;
    @SerializedName("bp_systolic")
    @Expose
    private Double bpSystolic;
    @SerializedName("bp_diastolic")
    @Expose
    private Double bpDiastolic;
    @SerializedName("blood_sugar")
    @Expose
    private Double bloodSugar;

    protected HealthAverages(Parcel in) {
        if (in.readByte() == 0) {
            heartRate = null;
        } else {
            heartRate = in.readDouble();
        }
        if (in.readByte() == 0) {
            bpSystolic = null;
        } else {
            bpSystolic = in.readDouble();
        }
        if (in.readByte() == 0) {
            bpDiastolic = null;
        } else {
            bpDiastolic = in.readDouble();
        }
        if (in.readByte() == 0) {
            bloodSugar = null;
        } else {
            bloodSugar = in.readDouble();
        }
    }

    public static final Creator<HealthAverages> CREATOR = new Creator<HealthAverages>() {
        @Override
        public HealthAverages createFromParcel(Parcel in) {
            return new HealthAverages(in);
        }

        @Override
        public HealthAverages[] newArray(int size) {
            return new HealthAverages[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (heartRate == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(heartRate);
        }
        if (bpSystolic == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(bpSystolic);
        }
        if (bpDiastolic == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(bpDiastolic);
        }
        if (bloodSugar == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(bloodSugar);
        }
    }
}
