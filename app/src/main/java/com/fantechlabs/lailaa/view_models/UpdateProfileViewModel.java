package com.fantechlabs.lailaa.view_models;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.models.response_models.UpdateProfileResponse;
import com.fantechlabs.lailaa.network.ServiceGenerator;
import com.fantechlabs.lailaa.network.services.OnboardingService;
import com.fantechlabs.lailaa.request_models.ProfileRequest;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;

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
                Constants.BASE_URL);
        if (service == null) {
            mUpdateProfileListener.onUpdateFailed(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        val profileService = service.updateProfile(request);

        //***********************************************************
        profileService.enqueue(new Callback<UpdateProfileResponse>()
                //***********************************************************
        {

            //***********************************************************
            @Override
            public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response)
            //***********************************************************
            {
                if (response.isSuccessful()) {
                    if (response.body().getSuccess() == null) {
                        mUpdateProfileListener.onUpdateFailed((TextUtils.isEmpty(response.body().getError()) ? AndroidUtil.getString(R.string.server_error) : response.body().getError()));
                        return;
                    }
                    mUpdateProfileListener.onUpdateSuccessfully(response.body());
                    return;
                }
                mUpdateProfileListener.onUpdateFailed(AndroidUtil.getString(R.string.server_error));
            }

            //***********************************************************
            @Override
            public void onFailure(Call<UpdateProfileResponse> call, Throwable t)
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
        void onUpdateSuccessfully(@Nullable UpdateProfileResponse response);

        void onUpdateFailed(@NonNull String errorMessage);
    }
}
