package com.aditum.view_models;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.aditum.R;
import com.aditum.models.updates.response_models.SignUpResponse;
import com.aditum.network.NetworkUtils;
import com.aditum.network.ServiceGenerator;
import com.aditum.network.services.OnboardingService;
import com.aditum.request_models.UserRequest;
import com.aditum.utils.AndroidUtil;
import com.aditum.utils.Constants;

import java.util.HashMap;

import lombok.val;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//***********************************************************
public class SignUpViewModel
        extends ViewModel
//***********************************************************
{
    public UserSignUpListener mUserLoginListener;

    //***********************************************************
    public SignUpViewModel(@NonNull UserSignUpListener mUserLoginListener)
    //***********************************************************
    {
        this.mUserLoginListener = mUserLoginListener;
    }

    //***********************************************************
    public void signUpUser(@NonNull UserRequest userRequest)
    //***********************************************************
    {
        val service = ServiceGenerator.createService(OnboardingService.class, true,
                Constants.BASE_URL_U);
        if (service == null) {
            mUserLoginListener.onSignUpFailed(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        HashMap<String, String> signuplist = new HashMap<String, String>();
        signuplist.put("email", userRequest.getEmail());
        signuplist.put("password", userRequest.getPassword());
        signuplist.put("user_type", "laila");

        val loginService = service.signUp(signuplist);

        loginService.enqueue(new Callback<SignUpResponse>() {
            //***********************************************************
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response)
            //***********************************************************
            {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() != 200) {
                        mUserLoginListener.onSignUpFailed((TextUtils.isEmpty(response.body().getData().getMessage()) ? AndroidUtil.getString(R.string.server_error) : response.body().getData().getMessage()));
                        return;
                    }
                    mUserLoginListener.onSignUpSuccessfully(response.body());
                    return;
                }
                val error = NetworkUtils.errorResponse(response.errorBody());
                mUserLoginListener.onSignUpFailed(error);
            }

            //***********************************************************
            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t)
            //***********************************************************
            {
                mUserLoginListener.onSignUpFailed(t.getLocalizedMessage());
            }
        });

    }

    //***********************************************************
    public interface UserSignUpListener
            //***********************************************************
    {
        void onSignUpSuccessfully(@Nullable SignUpResponse userResponse);

        void onSignUpFailed(@NonNull String errorMessage);
    }
}
