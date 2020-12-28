package com.fantechlabs.lailaa.models.response_models;

import com.fantechlabs.lailaa.models.Contact;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PharmacyResponse
{
    @SerializedName("contact")
    @Expose
    Contact contact;

    @SerializedName("error")
    @Expose
    private String error;

    @SerializedName("result")
    @Expose
    private String result;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getResult() { return result; }

    public void setResult(String result) { this.result = result; }
}
