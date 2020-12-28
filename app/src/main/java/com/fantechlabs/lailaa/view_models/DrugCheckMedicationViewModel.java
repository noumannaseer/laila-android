package com.fantechlabs.lailaa.view_models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.models.response_models.DrugCheckResponse;
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
                Constants.HEALTH_CARE_URL);
        if (service == null) {
            mDrugCheckMedicationListener.onDrugCheckFailed(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        HashMap<String, Object> drugCheckMedication = new HashMap<String, Object>();
        drugCheckMedication.put(Constants.USER_PRIVATE_CODE, Laila.instance().getMUser().getProfile().getUserPrivateCode());
        drugCheckMedication.put("medication_id", id);


        val drugCheckMedicationService = service.drugCheckMedication(drugCheckMedication);

        drugCheckMedicationService.enqueue(new Callback<DrugCheckResponse>()
        {
            //***********************************************************
            @Override
            public void onResponse(Call<DrugCheckResponse> call, Response<DrugCheckResponse> response)
            //***********************************************************
            {
                if (response.isSuccessful())
                {
                    val code = response.body().getCode();
                    switch (code)
                    {
                        case "402":
                        case "200":
                            mDrugCheckMedicationListener.onDrugCheckSuccessfully(response.body());
                            return;
                        case "401":
                            mDrugCheckMedicationListener.onDrugCheckFailed(response.body().getMsg());
                            return;
                    }
                }
                mDrugCheckMedicationListener.onDrugCheckSuccessfully(response.body());
            }

            //***********************************************************
            @Override
            public void onFailure(Call<DrugCheckResponse> call, Throwable t)
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
        void onDrugCheckSuccessfully(@Nullable DrugCheckResponse response);
        void onDrugCheckFailed(@NonNull String errorMessage);
    }
}
