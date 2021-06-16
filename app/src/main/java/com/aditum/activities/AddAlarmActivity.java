package com.aditum.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.aditum.Laila;
import com.aditum.R;
import com.aditum.databinding.ActivityAddAlarmBinding;
import com.aditum.models.updates.models.Event;
import com.aditum.models.updates.models.ResponseEvent;
import com.aditum.models.updates.response_models.AddEventResponse;
import com.aditum.models.updates.response_models.GetEventsResponse;
import com.aditum.utils.AndroidUtil;
import com.aditum.utils.Constants;
import com.aditum.utils.DateUtils;
import com.aditum.utils.SharedPreferencesUtils;
import com.aditum.view_models.MedicineEventViewModel;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.val;

//********************************************************
public class AddAlarmActivity extends BaseActivity
        implements MedicineEventViewModel.MedicineEventCompleteListener
//********************************************************
{
    private ActivityAddAlarmBinding mBinding;
    private Date mStartData;
    private Date mEndData;
    private Date mTime;
    private MedicineEventViewModel mEventViewModel;

    //********************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //********************************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_alarm);
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(AndroidUtil.getString(R.string.alarm));
        initControl();
    }

    //***********************************************************
    private void initControl()
    //***********************************************************
    {
        mEventViewModel = new MedicineEventViewModel(this);
        mBinding.startDate.setOnClickListener(v ->
        {
            datePicker(mBinding.setStartDate);
        });
        mBinding.endDate.setOnClickListener(v ->
        {
            datePicker(mBinding.setEndDate);
        });
        mBinding.time.setOnClickListener(v ->
        {
            timePicker(mBinding.setTime);
        });
        mBinding.saveBtn.setOnClickListener(v ->
        {
            setAlarm();
        });
        setCurrentDateAndTime();

    }

    //***********************************************************
    private void setCurrentDateAndTime()
    //***********************************************************
    {
        val date = DateUtils.getCurrentDate("dd-MM-yyyy");
        val time = DateUtils.getCurrentTime("hh:mm a").toUpperCase();
        mBinding.setStartDate.setText(date);
        mBinding.setEndDate.setText(date);
        mBinding.setTime.setText(time);
    }

    //***********************************************************
    @Override
    protected boolean showStatusBar()
    //***********************************************************
    {
        return false;
    }

    //*******************************************
    private void setAlarm()
    //*******************************************
    {
        if (mStartData == null)
            mStartData = new Date();
        if (mEndData == null)
            mEndData = new Date();

        mStartData = mStartData == null ? new Date() : mStartData;
        mEndData = mEndData == null ? new Date() : mEndData;

        val reminderTitle = mBinding.reminderTitle.getText()
                .toString();
        val startDate = mBinding.setStartDate.getText().toString();
        val endDate = mBinding.setEndDate.getText().toString();
        val time = mBinding.setTime.getText().toString();

        if (TextUtils.isEmpty(reminderTitle)) {
            mBinding.reminderTitle.setError(AndroidUtil.getString(R.string.required));
            mBinding.reminderTitle.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(startDate)) {
            mBinding.setStartDate.setError(AndroidUtil.getString(R.string.required));
            mBinding.setStartDate.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(endDate)) {
            mBinding.setEndDate.setError(AndroidUtil.getString(R.string.required));
            mBinding.setEndDate.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(time)) {
            mBinding.setTime.setError(AndroidUtil.getString(R.string.required));
            mBinding.setTime.requestFocus();
            return;
        }
        if (mEndData.getTime() < mStartData.getTime()) {
            AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.start_date_less_then_end_date), AndroidUtil.getString(R.string.alert), this);
            return;
        }

        val eventRequest = Laila.instance().getMAddEventRequest().Builder();

        List<Event> eventsList = new ArrayList<>();
        Event event = new Event();
        event = new Event();

        event.setType("Local Alarm");
        event.setEventTitle(reminderTitle);
        event.setMedicationId("");
        event.setContactId("1");
        event.setDeliveryType("1");
        event.setStartDate(startDate + " 8:00AM");
        event.setEndDate(endDate + " 11:59PM");
        event.setFrequency(1);
        event.setTimeSchedule(time);
        eventsList.add(event);

        val userId = Laila.instance().getMUser_U().getData().getUser().getId().toString();
        val token = Laila.instance().getMUser_U().getData().getUser().getToken();
        eventRequest.setUserId(Integer.parseInt(userId));
        eventRequest.setToken(token);
        eventRequest.setEvents(eventsList);

        showLoadingDialog();
        mEventViewModel.medicineEvent(eventRequest);
    }

    //*******************************************
    private void datePicker(TextView view)
    //*******************************************
    {
        Date dialogDate;
        if (view == mBinding.setStartDate) {
            dialogDate = mStartData == null ? new Date() : mStartData;
        } else {
            if (mStartData == null) {
                AndroidUtil.toast(false, "Select start date first");
                return;
            }

            dialogDate = mStartData;
        }

        new SingleDateAndTimePickerDialog.Builder(this)
                .bottomSheet()
                .minDateRange(dialogDate)
                .curved()
                .displayMinutes(false)
                .displayHours(false)
                .displayDays(false)
                .displayMonth(true)
                .displayYears(true)
                .displayDaysOfMonth(true)
                .listener(date ->
                {
                    if (view == mBinding.setStartDate) {
                        mStartData = date;
                        mBinding.setEndDate.setText("");
                        mEndData = null;
                    } else
                        mEndData = date;

                    view.setText(
                            DateUtils.getDate(date.getTime(),
                                    "dd-MMM-yyyy"));
                })
                .display();
    }

    //*******************************************
    private void timePicker(TextView time)
    //*******************************************
    {
        new SingleDateAndTimePickerDialog.Builder(this)
                .bottomSheet()
                .curved()
                .displayMinutes(true)
                .displayHours(true)
                .displayDays(false)
                .displayMonth(false)
                .displayYears(false)
                .displayDaysOfMonth(false)
                .listener(date ->
                {
                    mTime = date;
                    time.setText(
                            DateUtils.getDate(mTime.getTime(),
                                    "hh:mm a").toUpperCase());
                })
                .display();
    }


    @Override
    public void onSuccessfullyAddEvent(@Nullable AddEventResponse response) {

        if (response.getData().getResponseEvents() != null) {
            Laila.instance().addMedicineAlarm(response.getData().getResponseEvents(), 0);
            if (Laila.instance().getMUser_U().getData().getResponseEvents() == null)
                Laila.instance().getMUser_U().getData().setResponseEvents(new ArrayList<>());

            val responseEvents = response.getData().getResponseEvents();
            for (ResponseEvent event : responseEvents)
                Laila.instance().getMUser_U().getData().getResponseEvents().add(event);
            SharedPreferencesUtils.setValue(Constants.USER_DATA, Laila.instance().getMUser_U());
        }
        hideLoadingDialog();

        AndroidUtil.displayAlertDialog(
                "Reminder Add Successfully",
                AndroidUtil.getString(R.string.success),
                this,
                AndroidUtil.getString(
                        R.string.ok),
                (dialog, which) -> {
                    if (which == -1) {
                        onBackPressed();
                    }
                });
    }

    @Override
    public void onSuccessfullyGetEvents(@Nullable GetEventsResponse response) {

    }

    @Override
    public void onFailedEvents(@NonNull String errorMessage) {
        hideLoadingDialog();
        AndroidUtil.displayAlertDialog(errorMessage, AndroidUtil.getString(R.string.error), this);
    }
}