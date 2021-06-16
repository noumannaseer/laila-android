package com.aditum.models.response_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InformationResponse
{

    @SerializedName("code")
    @Expose
    private int code;
    @SerializedName("drug_identification_number")
    @Expose
    private String drugIdentificationNumber;
    @SerializedName("current_status")
    @Expose
    private String currentStatus;
    @SerializedName("current_status_date")
    @Expose
    private String currentStatusDate;
    @SerializedName("original_market_date")
    @Expose
    private String originalMarketDate;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("company_details")
    @Expose
    private String companyDetails;
    @SerializedName("drug_class")
    @Expose
    private String drugClass;
    @SerializedName("dosage_form")
    @Expose
    private String dosageForm;
    @SerializedName("route_of_administration")
    @Expose
    private String routeOfAdministration;
    @SerializedName("number_of_ais")
    @Expose
    private String numberOfAis;
    @SerializedName("ai_group_no")
    @Expose
    private String aiGroupNo;
    @SerializedName("schedules")
    @Expose
    private String schedules;
    @SerializedName("ahfs_data")
    @Expose
    private String ahfsData;
    @SerializedName("atc_data")
    @Expose
    private String atcData;
    @SerializedName("ingredients_data")
    @Expose
    private List<MedicineInformation> ingredientsData = null;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("err")
    @Expose
    private String err;

    public int getCode() { return code; }

    public void setCode(int code) { this.code = code; }

    public String getDrugIdentificationNumber() { return drugIdentificationNumber; }

    public void setDrugIdentificationNumber(String drugIdentificationNumber) { this.drugIdentificationNumber = drugIdentificationNumber; }

    public String getCurrentStatus() { return currentStatus; }

    public void setCurrentStatus(String currentStatus) { this.currentStatus = currentStatus; }

    public String getCurrentStatusDate() { return currentStatusDate; }

    public void setCurrentStatusDate(String currentStatusDate) { this.currentStatusDate = currentStatusDate; }

    public String getOriginalMarketDate() { return originalMarketDate; }

    public void setOriginalMarketDate(String originalMarketDate) { this.originalMarketDate = originalMarketDate; }

    public String getProductName() { return productName; }

    public void setProductName(String productName) { this.productName = productName; }

    public String getCompanyDetails() { return companyDetails; }

    public void setCompanyDetails(String companyDetails) { this.companyDetails = companyDetails; }

    public String getDrugClass() { return drugClass; }

    public void setDrugClass(String drugClass) { this.drugClass = drugClass; }

    public String getDosageForm() { return dosageForm; }

    public void setDosageForm(String dosageForm) { this.dosageForm = dosageForm; }

    public String getRouteOfAdministration() { return routeOfAdministration; }

    public void setRouteOfAdministration(String routeOfAdministration) { this.routeOfAdministration = routeOfAdministration; }

    public String getNumberOfAis() { return numberOfAis; }

    public void setNumberOfAis(String numberOfAis) { this.numberOfAis = numberOfAis; }

    public String getAiGroupNo() { return aiGroupNo; }

    public void setAiGroupNo(String aiGroupNo) { this.aiGroupNo = aiGroupNo; }

    public String getSchedules() { return schedules; }

    public void setSchedules(String schedules) { this.schedules = schedules; }

    public String getAhfsData() { return ahfsData; }

    public void setAhfsData(String ahfsData) { this.ahfsData = ahfsData; }

    public String getAtcData() { return atcData; }

    public void setAtcData(String atcData) { this.atcData = atcData; }

    public String getMsg() { return msg; }

    public void setMsg(String msg) { this.msg = msg; }

    public List<MedicineInformation> getIngredientsData() { return ingredientsData; }

    public void setIngredientsData(List<MedicineInformation> ingredientsData) { this.ingredientsData = ingredientsData; }

    public String getErr() { return err; }

    public void setErr(String err) { this.err = err; }
}
