package com.fantechlabs.lailaa.view_models;

import android.app.Activity;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.models.updates.request_models.AvatarRequest;
import com.fantechlabs.lailaa.models.updates.response_models.AvatarResponse;
import com.fantechlabs.lailaa.models.updates.response_models.UserResponse;
import com.fantechlabs.lailaa.network.NetworkUtils;
import com.fantechlabs.lailaa.network.ServiceGenerator;
import com.fantechlabs.lailaa.network.services.DocumentsService;
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
public class AvatarViewModel
        extends ViewModel
//***********************************************************
{
    public AvatarListener mAvatarListener;
    private Activity mActivity;

    //***********************************************************
    public AvatarViewModel(@NonNull AvatarListener mAvatarListener, Activity mActivity)
    //***********************************************************
    {
        this.mAvatarListener = mAvatarListener;
        this.mActivity = mActivity;

    }

    //***********************************************************
    public void uploadAvatar(@NonNull AvatarRequest avatarRequest)
    //***********************************************************
    {
        val service = ServiceGenerator.createService(DocumentsService.class, true,
                Constants.BASE_URL_U);

        if (service == null) {
            mAvatarListener.onFailedUploadAvatar(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }
        val avatar =
                !TextUtils.isEmpty(avatarRequest.getFile())
                        ? NetworkUtils.getFileMultiPartForm(
                        Uri.parse(avatarRequest.getFile()),
                        "file", mActivity)
                        : null;

        val avatarService = service.uploadAvatar(
                (NetworkUtils.getMultiPartForm(String.valueOf(avatarRequest.getUser_id()))),
                NetworkUtils.getMultiPartForm(String.valueOf(avatarRequest.getToken())),
                avatar
        );

        avatarService.enqueue(new Callback<AvatarResponse>() {
            //***********************************************************
            @Override
            public void onResponse(Call<AvatarResponse> call, Response<AvatarResponse> response)
            //***********************************************************
            {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() != 200) {
                        mAvatarListener.onFailedUploadAvatar((TextUtils.isEmpty(response.body().getData().getMessage()) ?
                                AndroidUtil.getString(R.string.server_error) :
                                response.body().getData().getMessage()));
                        return;
                    }
                    mAvatarListener.onSuccessfullyUploadAvatar(response.body());
                    return;
                }
                val error = NetworkUtils.errorResponse(response.errorBody());
                mAvatarListener.onFailedUploadAvatar(error);
            }

            //***********************************************************
            @Override
            public void onFailure(Call<AvatarResponse> call, Throwable t)
            //***********************************************************
            {
                mAvatarListener.onFailedUploadAvatar(t.getLocalizedMessage());
            }
        });

    }

    //***********************************************************
    public interface AvatarListener
    //***********************************************************
    {
        void onSuccessfullyUploadAvatar(@Nullable AvatarResponse avatarResponse);

        void onFailedUploadAvatar(@NonNull String errorMessage);
    }
}
