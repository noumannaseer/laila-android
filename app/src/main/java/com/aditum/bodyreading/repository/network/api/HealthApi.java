package com.aditum.bodyreading.repository.network.api;

import com.aditum.models.updates.request_models.AddHealthDataRequest;
import com.aditum.models.updates.request_models.ReadHealthDataRequest;
import com.aditum.models.updates.response_models.AddHealthDataResponse;
import com.aditum.models.updates.response_models.ReadHealthDataResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface HealthApi {

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("bodyvital/add_bodyvital")
    Call<AddHealthDataResponse>
    addData(
            @Body AddHealthDataRequest addHealthDataRequest
    );
    //**************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("bodyvital/read_bodyvital")
    Call<ReadHealthDataResponse>
    readData(
            @Body ReadHealthDataRequest readHealthDataRequest
    );

}
