package com.fantechlabs.lailaa.bodyreading.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;

import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.activities.BaseActivity;
import com.fantechlabs.lailaa.bodyreading.fragments.HomeFragment;
import com.fantechlabs.lailaa.databinding.ActivityBodyReadingBinding;

import java.util.Calendar;

import lombok.val;

//********************************************************
public class BodyReadingActivity extends BaseActivity
//********************************************************
{
    private ActivityBodyReadingBinding mBinding;
    private HomeFragment mHomeFragment;

    //********************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //********************************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_body_reading);
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("");
        initControl();
    }

    //********************************************************
    private void initControl()
    //********************************************************
    {
        mHomeFragment = new HomeFragment();
        loadFragment(mHomeFragment);
        mBinding.addBodyReading.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), AddBodyReadingActivity.class)));
    }

    //**********************************************
    private void loadFragment(Fragment fragment)
    //**********************************************
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }

    //********************************************************
    @Override
    protected boolean showStatusBar()
    //********************************************************
    {
        return false;
    }
}