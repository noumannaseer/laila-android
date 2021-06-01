package com.fantechlabs.lailaa.view_models;

import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.models.response_models.FollowUpResponse;
import com.fantechlabs.lailaa.network.ServiceGenerator;
import com.fantechlabs.lailaa.network.services.MedicationService;
import com.fantechlabs.lailaa.request_models.FollowUpUpdateRequest;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;

import lombok.NonNull;
import lombok.val;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//**********************************************************
public class UpdateFollowUpViewModel
        extends ViewModel
//**********************************************************

{

    private UpdateFollowUpModelListener mUpdateFollowUpViewModelListener;

    public UpdateFollowUpViewModel(UpdateFollowUpModelListener viewModelListener)
    {
        this.mUpdateFollowUpViewModelListener = viewModelListener;
    }

    //**********************************************************
    public void updateFollowUp(FollowUpUpdateRequest request)
    //**********************************************************
    {

        val service = ServiceGenerator.createService(MedicationService.class,
                                                     true,
                                                     Constants.BASE_URL_U);
        if (service == null)
        {
            mUpdateFollowUpViewModelListener.onUpdateFollowUpFailed(AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        val placeServices = service.updateFollowUp(request);

        //**********************************************************
        placeServices.enqueue(new Callback<FollowUpResponse>()
        //**********************************************************
        {

            //**********************************************************
            @Override
            public void onResponse(Call<FollowUpResponse> call, Response<FollowUpResponse> response)
            //**********************************************************
            {
                if (response.isSuccessful())
                {
                    if (response.body().getStatus() == 200)
                    {
                        mUpdateFollowUpViewModelListener.onUpdateFollowUpSuccess(response.body().getData().getMessage());
                        return;
                    }
                    mUpdateFollowUpViewModelListener.onUpdateFollowUpFailed((TextUtils.isEmpty(response.body().getData().getMessage()) ? AndroidUtil.getString(R.string.server_error) : response.body().getData().getMessage()));
                    return;
                }
                mUpdateFollowUpViewModelListener.onUpdateFollowUpFailed(AndroidUtil.getString(R.string.server_error));
            }

            //**********************************************************
            @Override
            public void onFailure(Call<FollowUpResponse> call, Throwable t)
            //**********************************************************
            {
                if (mUpdateFollowUpViewModelListener != null)
                    mUpdateFollowUpViewModelListener.onUpdateFollowUpFailed(t.getLocalizedMessage());
            }
        });

    }

    //**********************************************************
    public interface UpdateFollowUpModelListener
    //**********************************************************
    {
        void onUpdateFollowUpSuccess(@Nullable String msg);

        void onUpdateFollowUpFailed(@NonNull String msg);
    }

}
