package com.fantechlabs.lailaa.models.response_models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MedicineInformationResponse implements Parcelable {

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
    private String numberOfAis;
    @SerializedName("ai_group_no")
    @Expose
    private String aiGroupNo;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("last_update_date")
    @Expose
    private String lastUpdateDate;

    protected MedicineInformationResponse(Parcel in) {
        if (in.readByte() == 0) {
            drugCode = null;
        } else {
            drugCode = in.readInt();
        }
        className = in.readString();
        drugIdentificationNumber = in.readString();
        brandName = in.readString();
        descriptor = in.readString();
        numberOfAis = in.readString();
        aiGroupNo = in.readString();
        companyName = in.readString();
        lastUpdateDate = in.readString();
    }

    public static final Creator<MedicineInformationResponse> CREATOR = new Creator<MedicineInformationResponse>() {
        @Override
        public MedicineInformationResponse createFromParcel(Parcel in) {
            return new MedicineInformationResponse(in);
        }

        @Override
        public MedicineInformationResponse[] newArray(int size) {
            return new MedicineInformationResponse[size];
        }
    };

    public Integer getDrugCode() {
        return drugCode;
    }

    public void setDrugCode(Integer drugCode) {
        this.drugCode = drugCode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDrugIdentificationNumber() {
        return drugIdentificationNumber;
    }

    public void setDrugIdentificationNumber(String drugIdentificationNumber) {
        this.drugIdentificationNumber = drugIdentificationNumber;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    public String getNumberOfAis() {
        return numberOfAis;
    }

    public void setNumberOfAis(String numberOfAis) {
        this.numberOfAis = numberOfAis;
    }

    public String getAiGroupNo() {
        return aiGroupNo;
    }

    public void setAiGroupNo(String aiGroupNo) {
        this.aiGroupNo = aiGroupNo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (drugCode == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(drugCode);
        }
        parcel.writeString(className);
        parcel.writeString(drugIdentificationNumber);
        parcel.writeString(brandName);
        parcel.writeString(descriptor);
        parcel.writeString(numberOfAis);
        parcel.writeString(aiGroupNo);
        parcel.writeString(companyName);
        parcel.writeString(lastUpdateDate);
    }
}