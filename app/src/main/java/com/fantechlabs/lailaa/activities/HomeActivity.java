package com.fantechlabs.lailaa.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.developers.imagezipper.ImageZipper;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.databinding.ActivityHomeBinding;
import com.fantechlabs.lailaa.databinding.ActivitySigninBinding;
import com.fantechlabs.lailaa.models.Events;
import com.fantechlabs.lailaa.models.Profile;
import com.fantechlabs.lailaa.models.ProfileImages;
import com.fantechlabs.lailaa.request_models.ProfileRequest;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.view_models.InsertFollowUpViewModel;
import com.fantechlabs.lailaa.view_models.UpdateFollowUpViewModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.SneakyThrows;
import lombok.val;

import static com.fantechlabs.lailaa.utils.Constants.PROFILE;

//**********************************************************
public class HomeActivity extends BaseActivity
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
    private List<Events> mTodayEvents;
    private ArrayList<String> mSortedTimes;
    private int mCompareTime;
    private Date mDateOne, mDateTwo;
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
        gotoScreens();
    }

    //**********************************************************
    private void gotoScreens()
    //**********************************************************
    {
        mBinding.settings.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), SettingActivity.class)));
        mBinding.addMedication.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), MedicationActivity.class)));
        mBinding.medicalRecords.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), MedicalRecordsActivity.class)));
        mBinding.calender.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), CalenderActivity.class)));
        mBinding.telehealth.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), TelehealthActivity.class)));
        mBinding.profile.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), ProfileActivity.class)));

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

        if (mBase64Image == null)
        {
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
        if (ImagePicker.shouldHandle(requestCode, resultCode, data))
        {

            isImageSelected = true;
            Image image = ImagePicker.getFirstImageOrNull(data);
            mUri = data.getData();

            if (image != null)
            {

                mUri = Uri.fromFile(new File(image.getPath()));

                File file = new File(mUri.getPath());
                File imageZipperFile = null;
                try
                {
                    imageZipperFile = new ImageZipper(HomeActivity.this)
                            .setQuality(90)
                            .setMaxWidth(128)
                            .setMaxHeight(128)
                            .compressToFile(file);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                file = imageZipperFile == null ? file : imageZipperFile;

                mUri = Uri.fromFile(file);
                val result = AndroidUtil.getBytes(this, mUri);
                mBase64Image = Base64.encodeToString(result,  Base64.NO_WRAP);
                getDecodeImage(mBase64Image);
                Log.d("image", "imageUrl : " + mBase64Image);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //**********************************************************
    @Override
    protected boolean showStatusBar()
    //**********************************************************
    {
        return false;
    }
}