package com.fantechlabs.lailaa.view_models;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.models.response_models.SignUpResponse;
import com.fantechlabs.lailaa.network.ServiceGenerator;
import com.fantechlabs.lailaa.network.services.OnboardingService;
import com.fantechlabs.lailaa.request_models.UserRequest;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;

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
                                                     Constants.BASE_URL);
        if (service == null)
        {
            mUserLoginListener.onSignUpFailed(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        HashMap<String, String> signuplist = new HashMap<String, String>();
        signuplist.put("email", userRequest.getEmail());
        signuplist.put("password", userRequest.getPassword());

        val loginService = service.signUp(signuplist);

        loginService.enqueue(new Callback<SignUpResponse>()
        {
            //***********************************************************
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response)
            //***********************************************************
            {
                if (response.isSuccessful())
                {
                   if(response.body().getSuccess() == null)
                   {
                       mUserLoginListener.onSignUpFailed((TextUtils.isEmpty(response.body().getError()) ? AndroidUtil.getString(R.string.server_error) : response.body().getError()));
                       return;
                   }
                    mUserLoginListener.onSignUpSuccessfully(response.body());
                    return;
                }

                mUserLoginListener.onSignUpFailed(AndroidUtil.getString(R.string.server_error));
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
