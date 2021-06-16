package com.aditum.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;


import com.aditum.R;
import com.aditum.databinding.ActivityTelehealthBinding;

import java.util.Objects;

//***************************************************************
public class TelehealthActivity extends BaseActivity
//***************************************************************
{
    private ActivityTelehealthBinding mBinding;

    //***************************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //***************************************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_telehealth);
        setToolBar(mBinding.toolbar);
     }

    //**********************************************************
    public void setToolBar(Toolbar toolBar)
    //**********************************************************
    {
        setSupportActionBar(toolBar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    //***************************************************************
    @Override
    protected boolean showStatusBar()
    //***************************************************************
    {
        return false;
    }
}