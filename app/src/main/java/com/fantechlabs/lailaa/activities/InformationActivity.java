package com.fantechlabs.lailaa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fantechlabs.lailaa.IngredientsActivity;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.adapter.IngredientListAdapter;
import com.fantechlabs.lailaa.adapter.MedicineInformationListAdapter;
import com.fantechlabs.lailaa.databinding.ActivityInformationBinding;
import com.fantechlabs.lailaa.models.Ingredient;
import com.fantechlabs.lailaa.models.response_models.MedicineInformationResponse;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.view_models.IngredientsViewModel;
import com.fantechlabs.lailaa.view_models.MedicineInformationViewModel;

import java.util.List;

import lombok.val;

import static com.fantechlabs.lailaa.utils.AndroidUtil.getContext;

//******************************************************************
public class InformationActivity extends BaseActivity
        implements MedicineInformationViewModel.MedicineInformationListener
//******************************************************************
{
    private ActivityInformationBinding mBinding;
    public static final String RXDINNUMBER = "RXDINNUMBER";
    public static final String DRUGCODE = "DRUGCODE";
    private String mRxDinNumber;
    private MedicineInformationViewModel mMedicineInformationViewModel;
    private MedicineInformationListAdapter mMedicineInformationListAdapter;
    private int mDrugCode;

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
    public void onSuccessfully(@Nullable List<MedicineInformationResponse> response)
    //******************************************************************
    {
        hideLoadingDialog();
        if (response == null)
            return;
        mBinding.medicineInformation.setVisibility(View.VISIBLE);
        showMedicineInfoRecyclerView(response);
        gotoIngredientsScreen();
    }

    //**************************************************************
    private void showMedicineInfoRecyclerView(List<MedicineInformationResponse> response)
    //**************************************************************
    {
        if (response == null || response.size() == 0) {
            mBinding.noDataFound.setVisibility(View.VISIBLE);
            mBinding.main.setVisibility(View.GONE);
            return;
        }
        mBinding.main.setVisibility(View.VISIBLE);
        mBinding.noDataFound.setVisibility(View.GONE);
        mMedicineInformationListAdapter = new MedicineInformationListAdapter(response, this);
        mBinding.medicineInfoRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.medicineInfoRecyclerview.setAdapter(mMedicineInformationListAdapter);

        mDrugCode = response.get(0).getDrugCode();
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
    private void getParcelable()
    //******************************************************************
    {
        if (getIntent().getExtras()
                .containsKey(RXDINNUMBER)) {
            mRxDinNumber = getIntent().getStringExtra(RXDINNUMBER);
        }
    }
}