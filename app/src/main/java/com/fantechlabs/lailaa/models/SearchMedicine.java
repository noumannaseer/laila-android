package com.fantechlabs.lailaa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchMedicine
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
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("upc")
    @Expose
    private String upc;

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

    public String getError() { return error; }

    public void setError(String error) { this.error = error; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getUpc() { return upc; }

    public void setUpc(String upc) { this.upc = upc; }
}