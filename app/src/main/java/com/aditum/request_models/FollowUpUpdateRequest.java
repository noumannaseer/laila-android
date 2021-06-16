package com.aditum.request_models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
//******************************************************
public class FollowUpUpdateRequest
//******************************************************
{

    @SerializedName("followup_id")
    @Expose
    private String followupId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("medication_id")
    @Expose
    private String medicationId;
    @SerializedName("log_DateTime")
    @Expose
    private String logDateTime;
    @SerializedName("status")
    @Expose
    private String status;

    //******************************************************
    public static FollowUpUpdateRequest Builder()
    //******************************************************
    {
        return new FollowUpUpdateRequest();
    }

}