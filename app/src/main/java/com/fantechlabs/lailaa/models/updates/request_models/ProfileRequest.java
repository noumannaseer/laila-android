package com.fantechlabs.lailaa.models.updates.request_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//*********************************************
public class ProfileRequest
//*********************************************
{
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("date_of_birth")
    @Expose
    private String dateOfBirth;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("pref_lang")
    @Expose
    private String prefLang;
    @SerializedName("blood_type")
    @Expose
    private String bloodType;
    @SerializedName("organ_donor")
    @Expose
    private String organDonor;
    @SerializedName("address_line1")
    @Expose
    private String addressLine1;
    @SerializedName("address_line2")
    @Expose
    private String addressLine2;
    @SerializedName("address_pobox")
    @Expose
    private String addressPobox;
    @SerializedName("address_country")
    @Expose
    private String addressCountry;
    @SerializedName("address_province")
    @Expose
    private String addressProvince;
    @SerializedName("address_city")
    @Expose
    private String addressCity;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("height")
    @Expose
    private String height;
    @SerializedName("height_unit")
    @Expose
    private String heightUnit;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("weight_unit")
    @Expose
    private String weightUnit;
    @SerializedName("health_card_number")
    @Expose
    private String healthCardNumber;
    @SerializedName("private_insurance")
    @Expose
    private String privateInsurance;
    @SerializedName("private_insurance_number")
    @Expose
    private String privateInsuranceNumber;
    @SerializedName("allergies")
    @Expose
    private String allergies;
    @SerializedName("medical_conditions")
    @Expose
    private String medicalConditions;
    @SerializedName("is_assistant_active")
    @Expose
    private String isAssistantActive;
    @SerializedName("ad_amount")
    @Expose
    private String adAmount;
    @SerializedName("ad_currency")
    @Expose
    private String adCurrency;
    @SerializedName("ad_visible")
    @Expose
    private String adVisible;
    @SerializedName("token")
    @Expose
    private String token;

}
