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
import com.fantechlabs.lailaa.databinding.FragmentAddMedicationBinding;
import com.fantechlabs.lailaa.models.updates.models.SearchMedication;
import com.fantechlabs.lailaa.models.updates.response_models.SearchMedicationResponse;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.view_models.SearchMedicationViewModel;

import java.util.ArrayList;
import java.util.List;

import lombok.val;

import static com.fantechlabs.lailaa.utils.Constants.HUMAN;

//***********************************************************
public class AddMedicationFragment
        extends BaseFragment
        implements SearchMedicationViewModel.SearchMedicationListener
//***********************************************************

{
    private FragmentAddMedicationBinding mBinding;
    private View mRootView;
    private SearchMedicationViewModel mSearchMedicationViewModel;
    private ArrayList<String> mSearchResult;
    private List<SearchMedication> mSearchMedication;
    private Runnable mSearchRunnable;

    //***********************************************************
    public AddMedicationFragment()
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
            mBinding = FragmentAddMedicationBinding.inflate(inflater, parent, false);
            mRootView = mBinding.getRoot();
            mSearchMedicationViewModel = new SearchMedicationViewModel(this);
            initControls();
        }
        return mRootView;
    }

    //**********************************************************
    private void initControls()
    //**********************************************************
    {
        mSearchMedication = new ArrayList<>();
        searchMedicine();
        addMedication();
    }

    //***************************************************************
    private void addMedication()
    //***************************************************************
    {
        Laila.instance().mAutoCompleteLoadingBar.setLoadingIndicator(mBinding.loadingIndicator);
        mBinding.searchMedication.setOnItemClickListener((parent, view, position, id) ->
        {
            Laila.instance()
                    .setMSearchMedicine_U(null);
            Laila.instance().setMSearchMedicine_U(mSearchMedication.get(position));
            goToAddMedication();

        });
        mBinding.addManuallyButton.setOnClickListener(v ->
        {
            Laila.instance()
                    .setMSearchMedicine_U(null);
            goToAddMedication();
        });
    }

    //***************************************************************
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    //***************************************************************
    {
        if (isVisibleToUser) {
            if (Laila.instance().text_recognizer) {
                val searchData = Laila.instance().getMSearchData();
                if (searchData != null || searchData.length() != 0) {
                    mBinding.searchMedication.setText(searchData);
                    Laila.instance().mAutoCompleteLoadingBar.dispayIndicator();
                }
                Laila.instance().text_recognizer = false;
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    //**********************************************************
    private void searchMedicine()
    //**********************************************************
    {
        mBinding.searchMedication.addTextChangedListener(new TextWatcher() {

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
                if (s.length() > 3) {
                    mSearchRunnable = () -> {
                        Laila.instance().mAutoCompleteLoadingBar.dispayIndicator();
                        if (Laila.instance().from_image_din) {
                            mSearchMedicationViewModel.searchDin(s.toString());
                            Laila.instance().from_image_din = false;
                            return;
                        }
                        mSearchMedicationViewModel.searchMedication(s.toString());
                    };

                    AndroidUtil.handler.postDelayed(mSearchRunnable, 100);
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

    //*****************************************************************************
    @Override
    public void onSearchSuccessfully(@Nullable SearchMedicationResponse searchMedicationResponse)
    //*****************************************************************************
    {
        Laila.instance().mAutoCompleteLoadingBar.removeIndicator();
        if (searchMedicationResponse == null || searchMedicationResponse.getData().getSearchMedications().size() == 0) {
            AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.there_is_not_any_rx_din), AndroidUtil.getString(R.string.alert), getContext());
            return;
        }

        mSearchResult = new ArrayList<>();
        mSearchMedication = searchMedicationResponse.getData().getSearchMedications();
        for (SearchMedication item : mSearchMedication) {
            if (item.getClassName().equals(HUMAN))
                mSearchResult.add(item.getBrandName() + "\n"
                        + AndroidUtil.getString(R.string.din_rx) + item.getDrugIdentificationNumber()
                        + "\n"
                        + AndroidUtil.getString(R.string.medicine_class) + item.getClassName());
        }

        startAutoCompleteView(mSearchResult.toArray(new String[0]));
    }

    //*****************************************************************************
    @Override
    public void onSearchFailed(@NonNull String errorMessage) {
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
        mBinding.searchMedication.setThreshold(1);
        mBinding.searchMedication.setAdapter(adapter);
        mBinding.searchMedication.showDropDown();
        mSearchResult.clear();
    }

    //**************************************************************
    private void goToAddMedication()
    //**************************************************************
    {
        mSearchMedication.clear();
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
}
