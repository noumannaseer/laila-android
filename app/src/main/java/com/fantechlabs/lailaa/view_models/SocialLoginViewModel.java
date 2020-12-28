package com.fantechlabs.lailaa.view_models;

import android.text.TextUtils;

import androidx.lifecycle.ViewModel;

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.models.response_models.UserResponse;
import com.fantechlabs.lailaa.network.ServiceGenerator;
import com.fantechlabs.lailaa.network.services.OnboardingService;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;

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
    public void socialLogin(String email, String token, int isFacebook)
    //**********************************************************
    {

        val service = ServiceGenerator.createService(OnboardingService.class,
                true,
                Constants.BASE_URL);
        if (service == null) {
            mSocialLoginViewModelListener.onFailedSocialLogin(AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        HashMap<String, String> login = new HashMap<String, String>();
        login.put("email", email);

        switch (isFacebook) {
            case 1:
                login.put("facebook", token);
                break;
            case 0:
                login.put("gmail", token);
                break;
        }

        val placeServices = service.socialLogin(login);


        //**********************************************************
        placeServices.enqueue(new Callback<UserResponse>()
                //**********************************************************
        {

            //**********************************************************
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response)
            //**********************************************************
            {
                if (response.isSuccessful()) {
                    if (response.body().getError() != null) {
                        mSocialLoginViewModelListener.onFailedSocialLogin((TextUtils.isEmpty(response.body().getError()) ?
                                AndroidUtil.getString(R.string.server_error) :
                                response.body().getError()));
                        return;
                    }
                    mSocialLoginViewModelListener.onSuccessSocialLogin(response.body());
                    return;
                }
                mSocialLoginViewModelListener.onFailedSocialLogin(AndroidUtil.getString(R.string.server_error));
            }

            //**********************************************************
            @Override
            public void onFailure(Call<UserResponse> call, Throwable t)
            //**********************************************************
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
