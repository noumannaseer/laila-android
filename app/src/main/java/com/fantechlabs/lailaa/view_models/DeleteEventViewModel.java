package com.fantechlabs.lailaa.view_models;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.bodyreading.repository.network.utils.NetworkUtils;
import com.fantechlabs.lailaa.models.Events;
import com.fantechlabs.lailaa.models.response_models.EventResponse;
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
public class DeleteEventViewModel
        extends ViewModel
//***********************************************************
{
    public DeleteEventListener mDeleteEventListener;

    //***********************************************************
    public DeleteEventViewModel(@NonNull DeleteEventListener mListener)
    //***********************************************************
    {
        this.mDeleteEventListener = mListener;
    }

    //***********************************************************
    public void deleteEvent(@NonNull int eventId)
    //***********************************************************
    {
        val service = ServiceGenerator.createService(EventsService.class, true,
                Constants.BASE_URL_U);
        if (service == null) {
            mDeleteEventListener.onFailedToDeleteEvent(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }


        HashMap<String, Object> deleteEvent = new HashMap<String, Object>();
        deleteEvent.put(Constants.USER_ID, Laila.instance().getMUser_U().getData().getUser().getId().toString());
        deleteEvent.put(Constants.USER_TOKEN, Laila.instance().getMUser_U().getData().getUser().getToken());
        deleteEvent.put(Constants.EVENT_ID, eventId);

        val eventService = service.deleteEvent(deleteEvent);


        //***********************************************************
        eventService.enqueue(new Callback<EventResponse>()
                //***********************************************************
        {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        mDeleteEventListener.onFailedToDeleteEvent((TextUtils.isEmpty(response.body().getError()) ?
                                AndroidUtil.getString(R.string.server_error) :
                                response.body().getError()));
                        return;
                    }
                    mDeleteEventListener.onSuccessfullyDeleteEvent("Successfully deleted event");
                    return;
                }
                val error = NetworkUtils.responseError(response);
                mDeleteEventListener.onFailedToDeleteEvent(error);
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
                mDeleteEventListener.onFailedToDeleteEvent(t.getLocalizedMessage());
            }
        });

    }

    //***********************************************************
    public interface DeleteEventListener
            //***********************************************************
    {
        void onSuccessfullyDeleteEvent(@Nullable String result);

        void onFailedToDeleteEvent(@NonNull String error);
    }
}
