package com.aditum.bodyreading.repository.storge.requestmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//***************************************************
public class ReadHealthDataRequest
//***************************************************
{

    @SerializedName("user_private_code")
    @Expose
    private String userPrivateCode;
    @SerializedName("days")
    @Expose
    private int days;
    @SerializedName("avg_days")
    @Expose
    private int avgDays;

    @Override
    public String toString() {
        return "ReadHealthDataRequest{" +
                "userPrivateCode='" + userPrivateCode + '\'' +
                ", days=" + days +
                '}';
    }

    //******************************************************
    public static ReadHealthDataRequest Builder()
    //******************************************************
    {
        return new ReadHealthDataRequest();
    }

    public ReadHealthDataRequest setUserPrivateCode(String userPrivateCode) {
        this.userPrivateCode = userPrivateCode;
        return this;
    }

    public ReadHealthDataRequest setDays(int days) {
        this.days = days;
        return this;
    }

    public ReadHealthDataRequest setAvg_days(int avg_days) {
        this.avgDays = avg_days;
        return this;
    }
}
