package com.fantechlabs.lailaa.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;

import com.applandeo.materialcalendarview.EventDay;
import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.adapter.AlarmsAdapter;
import com.fantechlabs.lailaa.databinding.ActivityCalenderBinding;
import com.fantechlabs.lailaa.databinding.ActivityMedicationBinding;
import com.fantechlabs.lailaa.models.Events;
import com.fantechlabs.lailaa.models.Medication;
import com.fantechlabs.lailaa.models.response_models.RefillRemindersResponse;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;
import com.fantechlabs.lailaa.utils.DateUtils;
import com.fantechlabs.lailaa.utils.SharedPreferencesUtils;
import com.fantechlabs.lailaa.utils.UIUtils;
import com.fantechlabs.lailaa.view_models.DeleteEventViewModel;
import com.fantechlabs.lailaa.view_models.RefillRemindersViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lombok.val;

//******************************************************************
public class CalenderActivity extends BaseActivity
        implements DeleteEventViewModel.DeleteEventListener,
        RefillRemindersViewModel.RefillRemindersViewModelListener
//******************************************************************
{
    private ActivityCalenderBinding mBinding;
    public List<EventDay> mCalenderEvent = new ArrayList<>();
    private List<Events> mTodayEvents = new ArrayList<>();
    private String mStartDate, mEndDate;
    private int mEventId, mPosition;
    private DeleteEventViewModel mDeleteEventViewModel;
    private List<Medication> mMedicationList;
    private RefillRemindersViewModel mRefillRemindersViewModel;

    //******************************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //******************************************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_calender);
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("");
        initControl();
    }

    //*********************************************************
    @Override
    protected void onResume()
    //*********************************************************
    {
        super.onResume();
        mRefillRemindersViewModel = new RefillRemindersViewModel(this);
        getRefillReminders();
    }

    //*********************************************************
    private void showAlarmOnRecyclerView(List<Events> events)
    //*********************************************************
    {
        AlarmsAdapter alarmsAdapter = new AlarmsAdapter();
        if (events == null || events.size() == 0) {
            mBinding.alarm.setAdapter(null);
            return;
        }
        alarmsAdapter.setAlarms(events, mMedicationList, new AlarmsAdapter.ListClickListener() {
                    @Override
                    public void onDelete(int id) {
                        UIUtils.displayAlertDialog(
                                AndroidUtil.getString(R.string.delete_item),
                                AndroidUtil.getString(R.string.alert),
                                CalenderActivity.this,
                                AndroidUtil.getString(
                                        R.string.ok),
                                AndroidUtil.getString(
                                        R.string.cancel),
                                (dialog, which) -> {
                                    if (which == -1) {
                                        val events1 = Laila.instance().getMUser().getEvents();
                                        if (events1 == null || events1.size() == 0)
                                            return;
                                        for (int i = 0; i < events1.size(); i++) {
                                            if (events1.get(i).getId().equals(id)) {
                                                mEventId = id;
                                                mPosition = i;
                                            }
                                        }
                                        CalenderActivity.this.showLoadingDialog();
                                        mDeleteEventViewModel.deleteEvent(mEventId);
                                    }
                                });

                    }
                }, this
        );
        alarmsAdapter.notifyDataSetChanged();
        mBinding.alarm.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mBinding.alarm.setAdapter(alarmsAdapter);
    }

    //***********************************************************
    private void addMedicineOnCalender()
    //***********************************************************
    {
        if (Laila.instance().getMUser() == null || Laila.instance().getMUser().getEvents() == null)
            return;
        val alarms = Laila.instance().getMUser().getEvents();

        if (mTodayEvents.size() == 0) {
            mBinding.calendarView.setEvents(null);
            mCalenderEvent.clear();
        }

        for (val alarm : alarms) {
            Calendar calendar = Calendar.getInstance();

            splitDateTime(alarm.getStartDate(), alarm.getEndDate());
            val days = DateUtils.getNumberOfDays(mStartDate, mEndDate);

            calendar.setTimeInMillis(DateUtils.getDateFromStringFormat(mStartDate, "dd-MMM-yyyy").getTime());

            for (int i = 0; i <= days; i++) {
                Calendar finalCalender = Calendar.getInstance();
                finalCalender.setTimeInMillis(calendar.getTimeInMillis());
                mCalenderEvent.add(
                        new EventDay(finalCalender, R.drawable.record));
                calendar.add(Calendar.HOUR, 24);
            }
        }
        mBinding.calendarView.setEvents(mCalenderEvent);
    }

    //****************************************************************
    @Override
    protected boolean showStatusBar()
    //****************************************************************
    {
        return false;
    }

    //****************************************
    private void initControl()
    //****************************************
    {
        mDeleteEventViewModel = new DeleteEventViewModel(this);
        mBinding.addBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, AddAlarmActivity.class));
        });
        mBinding.calendarView.setOnDayClickListener(eventDay -> CalenderActivity.this.filterCalenderRecord(eventDay));
    }

    //***********************************************************
    private void getRefillReminders()
    //***********************************************************
    {
        showLoadingDialog();
        if (Laila.instance().getMUser() == null || Laila.instance().getMUser().getProfile() == null)
            return;
        showLoadingDialog();
        mRefillRemindersViewModel.getRefillReminders(Laila.instance().getMUser().getProfile().getUserPrivateCode());
    }

    //***********************************************************
    private void filterCalenderRecord(EventDay eventDay)
    //***********************************************************
    {

        Calendar clickedDayCalendar;
        clickedDayCalendar = eventDay == null ? Calendar.getInstance() : eventDay.getCalendar();

        getSupportActionBar().setTitle("" + DateUtils.getDate(clickedDayCalendar.getTime().getTime(), "dd-MMM-yyyy"));
        clickedDayCalendar.set(Calendar.HOUR_OF_DAY, 0);
        clickedDayCalendar.set(Calendar.MINUTE, 0);
        clickedDayCalendar.set(Calendar.SECOND, 0);
        clickedDayCalendar.set(Calendar.HOUR_OF_DAY, 23);
        clickedDayCalendar.set(Calendar.MINUTE, 59);
        clickedDayCalendar.set(Calendar.SECOND, 0);

        val events = Laila.instance().getMUser().getEvents();
        val user = Laila.instance().getMUser();

        if (user == null) {
            if (events == null) {
                mBinding.alarm.setAdapter(null);
            }
            return;
        }
        if (events == null || events.size() == 0) {
            mBinding.alarm.setAdapter(null);
            return;
        }

        mTodayEvents = new ArrayList<>();

        for (Events event : events) {
            val startDate = DateUtils.getDateFromString(event.getStartDate(), "dd-MMM-yyyy hh:mma");
            val endDate = DateUtils.addOneDayToDate(event.getEndDate(), "dd-MMM-yyyy hh:mma");
            if (clickedDayCalendar.getTime().before(endDate) && clickedDayCalendar.getTime().after(startDate))
                mTodayEvents.add(event);
        }

        showAlarmOnRecyclerView(mTodayEvents);
    }

    //***********************************************************
    private void splitDateTime(@NonNull String startDateTime, @NonNull String endDateTime)
    //***********************************************************
    {
        String[] startDateArray = startDateTime.split(" ");
        if (TextUtils.isEmpty(startDateArray[0]))
            return;
        mStartDate = startDateArray[0];
        String[] endDateArray = endDateTime.split(" ");
        if (TextUtils.isEmpty(endDateArray[0]))
            return;
        mEndDate = endDateArray[0];

    }

    //*************************************************************
    @Override
    public void onSuccessfullyDeleteEvent(@Nullable String result)
    //*************************************************************
    {

        val events1 = Laila.instance().getMUser().getEvents();

        if (events1 == null || events1.size() == 0)
            return;
        Laila.instance().getMUser().getEvents().remove(mPosition);
        mTodayEvents.remove(mPosition);

        val user = Laila.instance().getMUser();
        SharedPreferencesUtils.setValue(Constants.USER_DATA, user);

        filterCalenderRecord(null);
        addMedicineOnCalender();
        hideLoadingDialog();
        AndroidUtil.displayAlertDialog(result, AndroidUtil.getString(R.string.success_delete_alarm), this);
    }

    //*************************************************************
    @Override
    public void onFailedToDeleteEvent(@NonNull String error)
    //*************************************************************
    {
        hideLoadingDialog();
        AndroidUtil.displayAlertDialog(error, AndroidUtil.getString(R.string.error), this);
    }

    //*************************************************************
    @Override
    public void onSuccessGetRefillReminders(@Nullable RefillRemindersResponse response)
    //*************************************************************
    {
        hideLoadingDialog();
        val user = Laila.instance().getMUser();
        if (user != null && user.getMedication() != null) {
            mMedicationList = new ArrayList<>();
            if (response.getIds() != null || response.getIds().size() != 0) {
                val ids = response.getIds();
                val medicineList = user.getMedication();

                for (Integer id : ids) {
                    for (Medication medicine : medicineList) {
                        if (medicine.getId() == id && medicine.getPrescribed() == 1)
                            mMedicationList.add(medicine);
                    }
                }
            }
        }
        setCalenderEvents();
    }

    //*************************************************************
    private void setCalenderEvents()
    //*************************************************************
    {
        filterCalenderRecord(null);
        addMedicineOnCalender();
    }

    //*************************************************************
    @Override
    public void onFailedGetRefillReminders(@lombok.NonNull String message)
    //*************************************************************
    {
        hideLoadingDialog();
        setCalenderEvents();
        AndroidUtil.displayAlertDialog(message, AndroidUtil.getString(R.string.error), this);
    }

}