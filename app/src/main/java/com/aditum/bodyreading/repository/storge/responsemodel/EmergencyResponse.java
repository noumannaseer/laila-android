package com.aditum.bodyreading.repository.storge.responsemodel;


import com.aditum.bodyreading.repository.storge.model.Contact;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmergencyResponse {
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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
