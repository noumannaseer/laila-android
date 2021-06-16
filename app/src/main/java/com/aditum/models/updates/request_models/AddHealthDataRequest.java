package com.aditum.models.updates.request_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//****************************************************
public class AddHealthDataRequest
//****************************************************
{
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("heart_rate")
    @Expose
    private String heartRate;
    @SerializedName("bp_systolic")
    @Expose
    private String bpSystolic;
    @SerializedName("bp_diastolic")
    @Expose
    private String bpDiastolic;
    @SerializedName("blood_sugar")
    @Expose
    private String bloodSugar;
    @SerializedName("token")
    @Expose
    private String token;

    public static AddHealthDataRequest Builder() {
        return new AddHealthDataRequest();
    }
}
