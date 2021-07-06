package com.aditum.request_models;

import com.aditum.models.updates.request_models.AddMedicationRequest;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QuestionsRequest {

    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("questions")
    @Expose
    private List<String> questions = null;
    @SerializedName("token")
    @Expose
    private String token;

    //******************************************************
    public static QuestionsRequest Builder()
    //******************************************************
    {
        return new QuestionsRequest();
    }
}
