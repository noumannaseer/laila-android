package com.aditum.view_models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.aditum.Laila;
import com.aditum.R;
import com.aditum.models.updates.response_models.ActiveIngredientsResponse;
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
public class IngredientsViewModel
        extends ViewModel
//***********************************************************
{

    public MedicineIngredientsListener mMedicineIngredientsListener;


    //***********************************************************
    public IngredientsViewModel(@NonNull MedicineIngredientsListener mMedicineIngredientsListener)
    //***********************************************************
    {
        this.mMedicineIngredientsListener = mMedicineIngredientsListener;
    }

    //***********************************************************
    public void getIngredients(@NonNull String drugCode)
    //***********************************************************
    {
        val service = ServiceGenerator.createService(MedicationService.class, true,
                Constants.BASE_URL_U);
        if (service == null) {
            mMedicineIngredientsListener.onFailed(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }
        val userResponse = Laila.instance().getMUser_U();
        if (userResponse.getData() == null)
            return;
        val token = Laila.instance().getMUser_U().getData().getUser().getToken();
        HashMap<String, String> medicationIngredients = new HashMap<>();
        medicationIngredients.put("din_number", drugCode);
        medicationIngredients.put(Constants.USER_TOKEN, token);


        val medicationIngredientsService = service.getMedicineIngredients(medicationIngredients);

        medicationIngredientsService.enqueue(new Callback<ActiveIngredientsResponse>() {

            //***********************************************************
            @Override
            public void onResponse(Call<ActiveIngredientsResponse> call, Response<ActiveIngredientsResponse> response)
            //***********************************************************
            {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        mMedicineIngredientsListener.onSuccessfullyGetIngredients(response.body());
                        return;
                    }
                    mMedicineIngredientsListener.onFailed(AndroidUtil.getString(R.string.server_error));
                }
            }

            //***********************************************************
            @Override
            public void onFailure(Call<ActiveIngredientsResponse> call, Throwable t)
            //***********************************************************
            {
                mMedicineIngredientsListener.onFailed(t.getLocalizedMessage());
            }
        });

    }

    //***********************************************************
    public interface MedicineIngredientsListener
            //***********************************************************
    {
        void onSuccessfullyGetIngredients(@Nullable ActiveIngredientsResponse response);

        void onFailed(@NonNull String errorMessage);
    }
}
