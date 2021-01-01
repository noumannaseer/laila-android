package com.fantechlabs.lailaa;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.fantechlabs.lailaa.alarms.Alarm;
import com.fantechlabs.lailaa.alarms.AlarmReceiver;
import com.fantechlabs.lailaa.alarms.DatabaseHelper;
import com.fantechlabs.lailaa.alarms.LoadAlarmsService;
import com.fantechlabs.lailaa.bodyreading.repository.storge.requestmodel.AddHealthDataRequest;
import com.fantechlabs.lailaa.bodyreading.repository.storge.requestmodel.ReadHealthDataRequest;
import com.fantechlabs.lailaa.models.Contact;
import com.fantechlabs.lailaa.models.Events;
import com.fantechlabs.lailaa.models.Medication;
import com.fantechlabs.lailaa.models.Profile;
import com.fantechlabs.lailaa.models.SearchMedicine;
import com.fantechlabs.lailaa.models.response_models.UserResponse;
import com.fantechlabs.lailaa.request_models.AddMedicationRequest;
import com.fantechlabs.lailaa.request_models.AddPharmacyRequest;
import com.fantechlabs.lailaa.request_models.FollowUpRequest;
import com.fantechlabs.lailaa.request_models.FollowUpUpdateRequest;
import com.fantechlabs.lailaa.request_models.ProfileRequest;
import com.fantechlabs.lailaa.request_models.UserRequest;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.AutoCompleteLoadingBar;
import com.fantechlabs.lailaa.utils.UIUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import lombok.Getter;
import lombok.Setter;
import lombok.val;

//**********************************************************
public class Laila extends Application
//**********************************************************
{
    private static Context mContext;
    public AutoCompleteLoadingBar mAutoCompleteLoadingBar;

    @Getter
    @Setter
    private UserResponse mUser;
    @Getter
    @Setter
    private UserRequest userRequest;
    @Getter
    @Setter
    private ProfileRequest mProfileRequest;
    @Getter
    @Setter
    private SearchMedicine mSearchMedicine;
    @Getter
    @Setter
    private AddMedicationRequest mAddMedicationRequest;
    @Getter
    @Setter
    public Medication mUpdateMedication;
    @Getter
    @Setter
    public Contact mUpdateContact;
    @Getter
    @Setter
    private FollowUpRequest mFollowUpRequest;
    @Getter
    @Setter
    private FollowUpUpdateRequest mFollowUpUpdateRequest;
    @Getter
    @Setter
    public String mSearchData;
    @Getter
    @Setter
    public String mContactType;

    public boolean Edit_Profile = false;
    public boolean on_update_medicine = false;
    public boolean on_update_contact = false;
    public boolean is_medicine_added = false;
    public boolean is_pharmacy_added = false;
    public boolean text_recognizer = false;
    public boolean Bar_code = false;

    public static final String CHANNEL_ID = "alarm_channel";
    @Getter
    @Setter
    public int mMedicationPosition;
    @Getter
    @Setter
    public int mContactPosition;
    @Getter
    @Setter
    public int mMedicationId;
    @Getter
    @Setter
    private AddPharmacyRequest mAddPharmacyRequest;

    // For BodyReading
    public boolean Change_Data = true;
    @Getter
    @Setter
    private ReadHealthDataRequest mReadHealthDataRequest;
    @Getter
    @Setter
    private AddHealthDataRequest mAddHealthDataRequest;

    //****************************************************************
    @Override
    public void onCreate()
    //****************************************************************
    {
        super.onCreate();
        mContext = getApplicationContext();
        AndroidUtil.setContext(mContext);
        mAutoCompleteLoadingBar = new AutoCompleteLoadingBar(mContext);
    }

    //****************************************************************
    public static Laila instance()
    //****************************************************************
    {
        return (Laila) AndroidUtil.getApplicationContext();
    }

    //****************************************************************
    public Profile getCurrentUserProfile()
    //****************************************************************
    {
        if (mUser == null)
            return null;
        if (mUser.getProfile() == null)
            return null;
        return mUser.getProfile();
    }

    //****************************************************************
    public void addMedicineAlarm(List<Events> events, int followUPId)
    //****************************************************************
    {
        if (getMUser().getMedication() == null)
            getMUser().setMedication(new ArrayList<>());
        for (val event : events) {
            String[] start = event.getStartDate().split(" ");
            String[] end = event.getEndDate().split(" ");

            val startDate = UIUtils.getDateFromString(start[0], "dd-MMM-yyyy");
            val endDate = UIUtils.getDateFromString(end[0], "dd-MMM-yyyy");
            long msDiff = endDate.getTime() - startDate.getTime();
            long daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(startDate.getTime());

            for (int i = 0; i < daysDiff + 1; i++) {
                LoadAlarmsService.launchLoadAlarmsService(this);

                final long id = DatabaseHelper.getInstance(this)
                        .addAlarm();
                val alarm = new Alarm(id);
                alarm.setDay(Alarm.FRI, true);
                alarm.setDay(Alarm.SAT, true);
                alarm.setDay(Alarm.SUN, true);
                alarm.setDay(Alarm.MON, true);
                alarm.setDay(Alarm.TUES, true);
                alarm.setDay(Alarm.WED, true);
                alarm.setDay(Alarm.THURS, true);
                val time = event.getTimeSchedule();
                val timeZoneArray = time.split(" ");

                val timeArray = timeZoneArray[0].split(":");
                int hour = Integer.parseInt(timeArray[0]);

                if (timeZoneArray[1].toLowerCase()
                        .equals("pm"))
                    calendar.set(Calendar.HOUR_OF_DAY, 12 + hour);
                else
                    calendar.set(calendar.HOUR_OF_DAY, hour);

                calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
                Date date = calendar.getTime();
                alarm.setTime(date.getTime());
                alarm.setLabel(event.getEventTitle());
                alarm.setFollowUpId(followUPId);
                if (!TextUtils.isEmpty(event.getMedicationId().toString()))
                    alarm.setMedicineId(event.getMedicationId().toString());
                DatabaseHelper.getInstance(getApplicationContext())
                        .updateAlarm(alarm);

                AlarmReceiver.setReminderAlarm(this, alarm);

                calendar.add(Calendar.HOUR, 24 * (i + 1));
            }
        }

        LoadAlarmsService.launchLoadAlarmsService(this);
    }
}
