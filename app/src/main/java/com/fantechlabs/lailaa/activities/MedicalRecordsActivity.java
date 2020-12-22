package com.fantechlabs.lailaa.activities;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.databinding.ActivityMedicalRecordsBinding;
import com.fantechlabs.lailaa.databinding.ActivityMedicationBinding;

//******************************************************************************
public class MedicalRecordsActivity extends BaseActivity

//******************************************************************************
{
    private ActivityMedicalRecordsBinding mBinding;

    //******************************************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //******************************************************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_medical_records);
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("");
    }

    //******************************************************************************
    @Override
    protected boolean showStatusBar()
    //******************************************************************************
    {
        return false;
    }
}