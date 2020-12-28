package com.fantechlabs.lailaa.view_models;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.models.Events;
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
                Constants.BASE_URL);
        if (service == null) {
            mDeleteEventListener.onFailedToDeleteEvent(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        HashMap<String, Object> item = new HashMap<String, Object>();
        item.put(Constants.USER_PRIVATE_CODE, Laila.instance().getMUser().getProfile().getUserPrivateCode());
        item.put(Constants.ID, eventId);

        HashMap<String, Object> deleteItem = new HashMap<>();
        deleteItem.put("event", item);

        val paymentService = service.deleteEvent(deleteItem);


        //***********************************************************
        paymentService.enqueue(new Callback<Events>()
                //***********************************************************
        {
            @Override
            public void onResponse(Call<Events> call, Response<Events> response) {
                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        mDeleteEventListener.onFailedToDeleteEvent((TextUtils.isEmpty(response.body().getError()) ?
                                AndroidUtil.getString(R.string.server_error) :
                                response.body().getError()));
                        return;
                    }
                    mDeleteEventListener.onSuccessfullyDeleteEvent(response.body().getResult());
                    return;
                }

                mDeleteEventListener.onFailedToDeleteEvent(AndroidUtil.getString(R.string.server_error));
            }

            @Override
            public void onFailure(Call<Events> call, Throwable t) {
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
