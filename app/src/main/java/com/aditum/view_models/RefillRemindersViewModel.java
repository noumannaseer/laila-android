package com.aditum.view_models;

import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.aditum.Laila;
import com.aditum.R;
import com.aditum.bodyreading.repository.network.utils.NetworkUtils;
import com.aditum.models.updates.response_models.RefillReminderResponse;
import com.aditum.network.ServiceGenerator;
import com.aditum.network.services.MedicationService;
import com.aditum.utils.AndroidUtil;
import com.aditum.utils.Constants;

import java.util.HashMap;

import lombok.NonNull;
import lombok.val;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//**********************************************************
public class RefillRemindersViewModel
        extends ViewModel
//**********************************************************

{

    private RefillRemindersViewModelListener mRefillRemindersViewModelListener;

    public RefillRemindersViewModel(RefillRemindersViewModelListener viewModelListener) {
        this.mRefillRemindersViewModelListener = viewModelListener;
    }

    //**********************************************************
    public void getRefillReminders()
    //**********************************************************
    {

        val service = ServiceGenerator.createService(MedicationService.class,
                true,
                Constants.BASE_URL_U);
        if (service == null) {
            mRefillRemindersViewModelListener.onFailedGetRefillReminders(AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        HashMap<String, String> refillRemindersRequest = new HashMap<String, String>();
        refillRemindersRequest.put(Constants.USER_ID, Laila.instance().getMUser_U().getData().getUser().getId().toString());
        refillRemindersRequest.put(Constants.USER_TOKEN, Laila.instance().getMUser_U().getData().getUser().getToken());

        val placeServices = service.getRefillReminders(refillRemindersRequest);

        //**********************************************************
        placeServices.enqueue(new Callback<RefillReminderResponse>()
                //**********************************************************
        {

            //**********************************************************
            @Override
            public void onResponse(Call<RefillReminderResponse> call, Response<RefillReminderResponse> response)
            //**********************************************************
            {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 200) {
                        mRefillRemindersViewModelListener.onSuccessGetRefillReminders(response.body());
                        return;
                    }
                    mRefillRemindersViewModelListener.onFailedGetRefillReminders((TextUtils.isEmpty(response.body().getData().getMessage()) ? AndroidUtil.getString(R.string.server_error) : response.body().getData().getMessage()));
                    return;
                }
                val error = NetworkUtils.responseError(response);
                mRefillRemindersViewModelListener.onFailedGetRefillReminders(error);
            }

            //**********************************************************
            @Override
            public void onFailure(Call<RefillReminderResponse> call, Throwable t)
            //**********************************************************
            {
                if (mRefillRemindersViewModelListener != null)
                    mRefillRemindersViewModelListener.onFailedGetRefillReminders(t.getLocalizedMessage());
            }
        });

    }

    //**********************************************************
    public interface RefillRemindersViewModelListener
            //**********************************************************
    {
        void onSuccessGetRefillReminders(@Nullable RefillReminderResponse response);

        void onFailedGetRefillReminders(@NonNull String message);
    }

}
