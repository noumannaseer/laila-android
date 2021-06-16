package com.aditum.bodyreading.repository.storge.requestmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//***************************************************
public class AddHealthDataRequest
//***************************************************
{

    @SerializedName("user_private_code")
    @Expose
    private String userPrivateCode;
    @SerializedName("device_type")
    @Expose
    private String deviceType;
    @SerializedName("app_type")
    @Expose
    private String appType;
    @SerializedName("heart_rate")
    @Expose
    private Integer heartRate;
    @SerializedName("bp_systolic")
    @Expose
    private Integer bpSystolic;
    @SerializedName("bp_diastolic")
    @Expose
    private Integer bpDiastolic;
    @SerializedName("blood_sugar")
    @Expose
    private Integer bloodSugar;

    //******************************************************
    public static AddHealthDataRequest Builder()
    //******************************************************
    {
        return new AddHealthDataRequest();
    }

    public AddHealthDataRequest setUserPrivateCode(String userPrivateCode) { this.userPrivateCode = userPrivateCode;return this; }

    public AddHealthDataRequest setDeviceType(String deviceType) { this.deviceType = deviceType;return this; }

    public AddHealthDataRequest setAppType(String appType) { this.appType = appType;return this; }

    public AddHealthDataRequest setHeartRate(Integer heartRate) { this.heartRate = heartRate;return this; }

    public AddHealthDataRequest setBpSystolic(Integer bpSystolic) { this.bpSystolic = bpSystolic;return this; }

    public AddHealthDataRequest setBpDiastolic(Integer bpDiastolic) { this.bpDiastolic = bpDiastolic;return this; }

    public AddHealthDataRequest setBloodSugar(Integer bloodSugar) { this.bloodSugar = bloodSugar; return this; }
}
