package com.aditum.network.services;


import com.aditum.models.response_models.EventResponse;
import com.aditum.models.updates.response_models.AddEventResponse;
import com.aditum.models.updates.response_models.GetEventsResponse;
import com.aditum.request_models.EventRequest;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface EventsService {

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("medications/events")
    Call<AddEventResponse>
    addMedicineEvents(
            @Body Map<String, Object> addMedicineEvents
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
    @POST("medications/delete_event")
    Call<EventResponse>
    deleteEvent(
            @Body Map<String, Object> deleteEvent
    );

    //**************************************************************
    //**************************************************************
    @Headers("Accept: application/json")
    @POST("medications/get_events")
    Call<GetEventsResponse>
    getEvents(
            @Body HashMap<String, String> getEvents
    );

    //**************************************************************
}
