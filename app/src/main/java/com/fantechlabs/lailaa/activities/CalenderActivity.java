package com.fantechlabs.lailaa.activities;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.databinding.ActivityCalenderBinding;
import com.fantechlabs.lailaa.databinding.ActivityMedicationBinding;

//******************************************************************
public class CalenderActivity extends BaseActivity
//******************************************************************
{
    private ActivityCalenderBinding mBinding;

    //******************************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //******************************************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_calender);
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("");
     }
    //******************************************************************
    @Override
    protected boolean showStatusBar()
    //******************************************************************
    {
        return false;
    }
}