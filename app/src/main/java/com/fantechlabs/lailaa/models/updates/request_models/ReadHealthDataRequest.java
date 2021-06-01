package com.fantechlabs.lailaa.models.updates.request_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//*************************************************
public class ReadHealthDataRequest
//*************************************************
{
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("days")
    @Expose
    private String days;
    @SerializedName("avg_days")
    @Expose
    private String avgDays;
    @SerializedName("token")
    @Expose
    private String token;

    public static ReadHealthDataRequest Builder() {
        return new ReadHealthDataRequest();
    }
}
