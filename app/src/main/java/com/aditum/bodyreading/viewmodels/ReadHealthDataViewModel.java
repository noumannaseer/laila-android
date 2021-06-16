package com.aditum.bodyreading.viewmodels;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.aditum.R;
import com.aditum.bodyreading.repository.network.ServiceGenerator;
import com.aditum.bodyreading.repository.network.api.HealthApi;
import com.aditum.models.updates.request_models.ReadHealthDataRequest;
import com.aditum.models.updates.response_models.ReadHealthDataResponse;
import com.aditum.network.NetworkUtils;
import com.aditum.utils.AndroidUtil;
import com.aditum.utils.Constants;

import lombok.val;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//***********************************************************
public class ReadHealthDataViewModel
        extends ViewModel
//***********************************************************
{
    public ReadHealthDataListener mReadHealthDataListener;

    //***********************************************************
    public ReadHealthDataViewModel(@NonNull ReadHealthDataListener mListener)
    //***********************************************************
    {
        this.mReadHealthDataListener = mListener;
    }

    public static final String TAG = ReadHealthDataViewModel.class.getCanonicalName();

    //***********************************************************
    public void readHealthData(@NonNull ReadHealthDataRequest request)
    //***********************************************************
    {
        val service = ServiceGenerator.createService(HealthApi.class,
                true,
                Constants.BASE_URL_U);

        if (service == null) {
            mReadHealthDataListener.onReadHealthDataFailed(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        Log.d(TAG, "onRequest: " + request.toString());
        val readHealthDataService = service.readData(request);

        readHealthDataService.enqueue(new Callback<ReadHealthDataResponse>() {
            //***********************************************************
            @Override
            public void onResponse(Call<ReadHealthDataResponse> call, Response<ReadHealthDataResponse> response)
            //***********************************************************
            {
                Log.d(TAG, "onResponse: response from service");
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 404) {
                        Log.d(TAG, "onResponse: error" + response.body().getData().getMessage());
                        mReadHealthDataListener.onReadHealthDataFailed((TextUtils.isEmpty(response.body().getData().getMessage()) ? AndroidUtil.getString(R.string.server_error) : response.body().getData().getMessage()));
                        return;
                    }
                    mReadHealthDataListener.onReadHealthDataSuccessfully(response.body());
                    return;
                }
                val error = NetworkUtils.errorResponse(response.errorBody());
                mReadHealthDataListener.onReadHealthDataFailed(error);
            }

            //***********************************************************
            @Override
            public void onFailure(Call<ReadHealthDataResponse> call, Throwable t)
            //***********************************************************
            {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                mReadHealthDataListener.onReadHealthDataFailed(t.getLocalizedMessage());
            }
        });

    }

    //***********************************************************
    public interface ReadHealthDataListener
    //***********************************************************
    {
        void onReadHealthDataSuccessfully(@Nullable ReadHealthDataResponse response);

        void onReadHealthDataFailed(@NonNull String errorMessage);
    }
}
