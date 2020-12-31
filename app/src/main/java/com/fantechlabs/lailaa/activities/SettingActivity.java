package com.fantechlabs.lailaa.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.alarms.DatabaseHelper;
import com.fantechlabs.lailaa.databinding.ActivitySettingBinding;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;
import com.fantechlabs.lailaa.utils.SharedPreferencesUtils;
import com.fantechlabs.lailaa.utils.UIUtils;

import lombok.val;

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
        intiControls();
    }

    //****************************************************************
    private void intiControls()
    //****************************************************************
    {
        mBinding.termsCondition.setOnClickListener(
                v -> moveToBrowserActivity(getString(R.string.settings_terms_and_conditions),
                        getString(R.string.terms_conditions_url)));
        mBinding.privacyPolicy.setOnClickListener(
                v -> moveToBrowserActivity(getString(R.string.settings_privacy_policy),
                        getString(R.string.privacy_policy_url)));
        mBinding.faq.setOnClickListener(view -> {
        });
        mBinding.notifications.setOnClickListener(view -> {
        });
        mBinding.aboutUs.setOnClickListener(view -> {
        });
        mBinding.contacts.setOnClickListener(view -> {
            startActivity(new Intent(SettingActivity.this, ContactTypesHomeActivity.class));
        });
        mBinding.editProfile.setOnClickListener(view -> editProfile());
        mBinding.logout.setOnClickListener(v -> logout());
    }

    //********************************************
    private void moveToBrowserActivity(String title, String url)
    //********************************************
    {
        Intent browserIntent = new Intent(SettingActivity.this, BrowserActivity.class);
        browserIntent.putExtra(BrowserActivity.SCREEN_URL, url);
        browserIntent.putExtra(BrowserActivity.INFO_TITLE, title);
        startActivity(browserIntent);
    }

    //******************************************************************
    private void editProfile()
    //******************************************************************
    {
        startActivity(new Intent(this, ProfileActivity.class));
    }

    //******************************************************************
    private void logout()
    //******************************************************************
    {

        val rememberMe = SharedPreferencesUtils.getBoolean(Constants.REMEMBER);
        if (rememberMe) {
            SharedPreferencesUtils.setValue(Constants.EMAIL, Laila.instance().getCurrentUserProfile().getEmail());
            SharedPreferencesUtils.setValue(Constants.PASSWORD, Laila.instance().getCurrentUserProfile().getPassword());
        }

        UIUtils.displayAlertDialog(
                AndroidUtil.getString(R.string.logout),
                AndroidUtil.getString(R.string.menu_logout),
                this,
                AndroidUtil.getString(
                        R.string.ok),
                AndroidUtil.getString(
                        R.string.cancel),
                (dialog, which) -> {
                    if (which == -1) {
                        deleteAlarms();
                        SharedPreferencesUtils.setValue(Constants.USER_DATA, (Object) null);
                        SharedPreferencesUtils.setValue(Constants.REMEMBER, false);
                        Intent logoutIntent = new Intent(this, SignInActivity.class);
                        logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(logoutIntent);
                        finish();
                    }
                });

    }

    //**********************************
    private void deleteAlarms()
    //**********************************
    {
        val alarms = DatabaseHelper.getInstance(this).getAlarms();
        if (alarms != null) {
            for (val alarm : alarms) {
                DatabaseHelper.getInstance(this).deleteAlarm(alarm);
            }
        }
    }

    //****************************************************************
    @Override
    protected boolean showStatusBar()
    //****************************************************************
    {
        return false;
    }

}
