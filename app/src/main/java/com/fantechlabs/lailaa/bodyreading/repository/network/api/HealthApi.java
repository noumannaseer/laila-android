package com.fantechlabs.lailaa.bodyreading.repository.network.api;

import com.fantechlabs.lailaa.bodyreading.repository.storge.requestmodel.AddHealthDataRequest;
import com.fantechlabs.lailaa.bodyreading.repository.storge.requestmodel.ReadHealthDataRequest;
import com.fantechlabs.lailaa.bodyreading.repository.storge.responsemodel.AddDataHealthResponse;
import com.fantechlabs.lailaa.bodyreading.repository.storge.responsemodel.HealthDataReadingResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface HealthApi {

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("body_readings/add")
    Call<AddDataHealthResponse>
    addData(
            @Body AddHealthDataRequest addHealthDataRequest
    );
    //**************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("body_readings/read")
    Call<HealthDataReadingResponse>
    readData(
            @Body ReadHealthDataRequest readHealthDataRequest
    );

}
