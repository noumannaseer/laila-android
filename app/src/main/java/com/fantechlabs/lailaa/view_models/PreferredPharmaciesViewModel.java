package com.fantechlabs.lailaa.view_models;

import androidx.lifecycle.ViewModel;

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.models.PreferredPharmacies;
import com.fantechlabs.lailaa.network.ServiceGenerator;
import com.fantechlabs.lailaa.network.services.MedicationService;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;

import java.io.IOException;
import java.util.HashMap;

import lombok.NonNull;
import lombok.val;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//***********************************************
public class PreferredPharmaciesViewModel
        extends ViewModel
//***********************************************
{

    private PreferredPharmaciesViewModelListener mPreferredPharmaciesViewModelListener;

    public PreferredPharmaciesViewModel(PreferredPharmaciesViewModelListener mViewModelListener)
    {
        this.mPreferredPharmaciesViewModelListener = mViewModelListener;
    }

    //*******************************************************************
    public void getPreferredPharmacies(String placeIds)
    //*******************************************************************
    {

        val service = ServiceGenerator.createService(MedicationService.class,
                                                     true,
                                                     Constants.HEALTH_CARE_URL);
        if (service == null)
        {
            mPreferredPharmaciesViewModelListener.onFailed(AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        HashMap<String, String> ids = new HashMap<>();
        ids.put("ids", placeIds);

        val placeServices = service.getPreferredPharmacies(ids);

        //*******************************************************************
        placeServices.enqueue(new Callback<PreferredPharmacies>()
        //*******************************************************************
        {

            //*******************************************************************
            @Override
            public void onResponse(Call<PreferredPharmacies> call, Response<PreferredPharmacies> response)
            //*******************************************************************
            {
                if (response.isSuccessful())
                {
                    mPreferredPharmaciesViewModelListener.onSuccessFullyGetPreferredPharmacies(response.body());

                }
                else
                {
                    String errorMessage = "API error";
                    try
                    {
                        errorMessage = new String(response.errorBody()
                                                          .bytes());
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    if (mPreferredPharmaciesViewModelListener != null)
                        mPreferredPharmaciesViewModelListener.onFailed(errorMessage);

                }
            }

            //*******************************************************************
            @Override
            public void onFailure(Call<PreferredPharmacies> call, Throwable t)
            //*******************************************************************
            {
                if (mPreferredPharmaciesViewModelListener != null)
                    mPreferredPharmaciesViewModelListener.onFailed(t.getLocalizedMessage());

            }
        });

    }

    //******************************************************
    public interface PreferredPharmaciesViewModelListener
    //******************************************************
    {
        void onSuccessFullyGetPreferredPharmacies(@NonNull PreferredPharmacies response);

        void onFailed(@NonNull String message);
    }

}
