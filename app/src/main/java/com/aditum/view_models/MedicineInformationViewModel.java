package com.aditum.view_models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.aditum.Laila;
import com.aditum.R;
import com.aditum.models.updates.response_models.MedicineInfoResponse;
import com.aditum.network.ServiceGenerator;
import com.aditum.network.services.MedicationService;
import com.aditum.utils.AndroidUtil;
import com.aditum.utils.Constants;

import java.util.HashMap;

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
                Constants.BASE_URL_U);
        if (service == null) {
            mMedicineInformationListener.onFailed(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }
        val userResponse = Laila.instance().getMUser_U();
        if (userResponse.getData() == null)
            return;
        val token = Laila.instance().getMUser_U().getData().getUser().getToken();
        HashMap<String, String> medicationInfo = new HashMap<>();
        medicationInfo.put("din_number", medicineCode);
        medicationInfo.put(Constants.USER_TOKEN, token);

        val medicationInformationService = service.getMedicineInformation(medicationInfo);

        medicationInformationService.enqueue(new Callback<MedicineInfoResponse>() {
            @Override
            public void onResponse(Call<MedicineInfoResponse> call, Response<MedicineInfoResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        mMedicineInformationListener.onSuccessfully(response.body());
                        return;
                    }
                    mMedicineInformationListener.onFailed(AndroidUtil.getString(R.string.server_error));
                }
            }

            @Override
            public void onFailure(Call<MedicineInfoResponse> call, Throwable t) {
                mMedicineInformationListener.onFailed(t.getLocalizedMessage());
            }
        });
    }

    //***********************************************************
    public interface MedicineInformationListener
            //***********************************************************
    {
        void onSuccessfully(@Nullable MedicineInfoResponse response);

        void onFailed(@NonNull String errorMessage);
    }
}
