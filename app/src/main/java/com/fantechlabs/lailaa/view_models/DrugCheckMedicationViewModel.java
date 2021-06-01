package com.fantechlabs.lailaa.view_models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.models.response_models.DrugCheckResponse;
import com.fantechlabs.lailaa.models.updates.response_models.MedicineInteractionResponse;
import com.fantechlabs.lailaa.network.NetworkUtils;
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
public class DrugCheckMedicationViewModel
        extends ViewModel
//***********************************************************
{
    public DrugCheckMedicationListener mDrugCheckMedicationListener;

    //***********************************************************
    public DrugCheckMedicationViewModel(@NonNull DrugCheckMedicationListener mDeleteMedicationListener)
    //***********************************************************
    {
        this.mDrugCheckMedicationListener = mDeleteMedicationListener;
    }

    //***********************************************************
    public void drugCheckMedication(@NonNull int id)
    //***********************************************************
    {
        val service = ServiceGenerator.createService(MedicationService.class, true,
                Constants.BASE_URL_U);
        if (service == null) {
            mDrugCheckMedicationListener.onDrugCheckFailed(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        HashMap<String, Object> drugCheckMedication = new HashMap<String, Object>();
        drugCheckMedication.put(Constants.USER_ID, Laila.instance().getMUser_U().getData().getUser().getId());
        drugCheckMedication.put(Constants.MEDICINE_ID, id);
        drugCheckMedication.put(Constants.USER_TOKEN, Laila.instance().getMUser_U().getData().getUser().getToken());


        val drugCheckMedicationService = service.drugCheckMedication(drugCheckMedication);

        drugCheckMedicationService.enqueue(new Callback<MedicineInteractionResponse>() {
            //***********************************************************
            @Override
            public void onResponse(Call<MedicineInteractionResponse> call, Response<MedicineInteractionResponse> response)
            //***********************************************************
            {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() != 200) {
                        mDrugCheckMedicationListener.onDrugCheckFailed(response.body().getData().getMessage());
                        return;
                    }
                    mDrugCheckMedicationListener.onDrugCheckSuccessfully(response.body());
                    return;
                }
                val error = NetworkUtils.errorResponse(response.errorBody());
                mDrugCheckMedicationListener.onDrugCheckFailed(error);
            }

            //***********************************************************
            @Override
            public void onFailure(Call<MedicineInteractionResponse> call, Throwable t)
            //***********************************************************
            {
                mDrugCheckMedicationListener.onDrugCheckFailed(t.getLocalizedMessage());
            }
        });

    }

    //***********************************************************
    public interface DrugCheckMedicationListener
            //***********************************************************
    {
        void onDrugCheckSuccessfully(@Nullable MedicineInteractionResponse response);

        void onDrugCheckFailed(@NonNull String errorMessage);
    }
}
