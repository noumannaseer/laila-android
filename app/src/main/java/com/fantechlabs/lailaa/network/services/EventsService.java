package com.fantechlabs.lailaa.network.services;


import com.fantechlabs.lailaa.models.Events;
import com.fantechlabs.lailaa.models.response_models.MedicineEventResponse;
import com.fantechlabs.lailaa.request_models.AddMedicineEventRequest;
import com.fantechlabs.lailaa.request_models.EventRequest;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface EventsService
{

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("updateevents/1")
    Call<MedicineEventResponse>
    addMedicineEvents(
            @Body AddMedicineEventRequest addMedicineEventRequest
            );
    //**************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("updateevent/1")
    Call<EventRequest>
    addEvent(
            @Body EventRequest eventRequest
            );
    //**************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("deleteevent/1")
    Call<Events>
    deleteEvent(
            @Body Map<String, Object> deleteEvent
    );
    //**************************************************************

}
