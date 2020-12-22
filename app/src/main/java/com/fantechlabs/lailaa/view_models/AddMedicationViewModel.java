package com.fantechlabs.lailaa.view_models;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.models.response_models.MedicationResponse;
import com.fantechlabs.lailaa.network.ServiceGenerator;
import com.fantechlabs.lailaa.network.services.MedicationService;
import com.fantechlabs.lailaa.request_models.AddMedicationRequest;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;

import java.util.HashMap;

import lombok.val;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//***********************************************************
public class AddMedicationViewModel
        extends ViewModel
//***********************************************************
{
    public AddMedicationListener mAddMedicationListener;

    //***********************************************************
    public AddMedicationViewModel(@NonNull AddMedicationListener mUserLoginListener)
    //***********************************************************
    {
        this.mAddMedicationListener = mUserLoginListener;
    }

    //***********************************************************
    public void addMedication(@NonNull AddMedicationRequest addMedicationRequest)
    //***********************************************************
    {
        val service = ServiceGenerator.createService(MedicationService.class, true,
                Constants.BASE_URL);
        if (service == null) {
            mAddMedicationListener.onFailed(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        HashMap<String, Object> medicationList = new HashMap<String, Object>();
        medicationList.put(Constants.USER_PRIVATE_CODE, Laila.instance().getMUser().getProfile().getUserPrivateCode());
        medicationList.put(Constants.MEDICATION_NAME, addMedicationRequest.getMedicationName());
        medicationList.put(Constants.DIN_RX_NUMBER, addMedicationRequest.getMedicationDin());
        medicationList.put(Constants.STRENGTH, addMedicationRequest.getMedicationStrength());
        medicationList.put(Constants.STRENGTH_UOM, addMedicationRequest.getMedicationStrengthUnit());
        medicationList.put(Constants.COMMENTS, "");
        medicationList.put(Constants.AMOUNT, addMedicationRequest.getDispensedAmount());
        medicationList.put(Constants.START_DATE, addMedicationRequest.getDispensedDate());
        medicationList.put(Constants.MEDSCAPE_ID, "");
        medicationList.put(Constants.FREQUENCY1, "");
        medicationList.put(Constants.WHEN_NEEDED, "n");
        medicationList.put(Constants.FREQUENCY2, addMedicationRequest.getFrequency());
        medicationList.put(Constants.MEDECINE_FORM, addMedicationRequest.getFrom());
        medicationList.put(Constants.NUM_REFILLS, addMedicationRequest.getNoOfRefills());
        medicationList.put(Constants.PHARMACY, addMedicationRequest.getPharmacy());
        medicationList.put(Constants.REFILL_DATE, addMedicationRequest.getRefillDate());
        medicationList.put(Constants.DELIVERY_TYPE, addMedicationRequest.getDeliveryType());
        medicationList.put(Constants.NUMBER_OF_PILLS, addMedicationRequest.getNumber_of_pills());

        if (Laila.instance().on_update_medicine)
            medicationList.put(Constants.ID, addMedicationRequest.getId());

        HashMap<String, Object> main = new HashMap<String, Object>();
        main.put(Constants.MEDICATION, medicationList);

        val medicationService = service.addMedication(main);

        //***********************************************************
        medicationService.enqueue(new Callback<MedicationResponse>()
                //***********************************************************
        {

            //***********************************************************
            @Override
            public void onResponse(Call<MedicationResponse> call, Response<MedicationResponse> response)
            //***********************************************************
            {
                if (response.isSuccessful()) {
                    Log.d("SessionToken", "onResponse: " + response.toString());
                    if (response.body().getError() != null) {
                        mAddMedicationListener.onFailed(response.body().getError());
                        return;
                    }
                    Log.d("SessionToken", "onResponse: responseBody -> " + response.body().toString());
                    mAddMedicationListener.onSuccessfully(response.body());
                    return;
                }
                mAddMedicationListener.onFailed("Internal server error");
             }

            //***********************************************************
            @Override
            public void onFailure(Call<MedicationResponse> call, Throwable t)
            //***********************************************************
            {
                mAddMedicationListener.onFailed(t.getLocalizedMessage());
            }
        });

    }

    //***********************************************************
    public interface AddMedicationListener
            //***********************************************************
    {
        void onSuccessfully(@Nullable MedicationResponse medicationResponse);

        void onFailed(@NonNull String errorMessage);
    }

}
