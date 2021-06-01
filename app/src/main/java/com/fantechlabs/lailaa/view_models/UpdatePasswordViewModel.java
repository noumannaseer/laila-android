package com.fantechlabs.lailaa.view_models;

import androidx.lifecycle.ViewModel;

import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.models.response_models.PasswordResponse;
import com.fantechlabs.lailaa.models.updates.response_models.UserResponse;
import com.fantechlabs.lailaa.network.NetworkUtils;
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
public class UpdatePasswordViewModel
        extends ViewModel
//**********************************************************

{

    private UpdatePasswordViewModelListener mUpdatePasswordViewModelListener;

    public UpdatePasswordViewModel(UpdatePasswordViewModelListener viewModelListener) {
        this.mUpdatePasswordViewModelListener = viewModelListener;
    }

    //**********************************************************
    public void updatePassword(String currentPassword, String newPassword)
    //**********************************************************
    {
        val service = ServiceGenerator.createService(OnboardingService.class,
                true,
                Constants.BASE_URL_U);
        if (service == null) {
            mUpdatePasswordViewModelListener.onFailed(AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }
        val user_id = Laila.instance().getMUser_U().getData().getUser().getId().toString();
        val user_token = Laila.instance().getMUser_U().getData().getUser().getToken();
        HashMap<String, String> update = new HashMap<String, String>();
        update.put(Constants.USER_ID, user_id);
        update.put(Constants.OLD_PASSWORD, currentPassword);
        update.put(Constants.NEW_PASSWORD, newPassword);
        update.put(Constants.USER_TOKEN, user_token);


        val placeServices = service.updatePassword(update);

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
                    if (response.body().getStatus() != 200) {
                        mUpdatePasswordViewModelListener.onFailed(response.body().getData().getMessage());
                        return;
                    }
                    mUpdatePasswordViewModelListener.onSuccess(response.body().getData().getMessage());
                    return;
                }
                val error = NetworkUtils.errorResponse(response.errorBody());
                mUpdatePasswordViewModelListener.onFailed(error);
            }

            //**********************************************************
            @Override
            public void onFailure(Call<UserResponse> call, Throwable t)
            //**********************************************************
            {
                if (mUpdatePasswordViewModelListener != null)
                    mUpdatePasswordViewModelListener.onFailed(t.getLocalizedMessage());
            }
        });

    }

    //**********************************************************
    public interface UpdatePasswordViewModelListener
            //**********************************************************
    {
        void onSuccess(@NonNull String message);

        void onFailed(@NonNull String message);
    }

}
