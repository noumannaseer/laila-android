package com.fantechlabs.lailaa.view_models;

import androidx.lifecycle.ViewModel;

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.models.PreferredPharmacies;
import com.fantechlabs.lailaa.models.updates.response_models.PrefferedPharmacyResponse;
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
    public void getPreferredPharmacies(String userToken)
    //*******************************************************************
    {

        val service = ServiceGenerator.createService(MedicationService.class,
                                                     true,
                                                     Constants.BASE_URL_U);
        if (service == null)
        {
            mPreferredPharmaciesViewModelListener.onFailed(AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        HashMap<String, String> token = new HashMap<>();
        token.put(Constants.USER_TOKEN, userToken);

        val placeServices = service.getPreferredPharmacies(token);

        //*******************************************************************
        placeServices.enqueue(new Callback<PrefferedPharmacyResponse>()
        //*******************************************************************
        {

            //*******************************************************************
            @Override
            public void onResponse(Call<PrefferedPharmacyResponse> call, Response<PrefferedPharmacyResponse> response)
            //*******************************************************************
            {
                if (response.isSuccessful()) {
                    mPreferredPharmaciesViewModelListener.onSuccessFullyGetPreferredPharmacies(response.body());

                } else {
                    String errorMessage = "API error";
                    try {
                        errorMessage = new String(response.errorBody()
                                .bytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (mPreferredPharmaciesViewModelListener != null)
                        mPreferredPharmaciesViewModelListener.onFailed(errorMessage);

                }
            }

            //*******************************************************************
            @Override
            public void onFailure(Call<PrefferedPharmacyResponse> call, Throwable t)
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
        void onSuccessFullyGetPreferredPharmacies(@NonNull PrefferedPharmacyResponse response);

        void onFailed(@NonNull String message);
    }

}
