package com.aditum.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.aditum.R;
import com.aditum.adapter.IngredientListAdapter;
import com.aditum.databinding.ActivityIngredientsBinding;
import com.aditum.models.Ingredient;
import com.aditum.models.updates.response_models.ActiveIngredientsResponse;
import com.aditum.utils.AndroidUtil;
import com.aditum.view_models.IngredientsViewModel;

import java.util.List;

import static com.aditum.activities.InformationActivity.DRUGCODE;
import static com.aditum.utils.AndroidUtil.getContext;

//*********************************************************
public class IngredientsActivity extends BaseActivity
        implements IngredientsViewModel.MedicineIngredientsListener
//*********************************************************
{

    private ActivityIngredientsBinding mBinding;
    private IngredientsViewModel mIngredientsViewModel;
    private IngredientListAdapter mIngredientListAdapter;
    private int mDrugCode;

    //*********************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //*********************************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_ingredients);
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("");
        initControl();
    }

    //*********************************************************
    private void initControl()
    //*********************************************************
    {
        getParcelable();
        mIngredientsViewModel = new IngredientsViewModel(this);
        getIngredients();

    }

    //**************************************************************
    private void getIngredients()
    //**************************************************************
    {
        if (mDrugCode == 0) {
            hideLoadingDialog();
            return;
        }
        showLoadingDialog();
//        mIngredientsViewModel.getIngredients(mDrugCode);
    }

    //******************************************************************
    private void showIngredientsRecyclerView(List<Ingredient> response)
    //******************************************************************
    {
        mBinding.medicineIngredients.setVisibility(View.VISIBLE);
//        mIngredientListAdapter = new IngredientListAdapter(response, this);
        mBinding.ingredientRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.ingredientRecyclerview.setAdapter(mIngredientListAdapter);
    }
//
//    //******************************************************************
//    @Override
//    public void onSuccessfullyGetIngredients(@Nullable List<Ingredient> response)
//    //******************************************************************
//    {
//        hideLoadingDialog();
//        if (response == null || response.size() == 0)
//            return;
//        showIngredientsRecyclerView(response);
//    }
//

    @Override
    public void onSuccessfullyGetIngredients(@Nullable ActiveIngredientsResponse response) {

    }

    //******************************************************************
    @Override
    public void onFailed(@NonNull String errorMessage)
    //******************************************************************
    {
        hideLoadingDialog();
        AndroidUtil.displayAlertDialog(errorMessage, AndroidUtil.getString(R.string.alert), this);
    }

    //******************************************************************
    private void getParcelable()
    //******************************************************************
    {
        if (getIntent().getExtras()
                .containsKey(DRUGCODE)) {
            mDrugCode = getIntent().getIntExtra(DRUGCODE, 0);
        }
    }

    //*********************************************************
    @Override
    protected boolean showStatusBar()
    //*********************************************************
    {
        return false;
    }
}