package com.fantechlabs.lailaa.bodyreading.repository.storge.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//******************************************
public class Averages
    implements Parcelable
//******************************************
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

    protected Averages(Parcel in) {
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

    public static final Creator<Averages> CREATOR = new Creator<Averages>() {
        @Override
        public Averages createFromParcel(Parcel in) {
            return new Averages(in);
        }

        @Override
        public Averages[] newArray(int size) {
            return new Averages[size];
        }
    };

    public Double getHeartRate() { return heartRate; }

    public void setHeartRate(Double heartRate) { this.heartRate = heartRate; }

    public Double getBpSystolic() { return bpSystolic; }

    public void setBpSystolic(Double bpSystolic) { this.bpSystolic = bpSystolic; }

    public Double getBpDiastolic() { return bpDiastolic; }

    public void setBpDiastolic(Double bpDiastolic) { this.bpDiastolic = bpDiastolic; }

    public Double getBloodSugar() { return bloodSugar; }

    public void setBloodSugar(Double bloodSugar) { this.bloodSugar = bloodSugar; }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (heartRate == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(heartRate);
        }
        if (bpSystolic == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(bpSystolic);
        }
        if (bpDiastolic == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(bpDiastolic);
        }
        if (bloodSugar == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(bloodSugar);
        }
    }
}