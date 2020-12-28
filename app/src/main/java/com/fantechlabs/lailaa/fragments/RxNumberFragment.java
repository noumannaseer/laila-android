package com.fantechlabs.lailaa.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.activities.AddMedicationActivity;
import com.fantechlabs.lailaa.databinding.FragmentRxNumberBinding;
import com.fantechlabs.lailaa.models.SearchMedicine;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.view_models.SearchMedicationViewModel;
import com.fantechlabs.lailaa.view_models.UPCViewModel;

import java.util.ArrayList;
import java.util.List;

import lombok.val;


//***********************************************************
public class RxNumberFragment
        extends BaseFragment
        implements SearchMedicationViewModel.SearchMedicationListener,
        UPCViewModel.UPCViewModelListener
//***********************************************************

{

    private FragmentRxNumberBinding mBinding;
    private View mRootView;
    private SearchMedicationViewModel mSearchMedicationViewModel;
    private UPCViewModel mUpcViewModel;
    private ArrayList<String> mSearchResult;
    private List<SearchMedicine> mSearchMedication;
    private Runnable mSearchRunnable;


    //***********************************************************
    public RxNumberFragment()
    //***********************************************************

    {
        // Required empty public constructor
    }

    //*********************************************************************************************************
    @Override
    public View onCreateViewBaseFragment(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    //*********************************************************************************************************
    {
        if (mRootView == null) {
            mBinding = FragmentRxNumberBinding.inflate(inflater, parent, false);
            mRootView = mBinding.getRoot();
            initControls();
        }
        return mRootView;
    }

    //**********************************************************
    private void initControls()
    //**********************************************************
    {
        mSearchMedicationViewModel = new SearchMedicationViewModel(this);
        mUpcViewModel = new UPCViewModel(this);
        searchMedicine();

        Laila.instance().mAutoCompleteLoadingBar.setLoadingIndicator(mBinding.loadingIndicator);

        mBinding.rxNumber.setOnItemClickListener((parent, view, position, id) ->
        {
            Laila.instance()
                    .setMSearchMedicine(null);
            Laila.instance()
                    .setMSearchMedicine(
                            mSearchMedication.get(position));
            goToAddMedication();
        });

        mBinding.addManuallyButton.setOnClickListener(v ->
        {
            Laila.instance()
                    .setMSearchMedicine(null);
            goToAddMedication();
        });
    }

    //**********************************************************
    private void searchMedicine()
    //**********************************************************
    {
        mBinding.rxNumber.addTextChangedListener(new TextWatcher() {

            //*****************************************************************************
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            //*****************************************************************************
            {
            }

            //*****************************************************************************
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            //*****************************************************************************
            {
                AndroidUtil.handler.removeCallbacks(mSearchRunnable);
                if (s.length() > 2) {
                    mSearchRunnable = () ->
                    {
                        Laila.instance().mAutoCompleteLoadingBar.dispayIndicator();
                        mSearchMedicationViewModel.searchDin(s.toString());
                    };

                    AndroidUtil.handler.postDelayed(mSearchRunnable, 200);
                }
            }

            //*****************************************************************************
            @Override
            public void afterTextChanged(Editable s)
            //*****************************************************************************
            {
            }
        });
    }

    //***************************************************************
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    //***************************************************************
    {
        if (isVisibleToUser) {
            if (Laila.instance().Bar_code) {
                val searchData = Laila.instance().getMSearchData();
                if (searchData != null || searchData.length() != 0) {
                    showLoadingDialog();
                    mUpcViewModel.checkUPC(searchData);
                }
                Laila.instance().Bar_code = false;
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    //*****************************************************************************
    @Override
    public void onSearchSuccessfully(@Nullable List<SearchMedicine> searchMedicationResponse)
    //*****************************************************************************
    {
        Laila.instance().mAutoCompleteLoadingBar.removeIndicator();
        if (searchMedicationResponse == null || searchMedicationResponse.size() == 0)
            return;

        mSearchResult = new ArrayList<>();
        mSearchMedication = searchMedicationResponse;
        for (SearchMedicine item : searchMedicationResponse) {
            mSearchResult.add(item.getBrandName() + "\n"
                    + AndroidUtil.getString(R.string.din_rx) + item.getDrugIdentificationNumber());
        }

        startAutoCompleteView(mSearchResult.toArray(new String[0]));

    }

    //*****************************************************************************
    @Override
    public void onSearchFailed(@NonNull String errorMessage)
    //*****************************************************************************
    {
        Laila.instance().mAutoCompleteLoadingBar.removeIndicator();
        AndroidUtil.toast(false, errorMessage);
    }

    //**************************************************************
    private void startAutoCompleteView(String[] searchResults)
    //**************************************************************
    {
        val activity = getActivity();
        if (activity == null)
            return;

        if (searchResults == null || searchResults.length == 0)
            return;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                android.R.layout.select_dialog_item,
                searchResults);

        mBinding.rxNumber.setThreshold(1);
        mBinding.rxNumber.setAdapter(adapter);
        mBinding.rxNumber.showDropDown();
    }

    //**************************************************************
    private void goToAddMedication()
    //**************************************************************
    {
        Intent addMedicationIntent = new Intent(getContext(), AddMedicationActivity.class);
        getContext().startActivity(addMedicationIntent);
    }

    //**************************************************************
    @Override
    public void onDestroy()
    //**************************************************************
    {
        super.onDestroy();
    }

    //**************************************************************
    @Override
    public void onSuccess(@Nullable List<SearchMedicine> response)
    //**************************************************************
    {
        hideLoadingDialog();

        if (response == null || response.size() == 0)
            return;
        mSearchResult = new ArrayList<>();
        mSearchMedication = response;
        for (SearchMedicine item : response) {
            mSearchResult.add(item.getTitle() + "\n"
                    + AndroidUtil.getString(R.string.din_rx) + item.getUpc());
        }

        startAutoCompleteView(mSearchResult.toArray(new String[0]));
    }

    //**************************************************************
    @Override
    public void onFailed(String message)
    //**************************************************************
    {
        hideLoadingDialog();
        AndroidUtil.displayAlertDialog(message, AndroidUtil.getString(R.string.error), getActivity());
    }
}

