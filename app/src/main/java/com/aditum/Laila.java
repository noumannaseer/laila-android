package com.aditum;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.aditum.alarms.Alarm;
import com.aditum.alarms.AlarmReceiver;
import com.aditum.alarms.DatabaseHelper;
import com.aditum.alarms.LoadAlarmsService;
import com.aditum.bodyreading.repository.storge.requestmodel.AddHealthDataRequest;
import com.aditum.bodyreading.repository.storge.requestmodel.ReadHealthDataRequest;
import com.aditum.models.Profile;
import com.aditum.models.SearchMedicine;
import com.aditum.models.allergie_models.DocumentList;
import com.aditum.models.response_models.UserResponse;
import com.aditum.models.updates.models.Contact;
import com.aditum.models.updates.models.Medication;
import com.aditum.models.updates.models.ResponseEvent;
import com.aditum.models.updates.models.SearchMedication;
import com.aditum.models.updates.request_models.AddEventRequest;
import com.aditum.models.updates.request_models.AddMedicationRequest;
import com.aditum.models.updates.request_models.AddPharmacyRequest;
import com.aditum.models.updates.request_models.EmergencyContactRequest;
import com.aditum.models.updates.request_models.ProfileRequest;
import com.aditum.request_models.FollowUpRequest;
import com.aditum.request_models.FollowUpUpdateRequest;
import com.aditum.request_models.QuestionsRequest;
import com.aditum.request_models.UserRequest;
import com.aditum.utils.AndroidUtil;
import com.aditum.utils.AutoCompleteLoadingBar;
import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
    private List<DocumentList> mDocumentList;
    @Getter
    @Setter
    private AddEventRequest mAddEventRequest;
    @Getter
    @Setter
    private List<HashMap<String, String>> mDocumentListWithHashMap;
    @Getter
    @Setter
    private DocumentList mDocument;
    @Getter
    @Setter
    private UserResponse mUser;
    @Getter
    @Setter
    private com.aditum.models.updates.response_models.UserResponse mUser_U;
    @Getter
    @Setter
    private UserRequest userRequest;
    @Getter
    @Setter
    private ProfileRequest mProfileRequest;
    @Getter
    @Setter
    private com.aditum.models.updates.models.Profile mProfile;
    @Getter
    @Setter
    private SearchMedicine mSearchMedicine;
    @Getter
    @Setter
    private SearchMedication mSearchMedicine_U;
    @Getter
    @Setter
    private String mSearchMedicineString;
    @Getter
    @Setter
    private List<SearchMedication> mFilteredSearchMedications;

    @Getter
    @Setter
    private AddMedicationRequest mAddMedicationRequest;
    @Getter
    @Setter
    private AddMedicationRequest mAddMedicationRequestCopy;
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
    public boolean IS_Documents = false;
    //    public boolean Edit_Profile = false;
    public boolean is_edit_profile_fields = false;
    public boolean on_update_medicine = false;
    public boolean on_update_contact = false;
    public boolean is_medicine_added = false;
    public boolean is_pharmacy_added = false;
    public boolean from_update_events = false;
    public boolean text_recognizer = false;
    public boolean Bar_code = false;
    public boolean is_update_over_the_counter = false;
    public boolean is_manually_add_medicine = false;
    public boolean from_update_medication = false;
    public boolean is_get_profile = false;
    public boolean from_image_din = false;
    public boolean remind_me_later = false;
    public boolean from_pharmacy_added = false;
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
    private com.aditum.models.updates.request_models.ReadHealthDataRequest mReadHealthDataRequest_U;
    @Getter
    @Setter
    private AddHealthDataRequest mAddHealthDataRequest;
    @Getter
    @Setter
    private com.aditum.models.updates.request_models.AddHealthDataRequest mAddHealthDataRequest_U;
    private List<String> mTimeShedules = new ArrayList<>();
    public boolean from_third_screen = false;
    @Getter
    @Setter
    public Medication mUpdateMedicationClone;
    @Getter
    @Setter
    private EmergencyContactRequest mEmergencyContactRequest;
    @Getter
    @Setter
    private QuestionsRequest mQuestionsRequest;
    @Getter
    @Setter
    private List<String> mRequestedQuestionsList;

    //****************************************************************
    @Override
    public void onCreate()
    //****************************************************************
    {
        super.onCreate();
        mContext = getApplicationContext();
        AndroidUtil.setContext(mContext);
        mAutoCompleteLoadingBar = new AutoCompleteLoadingBar(mContext);
        FirebaseApp.initializeApp(mContext);
//        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

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
    public void addMedicineAlarm(List<ResponseEvent> events, int followUPId)
    //****************************************************************
    {
        if (mTimeShedules.size() > 0)
            mTimeShedules.clear();
        if (getMUser_U().getData().getMedicationList() == null)
            getMUser_U().getData().setMedicationList(new ArrayList<>());
        long startDate = 0;
        long endDate = 0;
        for (val event : events) {
            if (mTimeShedules.size() > 0) {
                if (mTimeShedules.contains(event.getTimeSchedule()))
                    continue;
            }
            mTimeShedules.add(event.getTimeSchedule());

            if (Laila.instance().remind_me_later) {
                startDate = event.getStartDate();
                endDate = event.getEndDate();
            } else {
                startDate = event.getStartDate() * 1000;
                endDate = event.getEndDate() * 1000;
            }
            long msDiff = endDate - startDate;
            long daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(startDate);

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
                        .equals("pm")) {
                    if (hour == 12)
                        calendar.set(calendar.HOUR_OF_DAY, hour);
                    else
                        calendar.set(Calendar.HOUR_OF_DAY, 12 + hour);

                } else
                    calendar.set(calendar.HOUR_OF_DAY, hour);

                calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
                Date date = calendar.getTime();
                alarm.setTime(date.getTime());
                alarm.setLabel(event.getEventTitle());
                alarm.setFollowUpId(followUPId);
                if (!TextUtils.isEmpty(event.getMedicationId()))
                    alarm.setMedicineId(event.getMedicationId());
                DatabaseHelper.getInstance(getApplicationContext())
                        .updateAlarm(alarm);

                AlarmReceiver.setReminderAlarm(this, alarm);

                calendar.add(Calendar.HOUR, 24 * (i + 1));
            }
        }
        Laila.instance().from_update_events = false;
        Laila.instance().remind_me_later = false;

        LoadAlarmsService.launchLoadAlarmsService(this);
    }
}

