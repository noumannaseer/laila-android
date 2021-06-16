package com.aditum.view_models;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.aditum.Laila;
import com.aditum.R;
import com.aditum.models.updates.request_models.AddMedicationRequest;
import com.aditum.models.updates.response_models.MedicationResponse;
import com.aditum.network.NetworkUtils;
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
                Constants.BASE_URL_U);
        if (service == null) {
            mAddMedicationListener.onFailed(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        val user_token = Laila.instance().getMUser_U().getData().getUser().getToken();
        val user_id = Laila.instance().getMUser_U().getData().getUser().getId();
        HashMap<String, Object> medicationList = new HashMap<String, Object>();

        medicationList.put(Constants.MEDICATION_NAME, addMedicationRequest.getMedicationName());
        medicationList.put(Constants.DIN_RX_NUMBER, addMedicationRequest.getDinRxNumber());
        medicationList.put(Constants.STRENGTH, addMedicationRequest.getStrength());
        medicationList.put(Constants.STRENGTH_UOM, addMedicationRequest.getStrengthUom());
        medicationList.put(Constants.DISPENSED_AMOUNT, addMedicationRequest.getDispensedAmount());
        medicationList.put(Constants.DISPENSED_DATE, addMedicationRequest.getDispensedDate());
        medicationList.put(Constants.PRESCRIBED, addMedicationRequest.getPrescribed());
        medicationList.put(Constants.FREQUENCY, addMedicationRequest.getFrequency());
        medicationList.put(Constants.MEDECINE_FORM, addMedicationRequest.getMedecineForm());
        medicationList.put(Constants.NUM_REFILLS, addMedicationRequest.getNumRefills());
        medicationList.put(Constants.PHARMACY_ID, addMedicationRequest.getPharmacyId());
        medicationList.put(Constants.REFILL_DATE, addMedicationRequest.getRefillDate());
        medicationList.put(Constants.DOSAGE, addMedicationRequest.getDosage());
        medicationList.put(Constants.USER_TOKEN, user_token);
        medicationList.put(Constants.USER_ID, user_id);

        val medicationService = service.addMedication(medicationList);

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
                    if (response.body().getStatus() != 200) {
                        mAddMedicationListener.onFailed(response.body().getData().getMessage());
                        return;
                    }
                    mAddMedicationListener.onSuccessfully(response.body());
                    return;
                }
                val error = NetworkUtils.errorResponse(response.errorBody());
                mAddMedicationListener.onFailed(error);
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
    public void updateMedication(@NonNull AddMedicationRequest addMedicationRequest)
    //***********************************************************
    {
        val service = ServiceGenerator.createService(MedicationService.class, true,
                Constants.BASE_URL_U);
        if (service == null) {
            mAddMedicationListener.onFailed(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }
        val user_token = Laila.instance().getMUser_U().getData().getUser().getToken();
        HashMap<String, Object> medicationList = new HashMap<String, Object>();
        medicationList.put(Constants.ID, addMedicationRequest.getId());
        medicationList.put(Constants.MEDICATION_NAME, addMedicationRequest.getMedicationName());
        medicationList.put(Constants.DIN_RX_NUMBER, addMedicationRequest.getDinRxNumber());
        medicationList.put(Constants.STRENGTH, addMedicationRequest.getStrength());
        medicationList.put(Constants.STRENGTH_UOM, addMedicationRequest.getStrengthUom());
        medicationList.put(Constants.DISPENSED_AMOUNT, addMedicationRequest.getDispensedAmount());
        medicationList.put(Constants.DISPENSED_DATE, addMedicationRequest.getDispensedDate());
        medicationList.put(Constants.PRESCRIBED, addMedicationRequest.getPrescribed());
        medicationList.put(Constants.FREQUENCY, addMedicationRequest.getFrequency());
        medicationList.put(Constants.MEDECINE_FORM, addMedicationRequest.getMedecineForm());
        medicationList.put(Constants.NUM_REFILLS, addMedicationRequest.getNumRefills());
        medicationList.put(Constants.PHARMACY_ID, addMedicationRequest.getPharmacyId());
        medicationList.put(Constants.REFILL_DATE, addMedicationRequest.getRefillDate());
        medicationList.put(Constants.DOSAGE, addMedicationRequest.getDosage());
        medicationList.put(Constants.USER_TOKEN, user_token);

        val medicationService = service.updateMedication(medicationList);

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
                    if (response.body().getStatus() != 200) {
                        mAddMedicationListener.onFailed(response.body().getData().getMessage());
                        return;
                    }
                    mAddMedicationListener.onSuccessfully(response.body());
                    return;
                }
                val e = response.errorBody().toString();
                val error = NetworkUtils.errorResponse(response.errorBody());
                mAddMedicationListener.onFailed(error);
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
    public void getMedications()
    //***********************************************************
    {
        val service = ServiceGenerator.createService(MedicationService.class, true,
                Constants.BASE_URL_U);
        if (service == null) {
            mAddMedicationListener.onFailedGetMedications(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }
        val userResponse = Laila.instance().getMUser_U();
        if (userResponse.getData() == null)
            return;
        val token = Laila.instance().getMUser_U().getData().getUser().getToken();
        val user_id = Laila.instance().getMUser_U().getData().getUser().getId().toString();
        HashMap<String, String> medications = new HashMap<>();
        medications.put(Constants.USER_TOKEN, token);
        medications.put(Constants.USER_ID, user_id);
        val getDocumentsServices = service.getMedications(medications);

        getDocumentsServices.enqueue(new Callback<MedicationResponse>() {
            @Override
            public void onResponse(Call<MedicationResponse> call, Response<MedicationResponse> response) {
                if (response.isSuccessful()) {

                    if (response.body().getStatus() == 200) {
                        mAddMedicationListener.onSuccessfullyGetMedications(response.body());
                        return;
                    }

                    mAddMedicationListener.onFailedGetMedications((TextUtils.isEmpty(response.body().getData().getMessage()) ?
                            AndroidUtil.getString(R.string.server_error) :
                            response.body().getData().getMessage()));
                    return;
                }
                val error = NetworkUtils.errorResponse(response.errorBody());
                mAddMedicationListener.onFailedGetMedications(error);

            }

            @Override
            public void onFailure(Call<MedicationResponse> call, Throwable t) {
                mAddMedicationListener.onFailedGetMedications(t.getLocalizedMessage());
            }
        });

    }

    //***********************************************************
    public interface AddMedicationListener
            //***********************************************************
    {
        void onSuccessfully(@Nullable MedicationResponse medicationResponse);

        void onFailed(@NonNull String errorMessage);

        void onSuccessfullyGetMedications(@Nullable MedicationResponse medicationResponse);

        void onFailedGetMedications(@NonNull String errorMessage);
    }

}
