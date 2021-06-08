package com.fantechlabs.lailaa.activities;

import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.databinding.ActivityAddMedicationBinding;
import com.fantechlabs.lailaa.fragments.AddMedicationOneFragment;
import com.fantechlabs.lailaa.fragments.AddMedicationThreeFragment;
import com.fantechlabs.lailaa.fragments.AddMedicationTwoFragment;

//****************************************************************
public class AddMedicationActivity extends BaseActivity
//****************************************************************
{

    private ActivityAddMedicationBinding mBinding;


    //****************************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //****************************************************************
    {

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_medication);
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("");
        initControls(savedInstanceState);

    }

    //****************************************************************
    @Override
    protected boolean showStatusBar()
    //****************************************************************
    {
        return false;
    }

    //******************************************************************
    @Override
    protected void onResume()
    //******************************************************************
    {
        super.onResume();
        if (Laila.instance().is_pharmacy_added) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container,
                            new AddMedicationThreeFragment())
                    .commit();
        }
    }

    //****************************************************************
    private void initControls(@Nullable Bundle savedInstanceStates)
    //****************************************************************
    {
        if (savedInstanceStates == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container,
                            new AddMedicationOneFragment())
                    .commit();
        }
    }

    //****************************************************************
    public void navigateToScreen(int index)
    //****************************************************************
    {
        switch (index) {
            case 0:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,
                                new AddMedicationOneFragment())
                        .commit();
                break;
            case 1:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,
                                new AddMedicationTwoFragment())
                        .commit();
                break;
            case 2:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,
                                new AddMedicationThreeFragment())
                        .commit();
                break;
        }
    }


    //******************************************
    @Override
    public void onBackPressed()
    //******************************************
    {
        super.onBackPressed();
        Laila.instance().on_update_medicine = false;
        Laila.instance().setMAddMedicationRequest(null);
        Laila.instance().setMAddMedicationRequestCopy(null);
        Laila.instance().setMUpdateMedication(null);
    }
}
