package com.fantechlabs.lailaa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.adapter.ProfileViewPagerAdapter;
import com.fantechlabs.lailaa.databinding.ActivityResources1Binding;
import com.fantechlabs.lailaa.fragments.HandoutsFragment;
import com.fantechlabs.lailaa.fragments.ReadingMaterialsFragment;
import com.fantechlabs.lailaa.models.allergie_models.AllergieListerner;
import com.fantechlabs.lailaa.models.allergie_models.XmlParcer;
import com.fantechlabs.lailaa.models.updates.response_models.ProfileResponse;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;
import com.fantechlabs.lailaa.utils.SharedPreferencesUtils;
import com.fantechlabs.lailaa.view_models.UpdateProfileViewModel;

import lombok.val;

//*****************************************************
public class ResourcesActivity1 extends BaseActivity
        //*****************************************************
{
    private ActivityResources1Binding mBinding;
    private ProfileViewPagerAdapter mProfileViewPagerAdapter;

    //*****************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //*****************************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_resources_1);
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("");
        initControls();
    }

    //***********************************************************
    private void initControls()
    //***********************************************************
    {
        searchResources();
        setupViewPager();
        mBinding.resourcesTab.setupWithViewPager(mBinding.viewPager);

    }

    //*******************************************************************
    private void searchResources()
    //*******************************************************************
    {
        mBinding.search.setOnClickListener(v -> {
            val diseaseName = mBinding.diseaseName.getText().toString();
            if (TextUtils.isEmpty(diseaseName)) {
                AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.disease_name_required), AndroidUtil.getString(R.string.alert), ResourcesActivity1.this);
                return;
            }
            showLoadingDialog();
            new XmlParcer(new AllergieListerner() {
                //***********************************************************
                @Override
                public void onExecutionCompleted()
                //***********************************************************
                {
                    hideLoadingDialog();
                    Intent intent = new Intent(ResourcesActivity1.this, AlergieResourceDetails.class);
                    intent.putExtra(Constants.DISEASE_NAME, diseaseName);
                    startActivity(intent);
                }

                //***********************************************************
                @Override
                public void onExecutionFailed()
                //***********************************************************
                {
                    hideLoadingDialog();
                    Intent intent = new Intent(ResourcesActivity1.this, AlergieResourceDetails.class);
                    startActivity(intent);
                }
            }).execute(Constants.BASE_URL_Terms + diseaseName);
        });
    }

    //*******************************************************************
    @Override
    public void onResume()
    //*******************************************************************
    {
        super.onResume();
    }

    //*****************************************************************
    private void setupViewPager()
    //*****************************************************************
    {
        mProfileViewPagerAdapter = new ProfileViewPagerAdapter(getSupportFragmentManager());
        mProfileViewPagerAdapter.addFragment(new ReadingMaterialsFragment(), AndroidUtil.getString(R.string.reading_material));
        mProfileViewPagerAdapter.addFragment(new HandoutsFragment(), AndroidUtil.getString(R.string.handouts));
        mBinding.viewPager.setAdapter(mProfileViewPagerAdapter);
    }

    //*****************************************************
    @Override
    protected boolean showStatusBar()
    //*****************************************************
    {
        return false;
    }
}