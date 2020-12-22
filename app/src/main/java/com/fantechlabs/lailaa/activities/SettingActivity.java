package com.fantechlabs.lailaa.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.databinding.ActivitySettingBinding;
import com.fantechlabs.lailaa.databinding.ActivitySigninBinding;

//****************************************************************
public class SettingActivity extends BaseActivity
//****************************************************************
{

    private ActivitySettingBinding mBinding;

    //****************************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //****************************************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("");
    }

    //****************************************************************
    @Override
    protected boolean showStatusBar()
    //****************************************************************
    {
        return false;
    }

}
