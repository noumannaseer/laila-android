package com.fantechlabs.lailaa.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.adapter.MedicationDetailsAdapter;
import com.fantechlabs.lailaa.databinding.ActivityMedicationDetailsBinding;
import com.google.android.material.tabs.TabLayout;


//****************************************************************
public class MedicationDetailsActivity extends BaseActivity
//****************************************************************

{

    private ActivityMedicationDetailsBinding mBinding;


    //****************************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //****************************************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_medication_details);
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("");
        initControls();
    }

    //****************************************************************
    @Override
    protected boolean showStatusBar()
    //****************************************************************
    {
        return false;
    }

    //****************************************************************
    private void initControls()
    //****************************************************************
    {
        mBinding.viewpager.setAdapter(new MedicationDetailsAdapter(getSupportFragmentManager(), mBinding.tabLayout.getTabCount()));
        mBinding.viewpager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mBinding.tabLayout));
        mBinding.tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            //****************************************************************
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            //****************************************************************
            {
                mBinding.viewpager.setCurrentItem(tab.getPosition());
            }

            //****************************************************************
            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            //****************************************************************
            {

            }

            //****************************************************************
            @Override
            public void onTabReselected(TabLayout.Tab tab)
            //****************************************************************
            {

            }
        });
    }

    //****************************************************************
    @Override
    protected void onResume()
    //****************************************************************
    {
        super.onResume();
        if (Laila.instance().Bar_code)
            mBinding.viewpager.setCurrentItem(2);
        if (Laila.instance().text_recognizer)
            mBinding.viewpager.setCurrentItem(0);
    }

    //****************************************************************
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    //****************************************************************
    {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //****************************************
    @Override
    public void onBackPressed()
    //****************************************
    {
        super.onBackPressed();
        Laila.instance().on_update_medicine = false;
    }
}
