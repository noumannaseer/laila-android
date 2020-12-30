package com.fantechlabs.lailaa.view_models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.models.response_models.MedicineInformationResponse;
import com.fantechlabs.lailaa.network.ServiceGenerator;
import com.fantechlabs.lailaa.network.services.MedicationService;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;

import java.util.List;

import lombok.val;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//***********************************************************
public class MedicineInformationViewModel
        extends ViewModel
//***********************************************************
{

    public MedicineInformationListener mMedicineInformationListener;


    //***********************************************************
    public MedicineInformationViewModel(@NonNull MedicineInformationListener mMedicineInformationListener)
    //***********************************************************
    {
        this.mMedicineInformationListener = mMedicineInformationListener;
    }

    //***********************************************************
    public void getMedicineInformation(@NonNull String medicineCode)
    //***********************************************************
    {
        val service = ServiceGenerator.createService(MedicationService.class, true,
                Constants.MEDICINE_INFO_URL);
        if (service == null) {
            mMedicineInformationListener.onFailed(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        val medicationInformationService = service.getMedicineInformation(medicineCode);

        medicationInformationService.enqueue(new Callback<List<MedicineInformationResponse>>() {
            @Override
            public void onResponse(Call<List<MedicineInformationResponse>> call, Response<List<MedicineInformationResponse>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        mMedicineInformationListener.onSuccessfully(response.body());
                        return;
                    }
                    mMedicineInformationListener.onFailed(AndroidUtil.getString(R.string.server_error));
                }
            }

            @Override
            public void onFailure(Call<List<MedicineInformationResponse>> call, Throwable t) {
                mMedicineInformationListener.onFailed(t.getLocalizedMessage());

            }
        });


    }

    //***********************************************************
    public interface MedicineInformationListener
            //***********************************************************
    {
        void onSuccessfully(@Nullable List<MedicineInformationResponse> response);

        void onFailed(@NonNull String errorMessage);
    }
}
