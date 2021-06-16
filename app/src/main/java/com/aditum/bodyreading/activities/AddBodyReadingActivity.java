package com.aditum.bodyreading.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.aditum.Laila;
import com.aditum.R;
import com.aditum.activities.BaseActivity;
 import com.aditum.bodyreading.viewmodels.AddHealthDataViewModel;
import com.aditum.databinding.ActivityAddBodyReadingBinding;
import com.aditum.models.updates.response_models.AddHealthDataResponse;
import com.aditum.utils.AndroidUtil;
import com.aditum.utils.DateUtils;
import com.aditum.utils.UIUtils;

import lombok.val;

//***************************************************************
public class AddBodyReadingActivity extends BaseActivity
        implements AddHealthDataViewModel.AddHealthDataListener
//***************************************************************
{
    private ActivityAddBodyReadingBinding mBinding;
    private String mHeartRate, mGlucoseLevel, mSystolic, mDiastolic;
    private AddHealthDataViewModel mAddHealthDataViewModel;

    //***************************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //***************************************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_body_reading);
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
        mAddHealthDataViewModel = new AddHealthDataViewModel(this);
        mBinding.selectionMethod.setVisibility(View.VISIBLE);
        mBinding.addRecordFromDevice.setVisibility(View.GONE);
        mBinding.addHeartRate.setVisibility(View.GONE);
        mBinding.addBloodPressure.setVisibility(View.GONE);
        mBinding.addGlucoseLevel.setVisibility(View.GONE);
        mBinding.userInfo.setVisibility(View.GONE);
        mBinding.personalSummary.setVisibility(View.GONE);
        mBinding.recordAddedSuccessfully.setVisibility(View.GONE);
        mBinding.addManual.setOnClickListener(v -> manual());
        mBinding.heartRateNextBtn.setOnClickListener(v -> heartRate());
        mBinding.bloodPressureNextBtn.setOnClickListener(v -> bloodPressure());
        mBinding.glucoseLevelNextBtn.setOnClickListener(v -> glucoseLevel());
        mBinding.heartRatePriBtn.setOnClickListener(v -> initControl());
        mBinding.bloodPressurePriBtn.setOnClickListener(v -> manual());
        mBinding.glucoseLevelPriBtn.setOnClickListener(v -> heartRate());
        mBinding.summaryPriBtn.setOnClickListener(v -> weight());
        mBinding.addRecord.setOnClickListener(v -> addRecord());
    }

    //*******************************************
    private void hideKeyBoard()
    //*******************************************
    {
        ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                .toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    //*******************************************
    private void addDevice()
    //*******************************************
    {
        mBinding.addRecordFromDevice.setVisibility(View.VISIBLE);
        mBinding.selectionMethod.setVisibility(View.GONE);
        mBinding.addHeartRate.setVisibility(View.GONE);
        mBinding.addBloodPressure.setVisibility(View.GONE);
        mBinding.addGlucoseLevel.setVisibility(View.GONE);
        mBinding.personalSummary.setVisibility(View.GONE);
    }

    //*******************************************
    private void manual()
    //*******************************************
    {
        mBinding.addRecordFromDevice.setVisibility(View.GONE);
        mBinding.selectionMethod.setVisibility(View.GONE);
        mBinding.userInfo.setVisibility(View.VISIBLE);
        mBinding.addHeartRate.setVisibility(View.VISIBLE);
        mBinding.addBloodPressure.setVisibility(View.GONE);
        mBinding.addGlucoseLevel.setVisibility(View.GONE);
        mBinding.personalSummary.setVisibility(View.GONE);
    }

    //*******************************************
    private void heartRate()
    //*******************************************
    {
        hideKeyBoard();
        mHeartRate = mBinding.heartRate.getText().toString();
        mBinding.addRecordFromDevice.setVisibility(View.GONE);
        mBinding.selectionMethod.setVisibility(View.GONE);
        mBinding.addHeartRate.setVisibility(View.GONE);
        mBinding.addBloodPressure.setVisibility(View.VISIBLE);
        mBinding.addGlucoseLevel.setVisibility(View.GONE);
        mBinding.personalSummary.setVisibility(View.GONE);
    }

    //*******************************************
    private void bloodPressure()
    //*******************************************
    {
        mSystolic = mBinding.systolic.getText().toString();
        mDiastolic = mBinding.diastolic.getText().toString();
        mBinding.addRecordFromDevice.setVisibility(View.GONE);
        mBinding.selectionMethod.setVisibility(View.GONE);
        mBinding.addHeartRate.setVisibility(View.GONE);
        mBinding.addBloodPressure.setVisibility(View.GONE);
        mBinding.addGlucoseLevel.setVisibility(View.VISIBLE);
        mBinding.personalSummary.setVisibility(View.GONE);

    }

    //*******************************************
    private void glucoseLevel()
    //*******************************************
    {
        mGlucoseLevel = mBinding.glucoseLevel.getText().toString();
        mBinding.addRecordFromDevice.setVisibility(View.GONE);
        mBinding.selectionMethod.setVisibility(View.GONE);
        mBinding.addHeartRate.setVisibility(View.GONE);
        mBinding.addBloodPressure.setVisibility(View.GONE);
        mBinding.addGlucoseLevel.setVisibility(View.GONE);
        mBinding.personalSummary.setVisibility(View.VISIBLE);
        summary();
    }

    //*******************************************
    private void weight()
    //*******************************************
    {
        mBinding.addRecordFromDevice.setVisibility(View.GONE);
        mBinding.selectionMethod.setVisibility(View.GONE);
        mBinding.addHeartRate.setVisibility(View.GONE);
        mBinding.addBloodPressure.setVisibility(View.GONE);
        mBinding.addGlucoseLevel.setVisibility(View.VISIBLE);
        mBinding.personalSummary.setVisibility(View.GONE);
    }

    //*******************************************
    private void summary()
    //*******************************************
    {
        val systolicValue = TextUtils.isEmpty(mSystolic) ? "-- " : mSystolic;
        val diastolicValue = TextUtils.isEmpty(mDiastolic) ? "-- " : mDiastolic;
        val hearRateValue = TextUtils.isEmpty(mHeartRate) ? "-- " : mHeartRate;
        val sugarValue = TextUtils.isEmpty(mGlucoseLevel) ? "-- " : mGlucoseLevel;

        mBinding.dateTime.setText(DateUtils.getCurrentDate("MMM dd, yyyy") + " at " + UIUtils.getCurrentTime().toUpperCase());
        mBinding.yourName.setText(AndroidUtil.getString(R.string.personal_body_reading));
        mBinding.yourBloodPressure.setText(AndroidUtil.getString(R.string.systolic) + " " + systolicValue + AndroidUtil.getString(R.string.blood_pressure_unit) + "\n" + AndroidUtil.getString(R.string.diastolic) + " " + diastolicValue + AndroidUtil.getString(R.string.blood_pressure_unit));
        mBinding.yourHeartRate.setText(hearRateValue + AndroidUtil.getString(R.string.heart_rate_unit));
        mBinding.yourSugar.setText(sugarValue + AndroidUtil.getString(R.string.sugar_unit));

        mBinding.addRecord.setEnabled(TextUtils.isEmpty(mSystolic) && TextUtils.isEmpty(mDiastolic) && TextUtils.isEmpty(mHeartRate) && TextUtils.isEmpty(mGlucoseLevel) ?
                false : true);

        val addRecordBtn = mBinding.addRecord.isEnabled();

        mBinding.addRecord.setBackgroundTintList(
                ContextCompat.getColorStateList(this, addRecordBtn ? R.color.darkBlue : R.color.secondaryTextColor));

    }

    //*******************************************
    private void addRecord()
    //*******************************************
    {
        val user = Laila.instance().getMUser_U();
        val userProfile = Laila.instance().getMUser_U().getData().getUser();
        val user_token = userProfile.getToken();
        val user_id = userProfile.getId().toString();
        if (user == null || userProfile == null)
            return;

        val addHealthDataRequest = Laila.instance()
                .getMAddHealthDataRequest_U().Builder();
        addHealthDataRequest.setUserId(user_id);
        addHealthDataRequest.setToken(user_token);
        addHealthDataRequest.setHeartRate(TextUtils.isEmpty(mHeartRate) ? "-1" : mHeartRate);
        addHealthDataRequest.setBpSystolic(TextUtils.isEmpty(mSystolic) || TextUtils.isEmpty(mDiastolic) ? "-1" : mSystolic);
        addHealthDataRequest.setBpDiastolic(TextUtils.isEmpty(mSystolic) || TextUtils.isEmpty(mDiastolic) ? "-1" : mDiastolic);
        addHealthDataRequest.setBloodSugar(TextUtils.isEmpty(mGlucoseLevel) ? "-1" : mGlucoseLevel);

        showLoadingDialog();
        mAddHealthDataViewModel.addHealthData(addHealthDataRequest);
    }

    //**************************************************
    @Override
    public void onAddHealthDataSuccessfully(@Nullable AddHealthDataResponse response)
    //**************************************************
    {
        hideLoadingDialog();
        Laila.instance().Change_Data = true;

        AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.body_reading_added),
                AndroidUtil.getString(R.string.success),
                this,
                AndroidUtil.getString(R.string.ok),
                (dialog, which) ->
                {
                    if (which == -1) {
                        goToHome();
                    }
                });
    }

    //**************************************************
    private void goToHome()
    //**************************************************
    {
        Intent homeIntent = new Intent(this, BodyReadingActivity.class);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
        finish();
    }

    //**************************************************
    @Override
    public void onAddHealthDataFailed(@NonNull String errorMessage)
    //**************************************************
    {
        hideLoadingDialog();
        AndroidUtil.displayAlertDialog(errorMessage, AndroidUtil.getString(R.string.error), this);
    }

    //***************************************************************
    @Override
    protected boolean showStatusBar()
    //***************************************************************
    {
        return false;
    }
}