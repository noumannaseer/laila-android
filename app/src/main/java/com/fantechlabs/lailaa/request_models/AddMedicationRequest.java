package com.fantechlabs.lailaa.request_models;

import java.util.List;

import lombok.Getter;

//******************************************************
@Getter
public class AddMedicationRequest
//******************************************************
{
    private int id;
    private String medicationName;
    private String medicationDin;
    private String medicationStrength;
    private String medicationStrengthUnit;
    private String from;
    private String dispensedDate;
    private String dispensedAmount;
    private String dosage;
    private String noOfRefills;
    private String frequency;
    private String pharmacy;
    private String refillDate;
    private String number_of_pills;
    private int prescribed;
    private int deliveryType;
    private boolean is_update;
    private List<String> intakeTimeList;


    public com.fantechlabs.lailaa.request_models.AddMedicationRequest setIntakeTimeList(List<String> intakeTime)
    {
        this.intakeTimeList = intakeTime;
        return this;
    }

    //******************************************************
    public static AddMedicationRequest Builder()
    //******************************************************
    {
        return new AddMedicationRequest();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public void setMedicationDin(String medicationDin) {
        this.medicationDin = medicationDin;
    }

    public void setMedicationStrength(String medicationStrength) {
        this.medicationStrength = medicationStrength;
    }

    public void setMedicationStrengthUnit(String medicationStrengthUnit) {
        this.medicationStrengthUnit = medicationStrengthUnit;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setDispensedDate(String dispensedDate) {
        this.dispensedDate = dispensedDate;
    }

    public void setDispensedAmount(String dispensedAmount) {
        this.dispensedAmount = dispensedAmount;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public void setNoOfRefills(String noOfRefills) {
        this.noOfRefills = noOfRefills;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public void setPharmacy(String pharmacy) {
        this.pharmacy = pharmacy;
    }

    public void setRefillDate(String refillDate) {
        this.refillDate = refillDate;
    }

    public void setNumber_of_pills(String number_of_pills) {
        this.number_of_pills = number_of_pills;
    }

    public void setPrescribed(int prescribed) {
        this.prescribed = prescribed;
    }

    public void setDeliveryType(int deliveryType) {
        this.deliveryType = deliveryType;
    }

    public void setIs_update(boolean is_update) {
        this.is_update = is_update;
    }
}
