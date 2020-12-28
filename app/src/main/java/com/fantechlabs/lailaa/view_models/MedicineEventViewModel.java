package com.fantechlabs.lailaa.view_models;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.models.response_models.MedicineEventResponse;
import com.fantechlabs.lailaa.network.ServiceGenerator;
import com.fantechlabs.lailaa.network.services.EventsService;
import com.fantechlabs.lailaa.request_models.AddMedicineEventRequest;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;

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
    public void medicineEvent(AddMedicineEventRequest request)
    //***********************************************************
    {
        val service = ServiceGenerator.createService(EventsService.class, true,
                Constants.BASE_URL);
        if (service == null) {
            mMedicineEventListener.onFailedToAddEvent(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        val paymentService = service.addMedicineEvents(request);

        //***********************************************************
        paymentService.enqueue(new Callback<MedicineEventResponse>()
        //***********************************************************
        {
            @Override
            public void onResponse(Call<MedicineEventResponse> call, Response<MedicineEventResponse> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body() == null)
                    {
                        mMedicineEventListener.onFailedToAddEvent((TextUtils.isEmpty(response.body().getError()) ?
                                AndroidUtil.getString(R.string.server_error) :
                                response.body().getError()));
                        return;
                    }
                    mMedicineEventListener.onSuccessfullyAddEvent(response.body());
                    return;
                }

                mMedicineEventListener.onFailedToAddEvent(AndroidUtil.getString(R.string.server_error));
            }

            @Override
            public void onFailure(Call<MedicineEventResponse> call, Throwable t)
            {
                mMedicineEventListener.onFailedToAddEvent(t.getLocalizedMessage());
            }
        });

    }

    //***********************************************************
    public interface MedicineEventCompleteListener
    //***********************************************************
    {
        void onSuccessfullyAddEvent(@Nullable MedicineEventResponse response);

        void onFailedToAddEvent(@NonNull String errorMessage);
    }
}
