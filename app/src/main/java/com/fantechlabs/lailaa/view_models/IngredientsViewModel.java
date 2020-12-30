package com.fantechlabs.lailaa.view_models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.models.Ingredient;
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
    public void getIngredients(@NonNull Integer drugCode)
    //***********************************************************
    {
        val service = ServiceGenerator.createService(MedicationService.class, true,
                Constants.MEDICINE_INFO_URL);
        if (service == null)
        {
            mMedicineIngredientsListener.onFailed(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        val url  = Constants.MEDICINE_INFO_URL + "activeingredient/" + drugCode;

        val medicationInformationService = service.getMedicineIngredients(url);

        medicationInformationService.enqueue(new Callback<List<Ingredient>>()
        {

            //***********************************************************
            @Override
            public void onResponse(Call<List<Ingredient>> call, Response<List<Ingredient>> response)
            //***********************************************************
            {
                if (response.isSuccessful())
                {
                    if (response.body() != null)
                    {
                        mMedicineIngredientsListener.onSuccessfullyGetIngredients(response.body());
                        return;
                    }
                    mMedicineIngredientsListener.onFailed(AndroidUtil.getString(R.string.server_error));
                }
            }

            //***********************************************************
            @Override
            public void onFailure(Call<List<Ingredient>> call, Throwable t)
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
        void onSuccessfullyGetIngredients(@Nullable List<Ingredient> response);

        void onFailed(@NonNull String errorMessage);
    }
}
