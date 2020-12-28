package com.fantechlabs.lailaa.view_models;

import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.models.response_models.RefillRemindersResponse;
import com.fantechlabs.lailaa.network.ServiceGenerator;
import com.fantechlabs.lailaa.network.services.MedicationService;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;

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

    public RefillRemindersViewModel(RefillRemindersViewModelListener viewModelListener)
    {
        this.mRefillRemindersViewModelListener = viewModelListener;
    }

    //**********************************************************
    public void getRefillReminders(String userPrivateCode)
    //**********************************************************
    {

        val service = ServiceGenerator.createService(MedicationService.class,
                                                     true,
                                                     Constants.HEALTH_CARE_URL);
        if (service == null)
        {
            mRefillRemindersViewModelListener.onFailedGetRefillReminders(AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        HashMap<String, String> request = new HashMap<String, String>();
        request.put(Constants.USER_PRIVATE_CODE, userPrivateCode);

        val placeServices = service.getRefillReminders(request);

        //**********************************************************
        placeServices.enqueue(new Callback<RefillRemindersResponse>()
        //**********************************************************
        {

            //**********************************************************
            @Override
            public void onResponse(Call<RefillRemindersResponse> call, Response<RefillRemindersResponse> response)
            //**********************************************************
            {
                if (response.isSuccessful())
                {
                    if (response.body().getCode().equals("200"))
                    {
                        mRefillRemindersViewModelListener.onSuccessGetRefillReminders(response.body());
                        return;
                    }
                    mRefillRemindersViewModelListener.onFailedGetRefillReminders((TextUtils.isEmpty(response.body().getMsg()) ? AndroidUtil.getString(R.string.server_error) : response.body().getMsg()));
                    return;
                }
                mRefillRemindersViewModelListener.onFailedGetRefillReminders(AndroidUtil.getString(R.string.server_error));
            }

            //**********************************************************
            @Override
            public void onFailure(Call<RefillRemindersResponse> call, Throwable t)
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
        void onSuccessGetRefillReminders(@Nullable RefillRemindersResponse response);

        void onFailedGetRefillReminders(@NonNull String message);
    }

}
