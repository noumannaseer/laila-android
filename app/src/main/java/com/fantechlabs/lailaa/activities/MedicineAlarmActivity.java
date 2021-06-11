package com.fantechlabs.lailaa.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.adapter.NotificationListAdapter;
import com.fantechlabs.lailaa.databinding.ActivityMedicineAlarmBinding;
import com.fantechlabs.lailaa.models.updates.models.ResponseEvent;
import com.fantechlabs.lailaa.models.updates.response_models.AddEventResponse;
import com.fantechlabs.lailaa.models.updates.response_models.GetEventsResponse;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;
import com.fantechlabs.lailaa.utils.SharedPreferencesUtils;
import com.fantechlabs.lailaa.view_models.MedicineEventViewModel;

import java.util.ArrayList;
import java.util.List;

//*********************************************************
public class MedicineAlarmActivity
        extends BaseActivity
        implements
        MedicineEventViewModel.MedicineEventCompleteListener
//*********************************************************
{

    private ActivityMedicineAlarmBinding mBinding;
    private MedicineEventViewModel mMedicineEventViewModel;
    private List<ResponseEvent> mResponseEvents;

    //*********************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //*********************************************************
    {
        mBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_medicine_alarm);
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("");
        initControls();
    }

    //*********************************************************
    @Override
    protected boolean showStatusBar()
    //*********************************************************
    {
        return false;
    }

    //*********************************************************
    private void initControls()
    //*********************************************************
    {
        mResponseEvents = new ArrayList<>();
        mMedicineEventViewModel = new MedicineEventViewModel(this);
        getEvents();

    }

    //*********************************************************
    private void getEvents()
    //*********************************************************
    {
        showLoadingDialog();
        mMedicineEventViewModel.getEvents();
    }

    //*********************************************************
    private void showNotificationRecyclerView()
    //*********************************************************
    {
        if (mResponseEvents == null || mResponseEvents.size() == 0)
            return;
        NotificationListAdapter notificationListAdapter = new NotificationListAdapter(mResponseEvents, this);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mBinding.recyclerView.setAdapter(notificationListAdapter);
    }

    @Override
    public void onSuccessfullyAddEvent(@Nullable AddEventResponse response) {

    }

    @Override
    public void onSuccessfullyGetEvents(@Nullable GetEventsResponse response) {
        hideLoadingDialog();
        if (response.getData().getEventsList() == null)
            return;
        mResponseEvents = response.getData().getEventsList();
        Laila.instance().getMUser_U().getData().setEventsList(mResponseEvents);
        SharedPreferencesUtils.setValue(Constants.USER_DATA, Laila.instance().getMUser_U());
        showNotificationRecyclerView();
    }

    @Override
    public void onFailedEvents(@NonNull String errorMessage) {
        hideLoadingDialog();
        AndroidUtil.displayAlertDialog(errorMessage, AndroidUtil.getString(R.string.error), this);
    }
}