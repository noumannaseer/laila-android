package com.fantechlabs.lailaa.activities;

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

import com.developers.imagezipper.ImageZipper;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.bodyreading.activities.BodyReadingActivity;
import com.fantechlabs.lailaa.databinding.ActivityHomeBinding;
import com.fantechlabs.lailaa.models.Events;
import com.fantechlabs.lailaa.models.Profile;
import com.fantechlabs.lailaa.models.ProfileImages;
import com.fantechlabs.lailaa.models.response_models.FollowUpResponse;
import com.fantechlabs.lailaa.request_models.ProfileRequest;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.UIUtils;
import com.fantechlabs.lailaa.view_models.InsertFollowUpViewModel;
import com.fantechlabs.lailaa.view_models.UpdateFollowUpViewModel;

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

import static com.fantechlabs.lailaa.utils.Constants.NO;
import static com.fantechlabs.lailaa.utils.Constants.PROFILE;
import static com.fantechlabs.lailaa.utils.Constants.YES;

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
    private ProfileRequest mUser;
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
        mInsertFollowUpViewModel = new InsertFollowUpViewModel(this);
        mUpdateFollowUpViewModel = new UpdateFollowUpViewModel(this);
        getParcelable();
        openNotification();
        gotoScreens();
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
                                insertFollowUp(YES, false);
                                break;
                            case -2:
                                insertFollowUp(NO, false);
                                break;
                            case -3:
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

        Events events = new Events();
        events.setTimeSchedule(formattedTime);
        events.setStartDate(UIUtils.getCurrentDate("dd-MMM-YYYY") + " 08:00AM");
        events.setEndDate(UIUtils.getCurrentDate("dd-MMM-YYYY") + " 11:59PM");
        events.setFollowupId(mFollowUpId);
        if (!TextUtils.isEmpty(mMedicineId))
            events.setMedicationId(Integer.valueOf(mMedicineId));
        events.setEventTitle(mEventName);

        List<Events> eventsList = new ArrayList<>();
        eventsList.add(events);

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

        mStatus = status;
        val followUpRequest = Laila.instance()
                .getMFollowUpRequest().Builder();
        followUpRequest.setUser_private_code(Laila.instance().getCurrentUserProfile().getUserPrivateCode());
        followUpRequest.setMedication_id(mMedicineId);
        followUpRequest.setLog_DateTime(UIUtils.getCurrentDate("yyyy-MM-dd") + " " + UIUtils.getCurrentTime("hh:mm:ss"));
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
        updateFollowUpRequest.setUser_private_code(Laila.instance().getCurrentUserProfile().getUserPrivateCode());
        updateFollowUpRequest.setMedication_id(mMedicineId);
        updateFollowUpRequest.setLog_DateTime(UIUtils.getCurrentDate("yyyy-MM-dd") + " " + UIUtils.getCurrentTime("hh:mm:ss"));
        updateFollowUpRequest.setStatus(status);
        updateFollowUpRequest.setFollowup_id(mFollowUpId);

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
        mBinding.recources.setOnClickListener(view -> startActivity(new Intent(this, ResourcesActivity.class)));

    }

    //**********************************************************
    private void setProfileImage()
    //**********************************************************
    {
        mUser = new ProfileRequest();
        mUser.setProfile(new Profile());

        if (Laila.instance().getMUser() == null || Laila.instance().getMUser().getProfile() == null)
            return;
        val imageList = Laila.instance().getMUser().getImages();

        ProfileImages image = new ProfileImages();
        image.setUserPrivateCode(Laila.instance().getMUser().getProfile().getUserPrivateCode());
        if (imageList != null)
            image.setId(imageList.get(0).getId());
        image.setImageData(mBase64Image);
        image.setType(PROFILE);
        image.setImageTitle("");
        image.setImageDescription("");

        List<ProfileImages> images = mUser.getImages();
        images = new ArrayList<>();
        images.add(image);

        if (mBase64Image == null) {
            mUser.setImages(null);
            return;
        }

        mUser.setImages(images);
        Laila.instance().getMProfileRequest().setImages(mUser.getImages());

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
        if (Laila.instance().getMUser() == null || Laila.instance().getMUser().getProfile() == null)
            return;
        if (Laila.instance().getMUser() == null || Laila.instance().getMUser().getImages() == null)
            return;

        val image = Laila.instance().getMUser().getImages().get(0).getImageData();

        if (isImageSelected) {
            getDecodeImage(mBase64Image);
            isImageSelected = false;
            return;
        }
        getDecodeImage(image);
    }

    //*******************************************************************************************
    @SneakyThrows
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    //*******************************************************************************************
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
            mFollowUpId = response.getFollowupId();
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