package com.fantechlabs.lailaa.models.response_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FollowUpResponse
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("followup_id")
    @Expose
    private Integer followupId;

    public Integer getStatus() { return status; }

    public void setStatus(Integer status) { this.status = status; }

    public String getMsg() { return msg; }

    public void setMsg(String msg) { this.msg = msg; }

    public Integer getFollowupId() { return followupId; }

    public void setFollowupId(Integer followupId) { this.followupId = followupId; }

}
