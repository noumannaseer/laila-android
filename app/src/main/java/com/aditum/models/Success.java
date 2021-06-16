package com.aditum.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Success {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("user_private_code")
    @Expose
    private String userPrivateCode;
    @SerializedName("mandatory_fields")
    @Expose
    private String mandatoryFields;
    @SerializedName("contact_type")
    @Expose
    private String contactType;
    @SerializedName("images")
    @Expose
    private ProfileImages images;


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getUserPrivateCode() {
        return userPrivateCode;
    }

    public void setUserPrivateCode(String userPrivateCode) { this.userPrivateCode = userPrivateCode; }

    public String getMandatoryFields() {
        return mandatoryFields;
    }

    public void setMandatoryFields(String mandatoryFields) { this.mandatoryFields = mandatoryFields; }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public ProfileImages getImages() { return images; }

    public void setImages(ProfileImages images) { this.images = images; }
}
