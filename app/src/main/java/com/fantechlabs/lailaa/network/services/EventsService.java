package com.fantechlabs.lailaa.network.services;


import com.fantechlabs.lailaa.models.Events;
import com.fantechlabs.lailaa.models.response_models.EventResponse;
import com.fantechlabs.lailaa.models.response_models.MedicineEventResponse;
import com.fantechlabs.lailaa.models.updates.response_models.AddEventResponse;
import com.fantechlabs.lailaa.models.updates.response_models.GetEventsResponse;
import com.fantechlabs.lailaa.request_models.AddMedicineEventRequest;
import com.fantechlabs.lailaa.request_models.EventRequest;

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
