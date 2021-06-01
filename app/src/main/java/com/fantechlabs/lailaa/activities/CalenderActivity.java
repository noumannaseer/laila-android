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
import com.fantechlabs.lailaa.models.response_models.RefillRemindersResponse;
import com.fantechlabs.lailaa.models.updates.models.Medication;
import com.fantechlabs.lailaa.models.updates.models.ResponseEvent;
import com.fantechlabs.lailaa.models.updates.response_models.AddEventResponse;
import com.fantechlabs.lailaa.models.updates.response_models.GetEventsResponse;
import com.fantechlabs.lailaa.models.updates.response_models.RefillReminderResponse;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;
import com.fantechlabs.lailaa.utils.DateUtils;
import com.fantechlabs.lailaa.utils.SharedPreferencesUtils;
import com.fantechlabs.lailaa.utils.UIUtils;
import com.fantechlabs.lailaa.view_models.DeleteEventViewModel;
import com.fantechlabs.lailaa.view_models.MedicineEventViewModel;
import com.fantechlabs.lailaa.view_models.RefillRemindersViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lombok.val;

//******************************************************************
public class CalenderActivity extends BaseActivity
        implements DeleteEventViewModel.DeleteEventListener,
        RefillRemindersViewModel.RefillRemindersViewModelListener,
        MedicineEventViewModel.MedicineEventCompleteListener
//******************************************************************
{
    private ActivityCalenderBinding mBinding;
    public List<EventDay> mCalenderEvent = new ArrayList<>();
    private List<ResponseEvent> mTodayEvents = new ArrayList<>();
    private String mStartDate, mEndDate;
    private int mEventId, mPosition;
    private DeleteEventViewModel mDeleteEventViewModel;
    private MedicineEventViewModel mMedicineEventViewModel;
    private List<Medication> mMedicationList;
    private RefillRemindersViewModel mRefillRemindersViewModel;
    private List<ResponseEvent> mResponseEvents;


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
        mMedicineEventViewModel = new MedicineEventViewModel(this);
        getEvents();
        getRefillReminders();
    }

    //*********************************************************
    private void getEvents()
    //*********************************************************
    {
        showLoadingDialog();
        mMedicineEventViewModel.getEvents();
    }

    //*********************************************************
    private void showAlarmOnRecyclerView(List<ResponseEvent> events)
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
//                                        val events1 = RXCare.instance().getMUser_U().getData().getResponseEvents();
                                        if (mResponseEvents == null || mResponseEvents.size() == 0)
                                            return;
                                        for (int i = 0; i < mResponseEvents.size(); i++) {
                                            if (mResponseEvents.get(i).getId().equals(id)) {
                                                mEventId = id;
                                                mPosition = i;
                                            }
                                        }
                                        showLoadingDialog();
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
        if (Laila.instance().getMUser_U() == null || mResponseEvents == null)
            return;
//        val alarms = RXCare.instance().getMUser_U().getData().getResponseEvents();

        if (mTodayEvents.size() == 0) {
            mBinding.calendarView.setEvents(null);
            mCalenderEvent.clear();
        }

        for (val alarm : mResponseEvents) {
            Calendar calendar = Calendar.getInstance();

            val start = DateUtils.getDateFromTimeStamp(alarm.getStartDate(), "dd-MMM-yyyy");
            val end = DateUtils.getDateFromTimeStamp(alarm.getEndDate(), "dd-MMM-yyyy");

            splitDateTime(start, end);
            val days = DateUtils.getNumberOfDays(mStartDate, mEndDate);
            calendar.setTimeInMillis(alarm.getStartDate() * 1000);

            for (int i = 0; i <= days - 1; i++) {
                Calendar finalCalender = Calendar.getInstance();
                finalCalender.setTimeInMillis(calendar.getTimeInMillis());
                mCalenderEvent.add(
                        new EventDay(finalCalender, R.drawable.record, Color.parseColor("#228B22")));
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
        mResponseEvents = new ArrayList<>();
        mDeleteEventViewModel = new DeleteEventViewModel(this);
        mBinding.addBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, AddAlarmActivity.class));
        });
        mBinding.calendarView.setOnDayClickListener(eventDay -> filterCalenderRecord(eventDay));
    }

    //***********************************************************
    private void getRefillReminders()
    //***********************************************************
    {
        showLoadingDialog();
        if (Laila.instance().getMUser_U() == null)
            return;
        showLoadingDialog();
        mRefillRemindersViewModel.getRefillReminders();
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

//        val events = RXCare.instance().getMUser_U().getData().getResponseEvents();
        val user = Laila.instance().getMUser_U();

        if (user == null) {
            if (mResponseEvents == null) {
                mBinding.alarm.setAdapter(null);
            }
            return;
        }
        if (mResponseEvents == null || mResponseEvents.size() == 0) {
            mBinding.alarm.setAdapter(null);
            return;
        }

        mTodayEvents = new ArrayList<>();

        for (ResponseEvent event : mResponseEvents) {
            val startDate = new Date(event.getStartDate() * 1000);
            val endDate = new Date(event.getEndDate() * 1000);
            val clickedDate = clickedDayCalendar.getTime();
            if (clickedDate.before(endDate) && clickedDate.after(startDate))
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
        getEvents();
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
    public void onSuccessGetRefillReminders(@Nullable RefillReminderResponse response)
    //*************************************************************
    {
        hideLoadingDialog();
        val user = Laila.instance().getMUser_U();
        if (user != null && user.getData().getMedicationList() != null) {
            mMedicationList = new ArrayList<>();
            if (response.getData().getIds() != null) {
                val ids = response.getData().getIds();
                val medicineList = user.getData().getMedicationList();

                for (Integer id : ids) {
                    for (Medication medicine : medicineList) {
                        if (medicine.getId().equals(id) && medicine.getPrescribed() == 1)
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

    //*************************************************************
    @Override
    public void onSuccessfullyAddEvent(@Nullable AddEventResponse response)
    //*************************************************************
    {

    }

    //*************************************************************
    @Override
    public void onSuccessfullyGetEvents(@Nullable GetEventsResponse response)
    //*************************************************************
    {
        hideLoadingDialog();
        if (response.getData().getEventsList() == null)
            return;
        mResponseEvents = response.getData().getEventsList();
        Laila.instance().getMUser_U().getData().setEventsList(mResponseEvents);
        SharedPreferencesUtils.setValue(Constants.USER_DATA, Laila.instance().getMUser_U());
        setCalenderEvents();
    }

    //*************************************************************
    @Override
    public void onFailedEvents(@NonNull String errorMessage)
    //*************************************************************
    {
        hideLoadingDialog();
    }

}