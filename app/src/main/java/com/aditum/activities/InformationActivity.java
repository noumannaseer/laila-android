package com.aditum.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aditum.R;
import com.aditum.adapter.IngredientListAdapter;
import com.aditum.adapter.MedicineInformationListAdapter;
import com.aditum.databinding.ActivityInformationBinding;
import com.aditum.models.updates.response_models.ActiveIngredientsResponse;
import com.aditum.models.updates.response_models.MedicineInfoResponse;
import com.aditum.utils.AndroidUtil;
import com.aditum.view_models.IngredientsViewModel;
import com.aditum.view_models.MedicineInformationViewModel;

import lombok.val;

import static com.aditum.utils.AndroidUtil.getContext;

//******************************************************************
public class InformationActivity extends BaseActivity
        implements MedicineInformationViewModel.MedicineInformationListener,
        IngredientsViewModel.MedicineIngredientsListener
//******************************************************************
{
    private ActivityInformationBinding mBinding;
    public static final String RXDINNUMBER = "RXDINNUMBER";
    public static final String DRUGCODE = "DRUGCODE";
    private String mRxDinNumber;
    private MedicineInformationViewModel mMedicineInformationViewModel;
    private MedicineInformationListAdapter mMedicineInformationListAdapter;
    private int mDrugCode;
    private IngredientsViewModel mIngredientsViewModel;
    private IngredientListAdapter mIngredientListAdapter;

    //******************************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //******************************************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_information);
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("");
        initControl();
    }

    //******************************************************************
    @Override
    protected boolean showStatusBar()
    //******************************************************************
    {
        return false;
    }

    //******************************************************************
    private void initControl()
    //******************************************************************
    {
        getParcelable();
        mIngredientsViewModel = new IngredientsViewModel(this);
        mMedicineInformationViewModel = new MedicineInformationViewModel(this);
        showLoadingDialog();
        if (mRxDinNumber.length() == 0 || mRxDinNumber == null)
            return;
        mMedicineInformationViewModel.getMedicineInformation(mRxDinNumber);
    }

    //******************************************************************
    private void gotoIngredientsScreen()
    //******************************************************************
    {
        mBinding.ingredients.setVisibility(View.VISIBLE);

        mBinding.ingredients.setOnClickListener(view -> {
            Intent informationIntent = new Intent(InformationActivity.this, IngredientsActivity.class);
            informationIntent.putExtra(InformationActivity.DRUGCODE, mDrugCode);
            startActivity(informationIntent);
        });

    }

    //******************************************************************
    @Override
    public void onSuccessfully(@Nullable MedicineInfoResponse response)
    //******************************************************************
    {
        if (response == null)
            return;
        mBinding.medicineInformation.setVisibility(View.VISIBLE);
        showMedicineInfoRecyclerView(response);
    }

    //**************************************************************
    private void showMedicineInfoRecyclerView(MedicineInfoResponse response)
    //**************************************************************
    {
        if (response == null || response.getData().getMedicationInfo() == null) {
            hideLoadingDialog();
            val responseMessage = response.getData().getMessage();
            if (responseMessage != null)
                AndroidUtil.displayAlertDialog(responseMessage,
                        AndroidUtil.getString(R.string.alert),
                        this, "Ok", (dialogInterface, i) -> {
                            mBinding.noDataFound.setVisibility(View.VISIBLE);
                            mBinding.main.setVisibility(View.GONE);
                        });
            return;
        }
        val medicineInfoList = response.getData().getMedicationInfo();
        mBinding.main.setVisibility(View.VISIBLE);
        mBinding.noDataFound.setVisibility(View.GONE);
        mMedicineInformationListAdapter = new MedicineInformationListAdapter(medicineInfoList, this);
        mBinding.medicineInfoRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.medicineInfoRecyclerview.setAdapter(mMedicineInformationListAdapter);

        val drugIdentification = response.getData().getMedicationInfo().get(0).getDrugIdentificationNumber();
        getIngredients(drugIdentification);
    }

    //**************************************************************
    private void getIngredients(String drugIdentification)
    //**************************************************************
    {
        if (drugIdentification == null) {
            hideLoadingDialog();
            return;
        }
        mIngredientsViewModel.getIngredients(drugIdentification);
    }

    //******************************************************************
    @Override
    public void onFailed(@NonNull String errorMessage)
    //******************************************************************
    {
        hideLoadingDialog();
        mBinding.medicineInformation.setVisibility(View.GONE);
        mBinding.main.setVisibility(View.GONE);
        mBinding.noDataFound.setVisibility(View.VISIBLE);
        AndroidUtil.displayAlertDialog(errorMessage, AndroidUtil.getString(R.string.alert), this);
    }

    //******************************************************************
    @Override
    public void onSuccessfullyGetIngredients(@Nullable ActiveIngredientsResponse response)
    //******************************************************************
    {
        hideLoadingDialog();
        if (response == null || response.getData().getActiveIngredient().size() == 0)
            return;
        showIngredientsRecyclerView(response);
    }

    //******************************************************************
    private void showIngredientsRecyclerView(ActiveIngredientsResponse response)
    //******************************************************************
    {
        mBinding.medicineIngredients.setVisibility(View.VISIBLE);
        if (response == null || response.getData().getActiveIngredient() == null)
            return;
        val ingredientsList = response.getData().getActiveIngredient();
        mIngredientListAdapter = new IngredientListAdapter(ingredientsList, this);
        mBinding.ingredientRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.ingredientRecyclerview.setAdapter(mIngredientListAdapter);
    }


    //******************************************************************
    private void getParcelable()
    //******************************************************************
    {
        if (getIntent().getExtras()
                .containsKey(RXDINNUMBER)) {
            mRxDinNumber = getIntent().getStringExtra(RXDINNUMBER);
        }
    }
}