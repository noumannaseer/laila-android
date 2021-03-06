package com.aditum.view_models;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.aditum.R;
import com.aditum.models.updates.response_models.UserResponse;
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
public class LoginViewModel
        extends ViewModel
//***********************************************************
{
    public UserLoginListener mUserLoginListener;

    //***********************************************************
    public LoginViewModel(@NonNull UserLoginListener mUserLoginListener)
    //***********************************************************
    {
        this.mUserLoginListener = mUserLoginListener;
    }

    //***********************************************************
    public void loginUser(@NonNull UserRequest userRequest)
    //***********************************************************
    {
        val service = ServiceGenerator.createService(OnboardingService.class, true,
                Constants.BASE_URL_U);

        if (service == null) {
            mUserLoginListener.onLoginFailed(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        HashMap<String, String> login = new HashMap<String, String>();
        login.put("email", userRequest.getEmail());
        login.put("password", userRequest.getPassword());
        login.put("user_type", "laila");
        val loginService = service.login(login);

        loginService.enqueue(new Callback<UserResponse>() {
            //***********************************************************
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response)
            //***********************************************************
            {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() != 200) {
                        mUserLoginListener.onLoginFailed((TextUtils.isEmpty(response.body().getData().getMessage()) ?
                                AndroidUtil.getString(R.string.server_error) :
                                response.body().getData().getMessage()));
                        return;
                    }
                    mUserLoginListener.onLoginSuccessfully(response.body());
                    return;
                }
                val error = NetworkUtils.errorResponse(response.errorBody());
                mUserLoginListener.onLoginFailed(error);
            }

            //***********************************************************
            @Override
            public void onFailure(Call<UserResponse> call, Throwable t)
            //***********************************************************
            {
                mUserLoginListener.onLoginFailed(t.getLocalizedMessage());
            }
        });

    }

    //***********************************************************
    public interface UserLoginListener
            //***********************************************************
    {
        void onLoginSuccessfully(@Nullable UserResponse userResponse);

        void onLoginFailed(@NonNull String errorMessage);
    }
}
