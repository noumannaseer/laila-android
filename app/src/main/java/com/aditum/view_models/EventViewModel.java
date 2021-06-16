package com.aditum.view_models;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.aditum.R;
import com.aditum.network.ServiceGenerator;
import com.aditum.network.services.EventsService;
import com.aditum.request_models.EventRequest;
import com.aditum.utils.AndroidUtil;
import com.aditum.utils.Constants;

import lombok.val;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//***********************************************************
public class EventViewModel
        extends ViewModel
//***********************************************************
{
    public EventListener mEventListener;

    //***********************************************************
    public EventViewModel(@NonNull EventListener mEventListener)
    //***********************************************************
    {
        this.mEventListener = mEventListener;
    }

    //***********************************************************
    public void addEvent(@NonNull EventRequest request)
    //***********************************************************
    {
        val service = ServiceGenerator.createService(EventsService.class, true,
                Constants.BASE_URL);
        if (service == null) {
            mEventListener.onFailedToAddEvent(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

//        HashMap<String, String> item = new HashMap<>();
//        item.put("user_private_code", RXCare.instance().getMUser().getProfile().getUserPrivateCode());
//        item.put("type", "Alarm");
//        item.put("event_title", request.getEventTitle());
//        item.put("start_date", request.getStartDate());
//        item.put("end_date", request.getEndDate());
//        item.put("frequency", "1");
//        item.put("alarm_type", "alarm");
//        item.put("timeSchedule", request.getTimeSchedule());

//        HashMap<String, Object> event = new HashMap<>();
//        event.put("event", request);
        val paymentService = service.addEvent(request);


        //***********************************************************
        paymentService.enqueue(new Callback<EventRequest>()
        //***********************************************************
        {
            @Override
            public void onResponse(Call<EventRequest> call, Response<EventRequest> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body() == null)
                    {
                        mEventListener.onFailedToAddEvent((TextUtils.isEmpty(response.body().getError()) ?
                                AndroidUtil.getString(R.string.server_error) :
                                response.body().getError()));
                        return;
                    }
                    mEventListener.onSuccessfullyAddEvent(response.body());
                    return;
                }

                mEventListener.onFailedToAddEvent(AndroidUtil.getString(R.string.server_error));
            }

            @Override
            public void onFailure(Call<EventRequest> call, Throwable t)
            {
                mEventListener.onFailedToAddEvent(t.getLocalizedMessage());
            }
        });

    }

    //***********************************************************
    public interface EventListener
    //***********************************************************
    {
        void onSuccessfullyAddEvent(@Nullable EventRequest response);

        void onFailedToAddEvent(@NonNull String errorMessage);
    }
}
