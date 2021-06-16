package com.aditum.models.response_models;

 import com.aditum.models.Success;
 import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignUpResponse
{
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("contact_type")
    @Expose
    private String contactType;
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("user_private_code")
    @Expose
    private String userPrivateCode;
    @SerializedName("mandatory_fields")
    @Expose
    private String mandatoryFields;
    @SerializedName("success")
    @Expose
    private Success success;
    @SerializedName("status_code")
    @Expose
    private int status_code;

    public String getError()
    { return error; }

    public void setError(String error)
    { this.error = error; }

    public String getContactType()
    {
        return contactType;
    }

    public void setContactType(String contactType)
    {
        this.contactType = contactType;
    }

    public String getResult()
    {
        return result;
    }

    public void setResult(String result)
    {
        this.result = result;
    }

    public String getUserPrivateCode()
    {
        return userPrivateCode;
    }

    public void setUserPrivateCode(String userPrivateCode) { this.userPrivateCode = userPrivateCode; }

    public String getMandatoryFields()
    {
        return mandatoryFields;
    }

    public void setMandatoryFields(String mandatoryFields) { this.mandatoryFields = mandatoryFields; }

    public Success getSuccess() { return success; }

    public void setSuccess(Success success) { this.success = success; }

    public int getStatus_code() { return status_code; }

    public void setStatus_code(int status_code) { this.status_code = status_code; }
}

