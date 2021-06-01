package com.fantechlabs.lailaa.models.updates.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Followup {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("medication_id")
    @Expose
    private Integer medicationId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("log_DateTime")
    @Expose
    private Integer logDateTime;

}