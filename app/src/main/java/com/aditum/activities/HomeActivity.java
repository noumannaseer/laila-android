package com.aditum.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.aditum.R;
import com.aditum.databinding.ActivityHomeBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.developers.imagezipper.ImageZipper;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.aditum.Laila;
import com.aditum.bodyreading.activities.BodyReadingActivity;
import com.aditum.models.response_models.FollowUpResponse;
import com.aditum.models.updates.models.ResponseEvent;
import com.aditum.utils.AndroidUtil;
import com.aditum.utils.UIUtils;
import com.aditum.view_models.InsertFollowUpViewModel;
import com.aditum.view_models.UpdateFollowUpViewModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lombok.SneakyThrows;
import lombok.val;

import static com.aditum.utils.Constants.NO;
import static com.aditum.utils.Constants.YES;

//**********************************************************
public class HomeActivity extends BaseActivity
        implements InsertFollowUpViewModel.InsertFollowUpModelListener,
        UpdateFollowUpViewModel.UpdateFollowUpModelListener
        //**********************************************************
{
    private ActivityHomeBinding mBinding;
    private String mBase64Image;
    private boolean isImageSelected = false;
    private Uri mUri;
    public static final String IS_NOTIFICATION = "IS_NOTIFICATION";
    private boolean mNotification = false;
    public static final String MEDICINE_ID = "MEDICINE_ID";
    public static final String MEDICINE_NAME = "MEDICINE_NAME";
    public static final String FOLLOW_UP_ID = "FOLLOW_UP_ID";
    private String mMedicineId, mEventName;
    private InsertFollowUpViewModel mInsertFollowUpViewModel;
    private UpdateFollowUpViewModel mUpdateFollowUpViewModel;
    private int mFollowUpId;
    private String mStatus;
    private boolean mDismiss = false;
    private RequestManager mGlideRequestManager;

    //**********************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //**********************************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setTitle("");
        initControls();
    }

    //**********************************************************
    private void initControls()
    //**********************************************************
    {

        initViews();
        getParcelable();
        openNotification();
        gotoScreens();
        setProfileImage();

        if (Laila.instance().getMUser_U() == null)
            return;
        if (Laila.instance().getMUser_U().getData() == null)
            return;
        val user = Laila.instance().getMUser_U().getData().getUser();
        val userProfile = Laila.instance().getMUser_U().getData().getProfile();
        if (user == null || userProfile == null || TextUtils.isEmpty(userProfile.getFirstName()))

            UIUtils.displayAlertDialog(
                    AndroidUtil.getString(R.string.profile_completion_is_required),
                    AndroidUtil.getString(R.string.profile),
                    this,
                    AndroidUtil.getString(
                            R.string.ok),
                    AndroidUtil.getString(
                            R.string.cancel),
                    (dialog, which) -> {
                        if (which == -1) {
                            startActivity(new Intent(this, ProfileActivity.class));
                        }
                    });

    }

    //*******************************************************************
    private void initViews()
    //*******************************************************************
    {
        mInsertFollowUpViewModel = new InsertFollowUpViewModel(this);
        mUpdateFollowUpViewModel = new UpdateFollowUpViewModel(this);
        mGlideRequestManager = Glide.with(this);
    }

    //*******************************************************************
    private void openNotification()
    //*******************************************************************
    {

        if (mNotification && !TextUtils.isEmpty(mMedicineId)) {
            AndroidUtil.displayAlertDialog(
                    AndroidUtil.getString(R.string.take_medicine),
                    AndroidUtil.getString(R.string.reminder),
                    this,
                    AndroidUtil.getString(
                            R.string.taken),
                    AndroidUtil.getString(
                            R.string.dismiss),
                    mFollowUpId != 0 ?
                            "" : AndroidUtil.getString(
                            R.string.remind_later),
                    (dialog, which) ->
                    {
                        switch (which) {
                            case -1:
                                if (!AndroidUtil.isNetworkStatusAvailable()) {
                                    AndroidUtil.displayAlertDialog(
                                            AndroidUtil.getString(R.string.Network_not_available),
                                            AndroidUtil.getString(R.string.Network_error),
                                            this, "Ok", (dialog1, which1) -> openNotification());
                                    return;
                                }
                                insertFollowUp(YES, false);
                                break;
                            case -2:
                                if (!AndroidUtil.isNetworkStatusAvailable()) {
                                    AndroidUtil.displayAlertDialog(
                                            AndroidUtil.getString(R.string.Network_not_available),
                                            AndroidUtil.getString(R.string.Network_error),
                                            this, "Ok", (dialog1, which1) -> openNotification());
                                    return;
                                }
                                insertFollowUp(NO, false);
                                break;
                            case -3:
                                if (!AndroidUtil.isNetworkStatusAvailable()) {
                                    AndroidUtil.displayAlertDialog(
                                            AndroidUtil.getString(R.string.Network_not_available),
                                            AndroidUtil.getString(R.string.Network_error),
                                            this, "Ok", (dialog1, which1) -> openNotification());

                                    return;
                                }
                                insertFollowUp(NO, true);
                                break;
                        }
                    });
        }

    }

    //*******************************************************************
    private void addLocalAlarm()
    //*******************************************************************
    {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, 30);

        Date time = now.getTime();
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String formattedTime = timeFormat.format(time).toUpperCase();


        ResponseEvent events = new ResponseEvent();
        events.setTimeSchedule(formattedTime);
        events.setStartDate(Calendar.getInstance().getTime().getTime());
        events.setEndDate(Calendar.getInstance().getTime().getTime());
        events.setContactId(1);
        events.setFrequency(1);
        events.setDeliveryType("1");
        events.setType("Medicine Alarm");
        events.setId(mFollowUpId);
        if (!TextUtils.isEmpty(mMedicineId))
            events.setMedicationId(mMedicineId);
        events.setEventTitle(mEventName);

        List<ResponseEvent> eventsList = new ArrayList<>();
        eventsList.add(events);
        Laila.instance().remind_me_later = true;
        Laila.instance().addMedicineAlarm(eventsList, mFollowUpId);
    }

    //*******************************************************************
    private void insertFollowUp(String status, boolean dismiss)
    //*******************************************************************
    {
        mDismiss = dismiss;
        if (!TextUtils.isEmpty(mMedicineId) && mFollowUpId != 0) {
            updateFollowUp(status);
            return;
        }
        if (Laila.instance().getMUser_U() == null)
            return;
        if (Laila.instance().getMUser_U().getData() == null)
            return;
        mStatus = status;
        val followUpRequest = Laila.instance()
                .getMFollowUpRequest().Builder();
        followUpRequest.setUserId(Laila.instance().getMUser_U().getData().getUser().getId().toString());
        followUpRequest.setToken(Laila.instance().getMUser_U().getData().getUser().getToken());
        followUpRequest.setMedicationId(mMedicineId);
        followUpRequest.setLogDateTime(UIUtils.getCurrentDate("yyyy-MM-dd") + " " + UIUtils.getCurrentTime("hh:mm:ss"));
        followUpRequest.setStatus(status);

        showLoadingDialog();
        mInsertFollowUpViewModel.insertFollowUp(followUpRequest);
    }

    //*******************************************************************
    private void updateFollowUp(String status)
    //*******************************************************************
    {
        val updateFollowUpRequest = Laila.instance()
                .getMFollowUpUpdateRequest().Builder();
        updateFollowUpRequest.setUserId(Laila.instance().getMUser_U().getData().getUser().getId().toString());
        updateFollowUpRequest.setToken(Laila.instance().getMUser_U().getData().getUser().getToken());
        updateFollowUpRequest.setMedicationId(mMedicineId);
        updateFollowUpRequest.setLogDateTime(UIUtils.getCurrentDate("yyyy-MM-dd") + " " + UIUtils.getCurrentTime("hh:mm:ss"));
        updateFollowUpRequest.setStatus(status);
        updateFollowUpRequest.setFollowupId(Integer.toString(mFollowUpId));

        showLoadingDialog();
        mUpdateFollowUpViewModel.updateFollowUp(updateFollowUpRequest);
    }

    //*********************************************************************
    private void getParcelable()
    //*********************************************************************
    {
        Bundle args = getIntent().getExtras();
        if (args != null) {
            mNotification = args.getBoolean(IS_NOTIFICATION);
            mFollowUpId = args.getInt(FOLLOW_UP_ID);
            mMedicineId = args.getString(MEDICINE_ID);
            mEventName = args.getString(MEDICINE_NAME);
        }
    }

    //**********************************************************
    private void gotoScreens()
    //**********************************************************
    {
        mBinding.settings.setOnClickListener(view -> startActivity(new Intent(this, SettingActivity.class)));
        mBinding.addMedication.setOnClickListener(view -> startActivity(new Intent(this, MedicationActivity.class)));
        mBinding.medicalRecords.setOnClickListener(view -> startActivity(new Intent(this, MedicalRecordsActivity.class)));
        mBinding.calender.setOnClickListener(view -> startActivity(new Intent(this, CalenderActivity.class)));
        mBinding.telehealth.setOnClickListener(view -> startActivity(new Intent(this, TelehealthActivity.class)));
        mBinding.profile.setOnClickListener(view -> startActivity(new Intent(this, ProfileActivity.class)));
        mBinding.bodyreading.setOnClickListener(view -> startActivity(new Intent(this, BodyReadingActivity.class)));
        mBinding.recources.setOnClickListener(view -> startActivity(new Intent(this, ResourcesActivity1.class)));

    }

    //******************************************************************************
    public void getDecodeImage(@NonNull String image)
    //******************************************************************************
    {
        val imageBytes = Base64.decode(image, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        mBinding.profile.setImageBitmap(decodedImage);
    }

    //*******************************************************************
    @Override
    public void onResume()
    //*******************************************************************
    {

        super.onResume();

        setProfileImage();
    }

    //*************************************************************
    private void setProfileImage()
    //*************************************************************
    {
        if (Laila.instance().getMUser_U() == null)
            return;
        if (Laila.instance().getMUser_U().getData() == null)
            return;
        if (Laila.instance().getMUser_U().getData().getProfile() == null)
            return;

        val image = Laila.instance().getMUser_U().getData().getProfile().getAvatar();
        if (image == null)
            return;

        mGlideRequestManager
                .load(image)
                .centerCrop()
                .placeholder(R.drawable.profile_icon)
                .thumbnail(0.1f)
                .into(mBinding.profile);
    }

    //*************************************************************
    @SneakyThrows
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    //*************************************************************
    {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {

            isImageSelected = true;
            Image image = ImagePicker.getFirstImageOrNull(data);
            mUri = data.getData();

            if (image != null) {

                mUri = Uri.fromFile(new File(image.getPath()));

                File file = new File(mUri.getPath());
                File imageZipperFile = null;
                try {
                    imageZipperFile = new ImageZipper(HomeActivity.this)
                            .setQuality(90)
                            .setMaxWidth(128)
                            .setMaxHeight(128)
                            .compressToFile(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                file = imageZipperFile == null ? file : imageZipperFile;

                mUri = Uri.fromFile(file);
                val result = AndroidUtil.getBytes(this, mUri);
                mBase64Image = Base64.encodeToString(result, Base64.NO_WRAP);
                getDecodeImage(mBase64Image);
                Log.d("image", "imageUrl : " + mBase64Image);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //*********************************************************************
    @Override
    public void onInsertFollowUpSuccess(@Nullable FollowUpResponse response)
    //*********************************************************************
    {
        hideLoadingDialog();
        if (mStatus.equals(NO) && mDismiss) {
            mFollowUpId = response.getData().getFolowup().getId();
            addLocalAlarm();
        }
        AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.thank_you), AndroidUtil.getString(R.string.alert), this);
    }

    //*********************************************************************
    @Override
    public void onInsertFollowUpFailed(String msg)
    //*********************************************************************
    {
        hideLoadingDialog();
        AndroidUtil.displayAlertDialog(msg, AndroidUtil.getString(R.string.error), this);
    }

    //*********************************************************************
    @Override
    public void onUpdateFollowUpSuccess(@Nullable String msg)
    //*********************************************************************
    {
        hideLoadingDialog();
        AndroidUtil.displayAlertDialog(msg, AndroidUtil.getString(R.string.alert), this);
    }

    //*********************************************************************
    @Override
    public void onUpdateFollowUpFailed(String msg)
    //*********************************************************************
    {
        hideLoadingDialog();
        AndroidUtil.displayAlertDialog(msg, AndroidUtil.getString(R.string.error), this);
    }

    //**********************************************************
    @Override
    protected boolean showStatusBar()
    //**********************************************************
    {
        return false;
    }
}