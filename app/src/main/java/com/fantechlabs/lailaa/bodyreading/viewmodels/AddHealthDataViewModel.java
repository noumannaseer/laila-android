package com.fantechlabs.lailaa.bodyreading.viewmodels;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.bodyreading.repository.network.ServiceGenerator;
import com.fantechlabs.lailaa.bodyreading.repository.network.api.HealthApi;
import com.fantechlabs.lailaa.models.updates.request_models.AddHealthDataRequest;
import com.fantechlabs.lailaa.models.updates.response_models.AddHealthDataResponse;
import com.fantechlabs.lailaa.network.NetworkUtils;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;

import lombok.val;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//***********************************************************
public class AddHealthDataViewModel
        extends ViewModel
//***********************************************************
{
    public AddHealthDataListener mAddHealthDataListener;

    //***********************************************************
    public AddHealthDataViewModel(@NonNull AddHealthDataListener mListener)
    //***********************************************************
    {
        this.mAddHealthDataListener = mListener;
    }

    //***********************************************************
    public void addHealthData(@NonNull AddHealthDataRequest request)
    //***********************************************************
    {
        val service = ServiceGenerator.createService(HealthApi.class, true,
                Constants.BASE_URL_U);

        if (service == null) {
            mAddHealthDataListener.onAddHealthDataFailed(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        val addHealthDataService = service.addData(request);

        addHealthDataService.enqueue(new Callback<AddHealthDataResponse>() {
            //***********************************************************
            @Override
            public void onResponse(Call<AddHealthDataResponse> call, Response<AddHealthDataResponse> response)
            //***********************************************************
            {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() != 200) {
                        mAddHealthDataListener.onAddHealthDataFailed((TextUtils.isEmpty(response.body().getData().getMessage()) ? AndroidUtil.getString(R.string.server_error) : response.body().getData().getMessage()));
                        return;
                    }
                    mAddHealthDataListener.onAddHealthDataSuccessfully(response.body());
                    return;
                }
                val error = NetworkUtils.errorResponse(response.errorBody());
                mAddHealthDataListener.onAddHealthDataFailed(error);
            }

            //***********************************************************
            @Override
            public void onFailure(Call<AddHealthDataResponse> call, Throwable t)
            //***********************************************************
            {
                mAddHealthDataListener.onAddHealthDataFailed(t.getLocalizedMessage());
            }
        });

    }

    //***********************************************************
    public interface AddHealthDataListener
            //***********************************************************
    {
        void onAddHealthDataSuccessfully(@Nullable AddHealthDataResponse response);

        void onAddHealthDataFailed(@NonNull String errorMessage);
    }
}
