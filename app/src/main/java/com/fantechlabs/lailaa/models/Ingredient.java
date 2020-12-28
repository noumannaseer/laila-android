package com.fantechlabs.lailaa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//************************************
public class Ingredient
//************************************
{

    @SerializedName("dosage_unit")
    @Expose
    private String dosageUnit;
    @SerializedName("dosage_value")
    @Expose
    private String dosageValue;
    @SerializedName("drug_code")
    @Expose
    private Integer drugCode;
    @SerializedName("ingredient_name")
    @Expose
    private String ingredientName;
    @SerializedName("strength")
    @Expose
    private String strength;
    @SerializedName("strength_unit")
    @Expose
    private String strengthUnit;

    public String getDosageUnit() {
        return dosageUnit;
    }

    public void setDosageUnit(String dosageUnit) {
        this.dosageUnit = dosageUnit;
    }

    public String getDosageValue() {
        return dosageValue;
    }

    public void setDosageValue(String dosageValue) {
        this.dosageValue = dosageValue;
    }

    public Integer getDrugCode() {
        return drugCode;
    }

    public void setDrugCode(Integer drugCode) {
        this.drugCode = drugCode;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getStrengthUnit() {
        return strengthUnit;
    }

    public void setStrengthUnit(String strengthUnit) {
        this.strengthUnit = strengthUnit;
    }

}
