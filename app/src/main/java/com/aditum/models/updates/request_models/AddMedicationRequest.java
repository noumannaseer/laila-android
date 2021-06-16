package com.aditum.models.updates.request_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//*******************************************************
public class AddMedicationRequest
//*******************************************************
{
    private int id;
    @SerializedName("medication_name")
    @Expose
    private String medicationName;
    @SerializedName("din_rx_number")
    @Expose
    private String dinRxNumber;
    @SerializedName("medecine_form")
    @Expose
    private String medecineForm;
    @SerializedName("strength")
    @Expose
    private Integer strength;
    @SerializedName("strength_uom")
    @Expose
    private String strengthUom;
    @SerializedName("prescribed")
    @Expose
    private Integer prescribed;
    @SerializedName("dispensed_date")
    @Expose
    private String dispensedDate;
    @SerializedName("dispensed_amount")
    @Expose
    private String dispensedAmount;
    @SerializedName("dosage")
    @Expose
    private Integer dosage;
    @SerializedName("frequency")
    @Expose
    private String frequency;
    @SerializedName("pharmacy_id")
    @Expose
    private Integer pharmacyId;
    @SerializedName("refill_date")
    @Expose
    private String refillDate;
    @SerializedName("num_refills")
    @Expose
    private Integer numRefills;
    @SerializedName("token")
    @Expose
    private String token;

    private List<String> intakeTimeList;


    //******************************************************
    public static AddMedicationRequest Builder()
    //******************************************************
    {
        return new AddMedicationRequest();
    }

}
