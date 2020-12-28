package com.fantechlabs.lailaa.view_models;

import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.models.response_models.FollowUpResponse;
import com.fantechlabs.lailaa.network.ServiceGenerator;
import com.fantechlabs.lailaa.network.services.MedicationService;
import com.fantechlabs.lailaa.request_models.FollowUpRequest;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;

import lombok.NonNull;
import lombok.val;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//**********************************************************
public class InsertFollowUpViewModel
        extends ViewModel
//**********************************************************

{

    private InsertFollowUpModelListener mInsertFollowUpViewModelListener;

    public InsertFollowUpViewModel(InsertFollowUpModelListener viewModelListener)
    {
        this.mInsertFollowUpViewModelListener = viewModelListener;
    }

    //**********************************************************
    public void insertFollowUp(FollowUpRequest request)
    //**********************************************************
    {

        val service = ServiceGenerator.createService(MedicationService.class,
                                                     true,
                                                     Constants.BASE_URL);
        if (service == null)
        {
            mInsertFollowUpViewModelListener.onInsertFollowUpFailed(AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        val placeServices = service.insertFollowUp(request);

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
                        mInsertFollowUpViewModelListener.onInsertFollowUpSuccess(response.body());
                        return;
                    }
                    mInsertFollowUpViewModelListener.onInsertFollowUpFailed((TextUtils.isEmpty(response.body().getMsg()) ? AndroidUtil.getString(R.string.server_error) : response.body().getMsg()));
                    return;
                }
                mInsertFollowUpViewModelListener.onInsertFollowUpFailed(AndroidUtil.getString(R.string.server_error));
            }

            //**********************************************************
            @Override
            public void onFailure(Call<FollowUpResponse> call, Throwable t)
            //**********************************************************
            {
                if (mInsertFollowUpViewModelListener != null)
                    mInsertFollowUpViewModelListener.onInsertFollowUpFailed(t.getLocalizedMessage());
            }
        });

    }

    //**********************************************************
    public interface InsertFollowUpModelListener
    //**********************************************************
    {
        void onInsertFollowUpSuccess(@Nullable FollowUpResponse msg);

        void onInsertFollowUpFailed(@NonNull String msg);
    }

}
