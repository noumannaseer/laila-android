package com.fantechlabs.lailaa.view_models;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.bodyreading.repository.network.utils.NetworkUtils;
import com.fantechlabs.lailaa.models.updates.request_models.AddEventRequest;
import com.fantechlabs.lailaa.models.updates.response_models.AddEventResponse;
import com.fantechlabs.lailaa.models.updates.response_models.GetEventsResponse;
import com.fantechlabs.lailaa.network.ServiceGenerator;
import com.fantechlabs.lailaa.network.services.EventsService;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;

import java.util.HashMap;

import lombok.val;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//***********************************************************
public class MedicineEventViewModel
        extends ViewModel
//***********************************************************
{
    public MedicineEventCompleteListener mMedicineEventListener;

    //***********************************************************
    public MedicineEventViewModel(@NonNull MedicineEventCompleteListener mListener)
    //***********************************************************
    {
        this.mMedicineEventListener = mListener;
    }

    //***********************************************************
    public void medicineEvent(AddEventRequest request)
    //***********************************************************
    {
        val service = ServiceGenerator.createService(EventsService.class, true,
                Constants.BASE_URL_U);
        if (service == null) {
            mMedicineEventListener.onFailedEvents(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }
        HashMap<String, Object> eventRequest = new HashMap<String, Object>();
        eventRequest.put(Constants.USER_ID, request.getUserId());
        eventRequest.put(Constants.USER_TOKEN, request.getToken());
        eventRequest.put(Constants.EVENTS, request.getEvents());

        val eventService = service.addMedicineEvents(eventRequest);

        //***********************************************************
        eventService.enqueue(new Callback<AddEventResponse>()
                //***********************************************************
        {
            @Override
            public void onResponse(Call<AddEventResponse> call, Response<AddEventResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        mMedicineEventListener.onFailedEvents((TextUtils.isEmpty(response.body().getData().getMessage()) ?
                                AndroidUtil.getString(R.string.server_error) :
                                response.body().getData().getMessage()));
                        return;
                    }
                    mMedicineEventListener.onSuccessfullyAddEvent(response.body());
                    return;
                }
                val error = NetworkUtils.responseError(response);
                mMedicineEventListener.onFailedEvents(error);
            }

            @Override
            public void onFailure(Call<AddEventResponse> call, Throwable t) {
                mMedicineEventListener.onFailedEvents(t.getLocalizedMessage());
            }
        });

    }

    //***********************************************************
    public void getEvents()
    //***********************************************************
    {
        val service = ServiceGenerator.createService(EventsService.class, true,
                Constants.BASE_URL_U);
        if (service == null) {
            mMedicineEventListener.onFailedEvents(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }
        val userId = Laila.instance().getMUser_U().getData().getUser().getId().toString();
        val token = Laila.instance().getMUser_U().getData().getUser().getToken();
        HashMap<String, String> eventRequest = new HashMap<>();
        eventRequest.put(Constants.USER_ID, userId);
        eventRequest.put(Constants.USER_TOKEN, token);

        val eventService = service.getEvents(eventRequest);

        //***********************************************************
        eventService.enqueue(new Callback<GetEventsResponse>()
                //***********************************************************
        {
            @Override
            public void onResponse(Call<GetEventsResponse> call, Response<GetEventsResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        mMedicineEventListener.onFailedEvents((TextUtils.isEmpty(response.body().getData().getMessage()) ?
                                AndroidUtil.getString(R.string.server_error) :
                                response.body().getData().getMessage()));
                        return;
                    }
                    mMedicineEventListener.onSuccessfullyGetEvents(response.body());
                    return;
                }
                val error = NetworkUtils.responseError(response);
                mMedicineEventListener.onFailedEvents(error);
            }

            @Override
            public void onFailure(Call<GetEventsResponse> call, Throwable t) {
                mMedicineEventListener.onFailedEvents(t.getLocalizedMessage());
            }
        });

    }

    //***********************************************************
    public interface MedicineEventCompleteListener
            //***********************************************************
    {
        void onSuccessfullyAddEvent(@Nullable AddEventResponse response);

        void onSuccessfullyGetEvents(@Nullable GetEventsResponse response);

        void onFailedEvents(@NonNull String errorMessage);
    }
}
