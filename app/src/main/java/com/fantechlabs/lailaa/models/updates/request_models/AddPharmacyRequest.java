package com.fantechlabs.lailaa.models.updates.request_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//****************************************************
public class AddPharmacyRequest
//****************************************************
{
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("contact_type")
    @Expose
    private String contactType;
    @SerializedName("contact_no")
    @Expose
    private String contactNo;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("address2")
    @Expose
    private String address2;
    @SerializedName("province")
    @Expose
    private String province;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("zip_code")
    @Expose
    private String zipCode;
    @SerializedName("is_preferred")
    @Expose
    private String isPreferred;
    @SerializedName("token")
    @Expose
    private String token;


    //******************************************************
    public static AddPharmacyRequest Builder()
    //******************************************************
    {
        return new AddPharmacyRequest();
    }

}
