package com.aditum.view_models;

import android.text.TextUtils;

import androidx.lifecycle.ViewModel;

import com.aditum.R;
import com.aditum.models.updates.response_models.UserResponse;
import com.aditum.network.NetworkUtils;
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
public class SocialLoginViewModel
        extends ViewModel
//**********************************************************

{
    private SocialLoginViewModelListener mSocialLoginViewModelListener;

    public SocialLoginViewModel(SocialLoginViewModelListener viewModelListener) {
        this.mSocialLoginViewModelListener = viewModelListener;
    }

    //**********************************************************
    public void socialLogin(String email, String token, String socialType)
    //**********************************************************
    {

        val service = ServiceGenerator.createService(OnboardingService.class,
                true,
                Constants.BASE_URL_U);
        if (service == null) {
            mSocialLoginViewModelListener.onFailedSocialLogin(AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        HashMap<String, String> login = new HashMap<>();
        login.put(Constants.EMAIL, email);
        login.put(Constants.SOCIAL_TYPE, socialType);
        login.put("user_type", "laila");

        val loginServices = service.socialLogin(login);
        loginServices.enqueue(new Callback<UserResponse>() {
            //*****************************************************
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response)
            //*****************************************************
            {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() != 200) {
                        mSocialLoginViewModelListener.onFailedSocialLogin((TextUtils.isEmpty(response.body().getData().getMessage()) ?
                                AndroidUtil.getString(R.string.server_error) :
                                response.body().getData().getMessage()));
                        return;
                    }
                    mSocialLoginViewModelListener.onSuccessSocialLogin(response.body());
                    return;
                }
                val error = NetworkUtils.errorResponse(response.errorBody());
                mSocialLoginViewModelListener.onFailedSocialLogin(error);

            }

            //*****************************************************
            @Override
            public void onFailure(Call<UserResponse> call, Throwable t)
            //*****************************************************
            {
                if (mSocialLoginViewModelListener != null)
                    mSocialLoginViewModelListener.onFailedSocialLogin(t.getLocalizedMessage());
            }
        });

    }

    //**********************************************************
    public interface SocialLoginViewModelListener
            //**********************************************************
    {
        void onSuccessSocialLogin(@NonNull UserResponse response);

        void onFailedSocialLogin(@NonNull String message);
    }

}
