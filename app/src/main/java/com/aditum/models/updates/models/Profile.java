package com.aditum.models.updates.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//********************************************
public class Profile implements Parcelable
//********************************************
{

    @SerializedName("id")
    @Expose
    private Integer id;
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
    private long dateOfBirth;
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
    private Double height;
    @SerializedName("height_unit")
    @Expose
    private String heightUnit;
    @SerializedName("weight")
    @Expose
    private Double weight;
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
    @SerializedName("medical_conditions")
    @Expose
    private String medicalConditions;
    @SerializedName("allergies")
    @Expose
    private String allergies;
    @SerializedName("is_assistant_active")
    @Expose
    private Integer isAssistantActive;
    @SerializedName("ad_amount")
    @Expose
    private Integer adAmount;
    @SerializedName("ad_currency")
    @Expose
    private String adCurrency;
    @SerializedName("ad_visible")
    @Expose
    private Integer adVisible;
    @SerializedName("updated_at")
    @Expose
    private long updatedAt;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("registeredDateTime")
    @Expose
    private long registeredDateTime;
    @SerializedName("updatedDateTime")
    @Expose
    private long updatedDateTime;

    public Profile() {
    }

    protected Profile(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        dateOfBirth = in.readLong();
        gender = in.readString();
        prefLang = in.readString();
        bloodType = in.readString();
        organDonor = in.readString();
        addressLine1 = in.readString();
        addressLine2 = in.readString();
        addressPobox = in.readString();
        addressCountry = in.readString();
        addressProvince = in.readString();
        addressCity = in.readString();
        phone = in.readString();
        if (in.readByte() == 0) {
            height = null;
        } else {
            height = in.readDouble();
        }
        heightUnit = in.readString();
        if (in.readByte() == 0) {
            weight = null;
        } else {
            weight = in.readDouble();
        }
        weightUnit = in.readString();
        healthCardNumber = in.readString();
        privateInsurance = in.readString();
        privateInsuranceNumber = in.readString();
        medicalConditions = in.readString();
        allergies = in.readString();
        if (in.readByte() == 0) {
            isAssistantActive = null;
        } else {
            isAssistantActive = in.readInt();
        }
        if (in.readByte() == 0) {
            adAmount = null;
        } else {
            adAmount = in.readInt();
        }
        adCurrency = in.readString();
        if (in.readByte() == 0) {
            adVisible = null;
        } else {
            adVisible = in.readInt();
        }
        updatedAt = in.readLong();
        avatar = in.readString();
        registeredDateTime = in.readLong();
        updatedDateTime = in.readLong();
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(email);
        dest.writeLong(dateOfBirth);
        dest.writeString(gender);
        dest.writeString(prefLang);
        dest.writeString(bloodType);
        dest.writeString(organDonor);
        dest.writeString(addressLine1);
        dest.writeString(addressLine2);
        dest.writeString(addressPobox);
        dest.writeString(addressCountry);
        dest.writeString(addressProvince);
        dest.writeString(addressCity);
        dest.writeString(phone);
        if (height == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(height);
        }
        dest.writeString(heightUnit);
        if (weight == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(weight);
        }
        dest.writeString(weightUnit);
        dest.writeString(healthCardNumber);
        dest.writeString(privateInsurance);
        dest.writeString(privateInsuranceNumber);
        dest.writeString(medicalConditions);
        dest.writeString(allergies);
        if (isAssistantActive == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(isAssistantActive);
        }
        if (adAmount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(adAmount);
        }
        dest.writeString(adCurrency);
        if (adVisible == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(adVisible);
        }
        dest.writeLong(updatedAt);
        dest.writeString(avatar);
        dest.writeLong(registeredDateTime);
        dest.writeLong(updatedDateTime);
    }
}
