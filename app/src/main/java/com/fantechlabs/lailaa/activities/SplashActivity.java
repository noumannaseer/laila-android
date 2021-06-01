package com.fantechlabs.lailaa.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.models.updates.response_models.ProfileResponse;
import com.fantechlabs.lailaa.models.updates.response_models.UserResponse;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;
import com.fantechlabs.lailaa.utils.SharedPreferencesUtils;
import com.fantechlabs.lailaa.utils.permissions.Permission;

import com.fantechlabs.lailaa.view_models.UpdateProfileViewModel;

import lombok.val;

//**********************************************************
public class SplashActivity extends AppCompatActivity
        implements Permission.OnResult, Permission.OnDenied,
        UpdateProfileViewModel.UpdateProfileListener
//**********************************************************
{
    private Permission mPermission;
    private UpdateProfileViewModel mUpdateProfileViewModel;

    //**********************************************************
    @Override
    protected void onCreate(Bundle savedInstanceState)
    //**********************************************************
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (AndroidUtil.isNetworkStatusAvailable()) {
            initPermission();
            return;
        }
        AndroidUtil.displayAlertDialog(
                AndroidUtil.getString(R.string.Network_not_available),
                AndroidUtil.getString(R.string.Network_error),
                this);
    }

    //******************************************************************
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    //******************************************************************
    {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            mPermission.request(this);
    }

    //*******************************************************************
    private void initViewModels()
    //*******************************************************************
    {
        mUpdateProfileViewModel = new UpdateProfileViewModel(this);
    }

    //*******************************************************************
    private void getProfile()
    //*******************************************************************
    {
        mUpdateProfileViewModel.getProfile();
    }

    //***************************************************************
    private void initControls()
    //***************************************************************
    {
        initViewModels();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //***************************************************************
        new Handler().postDelayed(() ->
                //***************************************************************
        {
            goToDashboard();
        }, 1000);
    }

    //*********************************************
    private void goToDashboard()
    //*********************************************
    {

        UserResponse userResponse = SharedPreferencesUtils.getModel(UserResponse.class, Constants.USER_DATA);

        if (userResponse != null) {
            Laila.instance().setMUser_U(userResponse);
            getProfile();
            if (Laila.instance().getMUser_U().getData().getUser() == null)
                return;
            val user_id = Laila.instance().getMUser_U().getData().getUser().getId();

            if (user_id != null) {
                Intent afterSplash = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(afterSplash);
                afterSplash.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                return;
            }
        }
        introScreen();
    }

    //**************************************************
    private void introScreen()
    //**************************************************
    {
        Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
        startActivity(intent);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
    }

    //*********************************************************************
    private void initPermission()
    //*********************************************************************
    {
        mPermission = new Permission(
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, SplashActivity.this);
        mPermission.request(SplashActivity.this);
    }

    //************************************************************************************
    @Override
    public void onPermission(@NonNull String permission, @NonNull Permission.Status status)
    //************************************************************************************
    {
        switch (status) {
            case Granted:
                initControls();
                break;
            case DeniedForNow:
            case DeniedPermanently:
                initPermission();
                break;
        }
    }

    //*************************************************************
    @Override
    public void onPermissionDenied(@NonNull String permission)
    //*************************************************************
    {
        AndroidUtil.toast(false, "" + permission);
    }

    //**********************************************************
    @Override
    public void onUpdateSuccessfully(@Nullable ProfileResponse response)
    //**********************************************************
    {
        Laila.instance().getMUser_U().getData().setProfile(response.getData().getProfile());
        SharedPreferencesUtils.setValue(Constants.USER_DATA, Laila.instance().getMUser_U());
    }

    //**********************************************************
    @Override
    public void onUpdateFailed(@NonNull String errorMessage)
    //**********************************************************
    {

    }
}