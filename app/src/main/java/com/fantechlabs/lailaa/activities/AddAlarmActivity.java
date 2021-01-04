package com.fantechlabs.lailaa.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.databinding.ActivityAddAlarmBinding;
import com.fantechlabs.lailaa.models.Events;
import com.fantechlabs.lailaa.request_models.EventRequest;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;
import com.fantechlabs.lailaa.utils.DateUtils;
import com.fantechlabs.lailaa.utils.SharedPreferencesUtils;
import com.fantechlabs.lailaa.view_models.EventViewModel;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;

import java.util.ArrayList;
import java.util.Date;

import lombok.val;

//********************************************************
public class AddAlarmActivity extends BaseActivity
        implements EventViewModel.EventListener
//********************************************************
{
    private ActivityAddAlarmBinding mBinding;
    private Date mStartData;
    private Date mEndData;
    private Date mTime;
    private EventViewModel mEventViewModel;

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
        mEventViewModel = new EventViewModel(this);
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

        Events event = new Events();
        if (event == null)
            event = new Events();

        event.setUserPrivateCode(Laila.instance().getMUser().getProfile().getUserPrivateCode());
        event.setEventTitle(reminderTitle);
        event.setStartDate(startDate + " 8:00AM");
        event.setEndDate(endDate + " 11:59PM");
        event.setTimeSchedule(time);
        event.setType("Alarm");
        event.setAlarmType("alarm");
        event.setFrequency("1");

        EventRequest request = new EventRequest();
        request.setEvent(event);

        showLoadingDialog();
        mEventViewModel.addEvent(request);
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

    //************************************************************
    @Override
    public void onSuccessfullyAddEvent(@Nullable EventRequest response)
    //************************************************************
    {
        hideLoadingDialog();

        if (response != null) {
            ArrayList<Events> event = new ArrayList<>();
            event.add(response.getEvent());
            Laila.instance().addMedicineAlarm(event, 0);
            if (Laila.instance().getMUser().getEvents() == null)
                Laila.instance().getMUser().setEvents(new ArrayList<>());
            Laila.instance().getMUser().getEvents().add(response.getEvent());
            SharedPreferencesUtils.setValue(Constants.USER_DATA, Laila.instance().getMUser());
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

    //************************************************************
    @Override
    public void onFailedToAddEvent(@NonNull String errorMessage)
    //************************************************************
    {
        hideLoadingDialog();
        AndroidUtil.displayAlertDialog(errorMessage, AndroidUtil.getString(R.string.error), this);
    }
}