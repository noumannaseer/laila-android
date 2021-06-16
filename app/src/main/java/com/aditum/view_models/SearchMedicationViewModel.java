package com.aditum.view_models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.aditum.Laila;
import com.aditum.R;
import com.aditum.models.updates.response_models.SearchMedicationResponse;
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
public class SearchMedicationViewModel
        extends ViewModel
//***********************************************************
{
    public SearchMedicationListener mSearchMedicationListener;

    //***********************************************************
    public SearchMedicationViewModel(@NonNull SearchMedicationListener mSearchMedicationListener)
    //***********************************************************
    {
        this.mSearchMedicationListener = mSearchMedicationListener;
    }

    //***********************************************************
    public void searchMedication(String medicationName)
    //***********************************************************
    {
        val service = ServiceGenerator.createService(MedicationService.class, true,
                Constants.BASE_URL_U);
        if (service == null) {
            mSearchMedicationListener.onSearchFailed(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }
        if (Laila.instance().getMUser_U() == null)
            return;
        if (Laila.instance().getMUser_U().getData() == null)
            return;
        val token = Laila.instance().getMUser_U().getData().getUser().getToken();
        HashMap<String, String> searchMedicine = new HashMap<>();
        searchMedicine.put(Constants.USER_TOKEN, token);
        searchMedicine.put(Constants.MEDICATION_NAME, medicationName);
        val searchMedicationService = service.searchMedication(searchMedicine);

        searchMedicationService.enqueue(new Callback<SearchMedicationResponse>() {
            //***********************************************************
            @Override
            public void onResponse(Call<SearchMedicationResponse> call, Response<SearchMedicationResponse> response)
            //***********************************************************
            {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 200) {
                        mSearchMedicationListener.onSearchSuccessfully(response.body());
                        return;
                    }
                    mSearchMedicationListener.onSearchFailed(AndroidUtil.getString(R.string.server_error));
                }
            }

            //***********************************************************
            @Override
            public void onFailure(Call<SearchMedicationResponse> call, Throwable t)
            //***********************************************************
            {
                mSearchMedicationListener.onSearchFailed(t.getLocalizedMessage());
            }
        });

    }

    //***********************************************************
    public void searchDin(@NonNull String searchDin)
    //***********************************************************
    {
        val service = ServiceGenerator.createService(MedicationService.class, true,
                Constants.BASE_URL_U);
        if (service == null) {
            mSearchMedicationListener.onSearchFailed(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }
        val token = Laila.instance().getMUser_U().getData().getUser().getToken();
        HashMap<String, String> searchMedicine = new HashMap<>();
        searchMedicine.put(Constants.USER_TOKEN, token);
        searchMedicine.put(Constants.DIN_RX_NUMBER, searchDin);

        val searchMedicationService = service.searchDin(searchMedicine);

        searchMedicationService.enqueue(new Callback<SearchMedicationResponse>() {
            //***********************************************************
            @Override
            public void onResponse(Call<SearchMedicationResponse> call, Response<SearchMedicationResponse> response)
            //***********************************************************
            {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 200) {
                        mSearchMedicationListener.onSearchSuccessfully(response.body());
                        return;
                    }
                    mSearchMedicationListener.onSearchFailed(AndroidUtil.getString(R.string.server_error));
                }
//                val error = NetworkUtils.errorResponse(response.errorBody());
//                mSearchMedicationListener.onSearchFailed(error);
            }

            //***********************************************************
            @Override
            public void onFailure(Call<SearchMedicationResponse> call, Throwable t)
            //***********************************************************
            {
                mSearchMedicationListener.onSearchFailed(t.getLocalizedMessage());
            }
        });

    }

    //***********************************************************
    public interface SearchMedicationListener
            //***********************************************************
    {
        void onSearchSuccessfully(@Nullable SearchMedicationResponse searchMedicationResponse);

        void onSearchFailed(@NonNull String errorMessage);
    }
}
