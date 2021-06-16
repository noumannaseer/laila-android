package com.aditum.models.response_models;

import com.aditum.models.Success;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//***********************************************
public class UpdateProfileResponse
//***********************************************
{

    @SerializedName("status_code")
    @Expose
    private int status_code;
    @SerializedName("missing_required_param")
    @Expose
    private String missing_required_param;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("success")
    @Expose
    private Success success;

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }

    public int getStatus_code() { return status_code; }

    public void setStatus_code(int status_code) { this.status_code = status_code; }

    public String getError() { return error; }

    public void setError(String error) { this.error = error; }

    public String getMissing_required_param() { return missing_required_param; }

    public void setMissing_required_param(String missing_required_param) { this.missing_required_param = missing_required_param; }

    public Success getSuccess() { return success; }

    public void setSuccess(Success success) { this.success = success; }
}
