package com.aditum.view_models;

import androidx.lifecycle.ViewModel;

import com.aditum.R;
import com.aditum.models.response_models.PasswordResponse;
import com.aditum.network.ServiceGenerator;
import com.aditum.network.services.OnboardingService;
import com.aditum.utils.AndroidUtil;
import com.aditum.utils.Constants;

import java.util.HashMap;

import lombok.NonNull;
import lombok.val;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//**********************************************************
public class ResetPasswordViewModel
        extends ViewModel
//**********************************************************

{

    private ResetPasswordViewModelListener mResetPasswordViewModelListener;

    public ResetPasswordViewModel(ResetPasswordViewModelListener viewModelListener)
    {
        this.mResetPasswordViewModelListener = viewModelListener;
    }

    //**********************************************************
    public void resetPassword(String email)
    //**********************************************************
    {

        val service = ServiceGenerator.createService(OnboardingService.class,
                                                     true,
                                                     Constants.BASE_URL_U);
        if (service == null)
        {
            mResetPasswordViewModelListener.onFailed(AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        HashMap<String, String> reset = new HashMap<String, String>();
        reset.put(Constants.EMAIL, email);



        val placeServices = service.resetPassword(reset);

        //**********************************************************
        placeServices.enqueue(new Callback<PasswordResponse>()
        //**********************************************************
        {

            //**********************************************************
            @Override
            public void onResponse(Call<PasswordResponse> call, Response<PasswordResponse> response)
            //**********************************************************
            {
                if (response.isSuccessful())
                {
                    if (response.body().getError() != null)
                    {
                        mResetPasswordViewModelListener.onFailed(response.body().getError());
                        return;
                    }
                    mResetPasswordViewModelListener.onSuccess(response.body().getResult());
                    return;
                }
                mResetPasswordViewModelListener.onFailed(AndroidUtil.getString(R.string.server_error));
            }

            //**********************************************************
            @Override
            public void onFailure(Call<PasswordResponse> call, Throwable t)
            //**********************************************************
            {
                if (mResetPasswordViewModelListener != null)
                    mResetPasswordViewModelListener.onFailed(t.getLocalizedMessage());
            }
        });

    }

    //**********************************************************
    public interface ResetPasswordViewModelListener
    //**********************************************************
    {
        void onSuccess(@NonNull String response);

        void onFailed(@NonNull String message);
    }

}
