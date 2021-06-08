package com.fantechlabs.lailaa.models.updates.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//**********************************************
public class SearchMedication implements Parcelable
//**********************************************
{
    @SerializedName("drug_code")
    @Expose
    private Integer drugCode;
    @SerializedName("class_name")
    @Expose
    private String className;
    @SerializedName("drug_identification_number")
    @Expose
    private String drugIdentificationNumber;
    @SerializedName("brand_name")
    @Expose
    private String brandName;
    @SerializedName("descriptor")
    @Expose
    private String descriptor;
    @SerializedName("number_of_ais")
    @Expose
    private Integer numberOfAis;
    @SerializedName("ai_group_no")
    @Expose
    private long aiGroupNo;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("last_update_date")
    @Expose
    private long lastUpdateDate;

    public SearchMedication() {
    }

    protected SearchMedication(Parcel in) {
        if (in.readByte() == 0) {
            drugCode = null;
        } else {
            drugCode = in.readInt();
        }
        className = in.readString();
        drugIdentificationNumber = in.readString();
        brandName = in.readString();
        descriptor = in.readString();
        if (in.readByte() == 0) {
            numberOfAis = null;
        } else {
            numberOfAis = in.readInt();
        }
        aiGroupNo = in.readLong();
        companyName = in.readString();
        lastUpdateDate = in.readLong();
    }

    public static final Creator<SearchMedication> CREATOR = new Creator<SearchMedication>() {
        @Override
        public SearchMedication createFromParcel(Parcel in) {
            return new SearchMedication(in);
        }

        @Override
        public SearchMedication[] newArray(int size) {
            return new SearchMedication[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (drugCode == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(drugCode);
        }
        dest.writeString(className);
        dest.writeString(drugIdentificationNumber);
        dest.writeString(brandName);
        dest.writeString(descriptor);
        if (numberOfAis == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(numberOfAis);
        }
        dest.writeLong(aiGroupNo);
        dest.writeString(companyName);
        dest.writeLong(lastUpdateDate);
    }
}
