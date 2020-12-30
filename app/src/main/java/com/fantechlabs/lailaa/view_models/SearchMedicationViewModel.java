package com.fantechlabs.lailaa.view_models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.models.SearchMedicine;
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
    public void searchMedication(@NonNull String searchMedicine)
    //***********************************************************
    {
        val service = ServiceGenerator.createService(MedicationService.class, true,
                                                     Constants.SEARCH_URL);
        if (service == null)
        {
            mSearchMedicationListener.onSearchFailed(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        val searchMedicationService = service.searchMedication(searchMedicine);

        searchMedicationService.enqueue(new Callback<List<SearchMedicine>>()
        {
            //***********************************************************
            @Override
            public void onResponse(Call<List<SearchMedicine>> call, Response<List<SearchMedicine>> response)
            //***********************************************************
            {
                if (response.isSuccessful())
                {
                        mSearchMedicationListener.onSearchSuccessfully(response.body());
                }
            }

            //***********************************************************
            @Override
            public void onFailure(Call<List<SearchMedicine>> call, Throwable t)
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
                Constants.SEARCH_URL);
        if (service == null)
        {
            mSearchMedicationListener.onSearchFailed(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        val searchMedicationService = service.searchDin(searchDin);

        searchMedicationService.enqueue(new Callback<List<SearchMedicine>>()
        {
            //***********************************************************
            @Override
            public void onResponse(Call<List<SearchMedicine>> call, Response<List<SearchMedicine>> response)
            //***********************************************************
            {
                if (response.isSuccessful())
                {
                    mSearchMedicationListener.onSearchSuccessfully(response.body());
                }
            }

            //***********************************************************
            @Override
            public void onFailure(Call<List<SearchMedicine>> call, Throwable t)
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
        void onSearchSuccessfully(@Nullable List<SearchMedicine> searchMedicationResponse);

        void onSearchFailed(@NonNull String errorMessage);
    }
}
