package com.aditum.view_models;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.aditum.Laila;
import com.aditum.R;
import com.aditum.models.updates.request_models.ProfileRequest;
import com.aditum.models.updates.response_models.ProfileResponse;
import com.aditum.network.NetworkUtils;
import com.aditum.network.ServiceGenerator;
import com.aditum.network.services.OnboardingService;
import com.aditum.utils.AndroidUtil;
import com.aditum.utils.Constants;

import java.util.HashMap;

import lombok.val;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//***********************************************************
public class UpdateProfileViewModel
        extends ViewModel
//***********************************************************
{
    public UpdateProfileListener mUpdateProfileListener;

    //***********************************************************
    public UpdateProfileViewModel(@NonNull UpdateProfileListener mUpdateProfileListener)
    //***********************************************************
    {
        this.mUpdateProfileListener = mUpdateProfileListener;
    }

    //***********************************************************
    public void updateProfile(@NonNull ProfileRequest request)
    //***********************************************************
    {
        val service = ServiceGenerator.createService(OnboardingService.class, true,
                Constants.BASE_URL_U);
        if (service == null) {
            mUpdateProfileListener.onUpdateFailed(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        val profileService = service.updateProfile(request);

        //***********************************************************
        profileService.enqueue(new Callback<ProfileResponse>()
                //***********************************************************
        {

            //***********************************************************
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response)
            //***********************************************************
            {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() != 200) {
                        mUpdateProfileListener.onUpdateFailed((TextUtils.isEmpty(response.body().getData().getMessage()) ? AndroidUtil.getString(R.string.server_error) : response.body().getData().getMessage()));
                        return;
                    }
                    mUpdateProfileListener.onUpdateSuccessfully(response.body());
                    return;
                }
                val error = NetworkUtils.errorResponse(response.errorBody());
                mUpdateProfileListener.onUpdateFailed(error);
            }

            //***********************************************************
            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t)
            //***********************************************************
            {
                val msg = t.getLocalizedMessage();
                mUpdateProfileListener.onUpdateFailed(t.getLocalizedMessage());
            }
        });

    }

    //***********************************************************
    public void getProfile()
    //***********************************************************
    {
        val service = ServiceGenerator.createService(OnboardingService.class, true,
                Constants.BASE_URL_U);
        if (service == null) {
            mUpdateProfileListener.onUpdateFailed(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }
        if (Laila.instance().getMUser_U().getData().getUser() == null)
            return;
        val user_id = Laila.instance().getMUser_U().getData().getUser().getId().toString();
        val token = Laila.instance().getMUser_U().getData().getUser().getToken();

        HashMap<String, String> profile = new HashMap<>();
        profile.put(Constants.USER_ID, user_id);
        profile.put(Constants.USER_TOKEN, token);

        val profileService = service.getProfile(profile);

        //***********************************************************
        profileService.enqueue(new Callback<ProfileResponse>()
                //***********************************************************
        {

            //***********************************************************
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response)
            //***********************************************************
            {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() != 200) {
                        mUpdateProfileListener.onUpdateFailed((TextUtils.isEmpty(response.body().getData().getMessage()) ? AndroidUtil.getString(R.string.server_error) : response.body().getData().getMessage()));
                        return;
                    }
                    mUpdateProfileListener.onUpdateSuccessfully(response.body());
                    return;
                }
                val error = NetworkUtils.errorResponse(response.errorBody());
                mUpdateProfileListener.onUpdateFailed(error);
            }

            //***********************************************************
            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t)
            //***********************************************************
            {
                val msg = t.getLocalizedMessage();
                mUpdateProfileListener.onUpdateFailed(t.getLocalizedMessage());
            }
        });

    }

    //***********************************************************
    public interface UpdateProfileListener
            //***********************************************************
    {
        void onUpdateSuccessfully(@Nullable ProfileResponse response);

        void onUpdateFailed(@NonNull String errorMessage);
    }
}
