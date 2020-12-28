package com.fantechlabs.lailaa.bodyreading.viewmodels;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.bodyreading.repository.network.ServiceGenerator;
import com.fantechlabs.lailaa.bodyreading.repository.network.api.HealthApi;
import com.fantechlabs.lailaa.bodyreading.repository.storge.requestmodel.AddHealthDataRequest;
import com.fantechlabs.lailaa.bodyreading.repository.storge.responsemodel.AddDataHealthResponse;
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
                Constants.BASE_URL);

        if (service == null) {
            mAddHealthDataListener.onAddHealthDataFailed(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        val addHealthDataService = service.addData(request);

        addHealthDataService.enqueue(new Callback<AddDataHealthResponse>() {
            //***********************************************************
            @Override
            public void onResponse(Call<AddDataHealthResponse> call, Response<AddDataHealthResponse> response)
            //***********************************************************
            {
                if (response.isSuccessful()) {
                    if (response.body().getSuccess() == null) {
                        mAddHealthDataListener.onAddHealthDataFailed((TextUtils.isEmpty(response.body().getError()) ? AndroidUtil.getString(R.string.server_error) : response.body().getError()));
                        return;
                    }
                    mAddHealthDataListener.onAddHealthDataSuccessfully(response.body());
                    return;
                }

                mAddHealthDataListener.onAddHealthDataFailed(AndroidUtil.getString(R.string.server_error));
            }

            //***********************************************************
            @Override
            public void onFailure(Call<AddDataHealthResponse> call, Throwable t)
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
        void onAddHealthDataSuccessfully(@Nullable AddDataHealthResponse response);

        void onAddHealthDataFailed(@NonNull String errorMessage);
    }
}
