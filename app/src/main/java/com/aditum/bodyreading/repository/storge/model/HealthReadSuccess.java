package com.aditum.bodyreading.repository.storge.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


//************************************************
public class HealthReadSuccess
    implements Parcelable
//************************************************
{
    @SerializedName("result")
    @Expose
    private ArrayList<Health> result;
    @SerializedName("averages")
    @Expose
    private Averages averages;

    public HealthReadSuccess(){}

    protected HealthReadSuccess(Parcel in) {
        result = in.createTypedArrayList(Health.CREATOR);
        averages = in.readParcelable(Averages.class.getClassLoader());
    }

    public static final Creator<HealthReadSuccess> CREATOR = new Creator<HealthReadSuccess>() {
        @Override
        public HealthReadSuccess createFromParcel(Parcel in) {
            return new HealthReadSuccess(in);
        }

        @Override
        public HealthReadSuccess[] newArray(int size) {
            return new HealthReadSuccess[size];
        }
    };

    public ArrayList<Health> getResult() { return result; }

    public void setResult(ArrayList<Health> result) { this.result = result; }

    public Averages getAverages() { return averages; }

    public void setAverages(Averages averages) { this.averages = averages; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(result);
        dest.writeParcelable(averages, flags);
    }
}
