package com.fantechlabs.lailaa.view_models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.models.updates.response_models.MedicationResponse;
import com.fantechlabs.lailaa.network.ServiceGenerator;
import com.fantechlabs.lailaa.network.services.MedicationService;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;

import java.util.HashMap;

import lombok.val;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//***********************************************************
public class DeleteMedicationViewModel
        extends ViewModel
//***********************************************************
{
    public DeleteMedicationListener mDeleteMedicationListener;

    //***********************************************************
    public DeleteMedicationViewModel(@NonNull DeleteMedicationListener mDeleteMedicationListener)
    //***********************************************************
    {
        this.mDeleteMedicationListener = mDeleteMedicationListener;
    }

    //***********************************************************
    public void deleteMedication(@NonNull String id)
    //***********************************************************
    {
        val service = ServiceGenerator.createService(MedicationService.class, true,
                Constants.BASE_URL_U);
        if (service == null) {
            mDeleteMedicationListener.onFailed(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }
        val user_token = Laila.instance().getMUser_U().getData().getUser().getToken();
        HashMap<String, String> deleteMedication = new HashMap<String, String>();
        deleteMedication.put(Constants.USER_TOKEN, user_token);
        deleteMedication.put("id", id);

        val medicationService = service.deleteMedication(deleteMedication);

        medicationService.enqueue(new Callback<MedicationResponse>() {

            //***********************************************************
            @Override
            public void onResponse(Call<MedicationResponse> call, Response<MedicationResponse> response)
            //***********************************************************
            {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() != 200) {
                        mDeleteMedicationListener.onFailed(response.body().getData().getMessage());
                        return;
                    }
                    mDeleteMedicationListener.onSuccessfully(response.body());
                    return;
                }
                mDeleteMedicationListener.onFailed(AndroidUtil.getString(R.string.server_error));

            }

            //***********************************************************
            @Override
            public void onFailure(Call<MedicationResponse> call, Throwable t)
            //***********************************************************
            {
                mDeleteMedicationListener.onFailed(t.getLocalizedMessage());
            }
        });

    }

    //***********************************************************
    public interface DeleteMedicationListener
            //***********************************************************
    {
        void onSuccessfully(@Nullable MedicationResponse medicationResponse);

        void onFailed(@NonNull String errorMessage);
    }
}
