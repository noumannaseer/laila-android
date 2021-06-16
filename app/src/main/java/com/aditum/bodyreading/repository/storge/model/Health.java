package com.aditum.bodyreading.repository.storge.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//*****************************************
public class Health
    implements Parcelable
//*****************************************
{

    @SerializedName("heart_rate")
    @Expose
    private int heartRate;
    @SerializedName("bp_systolic")
    @Expose
    private int bpSystolic;
    @SerializedName("bp_diastolic")
    @Expose
    private int bpDiastolic;
    @SerializedName("blood_sugar")
    @Expose
    private int bloodSugar;
    @SerializedName("created")
    @Expose
    private long created;

    public Health(){}

    protected Health(Parcel in) {
        heartRate = in.readInt();
        bpSystolic = in.readInt();
        bpDiastolic = in.readInt();
        bloodSugar = in.readInt();
        created = in.readLong();
    }

    public static final Creator<Health> CREATOR = new Creator<Health>() {
        @Override
        public Health createFromParcel(Parcel in) {
            return new Health(in);
        }

        @Override
        public Health[] newArray(int size) {
            return new Health[size];
        }
    };

    public void addHeartRate(int value)
    {
        heartRate = heartRate + value;
    }

    public void addBpSystolic(int value)
    {
        bpSystolic = bpSystolic + value;
    }

    public void addBpDiastolic(int value)
    {
        bpDiastolic = bpDiastolic + value;
    }

    public void addBloodSugar(int value)
    {
        bloodSugar = bloodSugar + value;
    }


    public int getHeartRate() { return heartRate; }

    public void setHeartRate(int heartRate) { this.heartRate = heartRate; }

    public int getBpSystolic() { return bpSystolic; }

    public void setBpSystolic(int bpSystolic) { this.bpSystolic = bpSystolic; }

    public int getBpDiastolic() { return bpDiastolic; }

    public void setBpDiastolic(int bpDiastolic) { this.bpDiastolic = bpDiastolic; }

    public int getBloodSugar() { return bloodSugar; }

    public void setBloodSugar(int bloodSugar) { this.bloodSugar = bloodSugar; }

    public long getCreated() { return created; }

    public void setCreated(long created) { this.created = created; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(heartRate);
        dest.writeInt(bpSystolic);
        dest.writeInt(bpDiastolic);
        dest.writeInt(bloodSugar);
        dest.writeLong(created);
    }
}