package com.fantechlabs.lailaa.bodyreading.viewmodels;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.bodyreading.repository.network.ServiceGenerator;
import com.fantechlabs.lailaa.bodyreading.repository.network.api.HealthApi;
import com.fantechlabs.lailaa.bodyreading.repository.storge.requestmodel.ReadHealthDataRequest;
import com.fantechlabs.lailaa.bodyreading.repository.storge.responsemodel.HealthDataReadingResponse;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;

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
        val service = ServiceGenerator.createService(HealthApi.class, true, Constants.BASE_URL);

        if (service == null) {
            mReadHealthDataListener.onReadHealthDataFailed(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        Log.d(TAG, "onRequest: " + request.toString());
        val readHealthDataService = service.readData(request);

        readHealthDataService.enqueue(new Callback<HealthDataReadingResponse>() {
            //***********************************************************
            @Override
            public void onResponse(Call<HealthDataReadingResponse> call, Response<HealthDataReadingResponse> response)
            //***********************************************************
            {
                Log.d(TAG, "onResponse: response from service");
                if (response.isSuccessful()) {
                    if (response.body().getStatus_code() == 404) {
                        Log.d(TAG, "onResponse: error" + response.body().getError());
                        mReadHealthDataListener.onReadHealthDataFailed((TextUtils.isEmpty(response.body().getError()) ? AndroidUtil.getString(R.string.server_error) : response.body().getError()));
                        return;
                    }
                    mReadHealthDataListener.onReadHealthDataSuccessfully(response.body());
                    return;
                }

                mReadHealthDataListener.onReadHealthDataFailed(AndroidUtil.getString(R.string.server_error));
            }

            //***********************************************************
            @Override
            public void onFailure(Call<HealthDataReadingResponse> call, Throwable t)
            //***********************************************************
            {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                mReadHealthDataListener.onReadHealthDataFailed(t.getLocalizedMessage());
            }
        });

    }

//    //***********************************************************
//    public void readHealthData(@NonNull ReadHealthDataRequest request)
//    //***********************************************************
//    {
//        val service = ServiceGenerator.createService(HealthApi.class, true, Constants.BASE_URL);
//
//        if (service == null)
//        {
//            mReadHealthDataListener.onReadHealthDataFailed(
//                    AndroidUtil.getString(R.string.internet_not_vailable));
//            return;
//        }
//
//        Log.d(TAG, "onRequest: " +request.toString());
//        val readHealthDataService = service.readData(request);
//
//        readHealthDataService.enqueue(new Callback<HealthDataReadingResponse>()
//        {
//            //***********************************************************
//            @Override
//            public void onResponse(Call<HealthDataReadingResponse> call, Response<HealthDataReadingResponse> response)
//            //***********************************************************
//            {
//                Log.d(TAG, "onResponse: response from service");
//                if (response.isSuccessful())
//                {
//                    if (response.body().getStatus_code() == 404)
//                    {
//                        Log.d(TAG, "onResponse: error" +response.body().getError());
//                        mReadHealthDataListener.onReadHealthDataFailed((TextUtils.isEmpty(response.body().getError()) ? AndroidUtil.getString(R.string.server_error) : response.body().getError()));
//                        return;
//                    }
//                    mReadHealthDataListener.onReadHealthDataSuccessfully(response.body());
//                    return;
//                }
//
//                mReadHealthDataListener.onReadHealthDataFailed(AndroidUtil.getString(R.string.server_error));
//            }
//
//            //***********************************************************
//            @Override
//            public void onFailure(Call<HealthDataReadingResponse> call, Throwable t)
//            //***********************************************************
//            {
//                Log.d(TAG, "onFailure: "+t.getLocalizedMessage());
//                mReadHealthDataListener.onReadHealthDataFailed(t.getLocalizedMessage());
//            }
//        });
//
//    }

    //***********************************************************
    public interface ReadHealthDataListener
            //***********************************************************
    {
        void onReadHealthDataSuccessfully(@Nullable HealthDataReadingResponse response);

        void onReadHealthDataFailed(@NonNull String errorMessage);
    }
}
