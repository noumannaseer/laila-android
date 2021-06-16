package com.aditum.models.response_models;

import com.aditum.models.SearchMedicine;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SearchMedicationResponse
{

    @SerializedName("medication")
    @Expose
    private ArrayList<SearchMedicine> searchMedication;
    @SerializedName("error")
    @Expose
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public ArrayList<SearchMedicine> getSearchMedication() { return searchMedication; }

    public void setSearchMedication(ArrayList<SearchMedicine> searchMedication) { this.searchMedication = searchMedication; }
}