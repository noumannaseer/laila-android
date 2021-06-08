package com.fantechlabs.lailaa.models.updates.request_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmergencyContactRequest {
    @SerializedName("id")
    @Expose
    private String contactId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("contact_type")
    @Expose
    private String contactType;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("token")
    @Expose
    private String token;

    //******************************************************
    public static EmergencyContactRequest Builder()
    //******************************************************
    {
        return new EmergencyContactRequest();
    }


}
