package com.fantechlabs.lailaa.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.developers.imagezipper.ImageZipper;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.adapter.ProfileViewPagerAdapter;
import com.fantechlabs.lailaa.databinding.ActivityProfileBinding;
import com.fantechlabs.lailaa.fragments.ProfileOneFragment;
import com.fantechlabs.lailaa.fragments.ProfileThreeFragment;
import com.fantechlabs.lailaa.fragments.ProfileTwoFragment;
import com.fantechlabs.lailaa.models.updates.models.Profile;
import com.fantechlabs.lailaa.models.updates.request_models.AvatarRequest;
import com.fantechlabs.lailaa.models.updates.request_models.ProfileRequest;
import com.fantechlabs.lailaa.models.updates.response_models.AvatarResponse;
import com.fantechlabs.lailaa.models.updates.response_models.ProfileResponse;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;
import com.fantechlabs.lailaa.utils.SharedPreferencesUtils;
import com.fantechlabs.lailaa.utils.permissions.Permission;
import com.fantechlabs.lailaa.view_models.AvatarViewModel;
import com.fantechlabs.lailaa.view_models.UpdateProfileViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;
import lombok.val;
import rx.internal.util.LinkedArrayList;

//*********************************************************
public class ProfileActivity extends BaseActivity
        implements UpdateProfileViewModel.UpdateProfileListener,
        AvatarViewModel.AvatarListener,
        Permission.OnResult
//*********************************************************
{
    private ActivityProfileBinding mBinding;
    private ProfileViewPagerAdapter mProfileViewPagerAdapter;
    private Uri mUri;
    private String mBase64Image;
    private boolean isImageSelected = false;
    private UpdateProfileViewModel mUpdateProfileViewModel;
    private AvatarViewModel mAvatarViewModel;
    private Permission mPermission;
    private AlertDialog.Builder builder;
    private boolean isCalled = false;
    private String mImageUrl;
    private String mHeight, mWeight, mHeightUnit, mWeightUnit;
    private RequestManager mGlideRequestManager;
    private ProfileRequest mProfileRequest;
    private Profile mUserProfile;

    //*********************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //*********************************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("");
        mBinding.toolbar.setNavigationOnClickListener(view -> {
            Laila.instance().setMProfileRequest(null);
            onBackPressed();
        });
        initControl();

    }

    //*****************************************************************
    private void initControl()
    //*****************************************************************
    {
        initViews();
        imageClick();
        setupViewPager();
        updateProfile();

        val userDetail = Laila.instance().getMUser_U().getData();
        if (userDetail.getUser() == null)
            return;
        if (userDetail.getProfile() != null) {
            val image = userDetail.getProfile().getAvatar();
            if (image == null)
                return;
            if (image.isEmpty())
                return;
            mGlideRequestManager
                    .load(image)
                    .centerCrop()
                    .thumbnail(0.1f)
                    .into(mBinding.profileImage);
        }


    }

    //*******************************************
    private void initViews()
    //*******************************************
    {
        mUpdateProfileViewModel = new UpdateProfileViewModel(this);
        mAvatarViewModel = new AvatarViewModel(this, ProfileActivity.this);
        mBinding.profileTab.setupWithViewPager(mBinding.profileViewpager);
        mGlideRequestManager = Glide.with(this);

    }

    //*******************************************
    private void updateProfile()
    //*******************************************
    {
        mBinding.updateButton.setOnClickListener(v -> {
            setEditProfile();
        });
    }

    //*******************************************
    private void selectImage()
    //*******************************************
    {
        final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};

        builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                dialog.dismiss();
                if (options[item].equals("Take Photo")) {
                    dialog.dismiss();
                    ImagePicker.cameraOnly().start(ProfileActivity.this);
                } else if (options[item].equals("Choose From Gallery")) {
                    dialog.dismiss();
                    ImagePicker.create(ProfileActivity.this).single().start();
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    //*************************************************
    private void setEditProfile()
    //*************************************************
    {
        onUpdate();
        mBinding.updateButton.setImageResource(R.drawable.check);
        imageClick();
        ProfileTwoFragment.mCounter = 0;
        Laila.instance().is_edit_profile_fields = false;
        Laila.instance().setMProfileRequest(null);
        setupViewPager();
    }

    //*****************************************************************
    private void setupViewPager()
    //*****************************************************************
    {
        mProfileViewPagerAdapter = new ProfileViewPagerAdapter(getSupportFragmentManager());
        mProfileViewPagerAdapter.addFragment(new ProfileOneFragment(), AndroidUtil.getString(R.string.about));
        mProfileViewPagerAdapter.addFragment(new ProfileTwoFragment(), AndroidUtil.getString(R.string.address));
        mProfileViewPagerAdapter.addFragment(new ProfileThreeFragment(), AndroidUtil.getString(R.string.health));
        mBinding.profileViewpager.setAdapter(mProfileViewPagerAdapter);
    }

    //*********************************************
    private void imageClick()
    //*********************************************
    {
        mBinding.profileImage.setOnClickListener(View -> initPermission());
    }

    //*********************************************************************
    private void initPermission()
    //*********************************************************************
    {
        isCalled = false;
        mPermission = new Permission(
                new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, ProfileActivity.this);
        mPermission.request(ProfileActivity.this);
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
                mImageUrl = mUri.toString();
                File file = new File(mUri.getPath());
                File imageZipperFile = null;
                try {
                    imageZipperFile = new ImageZipper(ProfileActivity.this)
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

    //*********************************************
    public void getDecodeImage(@NonNull String image)
    //*********************************************
    {
        val imageBytes = Base64.decode(image, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        mBinding.profileImage.setImageBitmap(decodedImage);
    }

    //*******************************************
    private void onUpdate()
    //*******************************************
    {

        if (Laila.instance().getMProfileRequest() == null) {
            AndroidUtil.displayAlertDialog("Please Update fields", AndroidUtil.getString(R.string.update), this);
            return;
        }
        mProfileRequest = Laila.instance().getMProfileRequest();
        mUserProfile = Laila.instance().getMUser_U().getData().getProfile();

        val firstName = mProfileRequest.getFirstName();
        val lastName = mProfileRequest.getLastName();
        val lang = mProfileRequest.getPrefLang();
        val zipCode = mProfileRequest.getAddressPobox();
        val country = mProfileRequest.getAddressCountry();
        val city = mProfileRequest.getAddressCity();
        val phoneNo = mProfileRequest.getPhone();
        val dob = mProfileRequest.getDateOfBirth();
        val address1 = mProfileRequest.getAddressLine1();
        val addressProvince = mProfileRequest.getAddressProvince();

        if (mProfileRequest.getOrganDonor() == null || mProfileRequest.getOrganDonor().isEmpty()) {
            mProfileRequest.setOrganDonor(mUserProfile.getOrganDonor());
            Laila.instance().setMProfileRequest(mProfileRequest);
        }

        if (TextUtils.isEmpty(firstName)) {
            AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.first_name_required), AndroidUtil.getString(R.string.alert), this);
            return;
        }
        if (TextUtils.isEmpty(lastName)) {
            AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.last_name_required), AndroidUtil.getString(R.string.alert), this);
            return;
        }
        if (TextUtils.isEmpty(lang)) {
            AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.language_required), AndroidUtil.getString(R.string.alert), this);
            return;
        }
        if (TextUtils.isEmpty(dob)) {
            AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.dob_required), AndroidUtil.getString(R.string.alert), this);
            return;
        }
        if (TextUtils.isEmpty(address1)) {
            AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.address1_required),
                    AndroidUtil.getString(R.string.alert),
                    this,
                    "Ok", (dialogInterface, i) -> navigateToNextScreen(1));
            return;
        }
        if (TextUtils.isEmpty(zipCode)) {
            AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.zip_code_required), AndroidUtil.getString(R.string.alert),
                    this,
                    "Ok", (dialogInterface, i) -> navigateToNextScreen(1));
            return;
        }
        if (TextUtils.isEmpty(country)) {
            AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.country_required), AndroidUtil.getString(R.string.alert), this,
                    "Ok", (dialogInterface, i) -> navigateToNextScreen(1));
            return;
        }
        if (TextUtils.isEmpty(addressProvince)) {
            AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.province_required), AndroidUtil.getString(R.string.alert), this,
                    "Ok", (dialogInterface, i) -> navigateToNextScreen(1));
            return;
        }
        if (TextUtils.isEmpty(city)) {
            AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.city_required), AndroidUtil.getString(R.string.alert), this,
                    "Ok", (dialogInterface, i) -> navigateToNextScreen(1));
            return;
        }
        if (TextUtils.isEmpty(phoneNo)) {
            AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.phone_required), AndroidUtil.getString(R.string.alert), this,
                    "Ok", (dialogInterface, i) -> navigateToNextScreen(1));
            return;
        }
        setHealthData();
        if (mHeight.isEmpty() || mHeight == null) {
            AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.height_required), AndroidUtil.getString(R.string.alert), this,
                    "Ok", (dialogInterface, i) -> navigateToNextScreen(2));
            return;
        }
        if (mWeight.isEmpty()) {
            AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.weight_required), AndroidUtil.getString(R.string.alert), this,
                    "Ok", (dialogInterface, i) -> navigateToNextScreen(2));
            return;
        }
        val profileRequest = Laila.instance().getMProfileRequest();
        showLoadingDialog();
        mUpdateProfileViewModel.updateProfile(profileRequest);
    }

    //**********************************************
    private void setHealthData()
    //**********************************************
    {
        if (mProfileRequest.getOrganDonor().isEmpty()) {
            mProfileRequest.setOrganDonor("No");
            Laila.instance().setMProfileRequest(mProfileRequest);
        }

        if (mProfileRequest.getHeight() != null)
            mHeight = mProfileRequest.getHeight();
        else {
            mHeight = mUserProfile.getHeight().toString();
            mProfileRequest.setHeight(mHeight);
        }
        if (mProfileRequest.getWeight() != null)
            mWeight = mProfileRequest.getWeight();
        else {
            mWeight = mUserProfile.getWeight().toString();
            mProfileRequest.setWeight(mWeight);
        }
        if (mProfileRequest.getHeightUnit() != null)
            mHeightUnit = mProfileRequest.getHeightUnit();
        else {
            mHeightUnit = mUserProfile.getHeightUnit();
            mProfileRequest.setHeightUnit(mHeightUnit);
        }
        if (mProfileRequest.getWeightUnit() != null)
            mWeightUnit = mProfileRequest.getWeightUnit();
        else {
            mWeightUnit = mUserProfile.getWeightUnit();
            mProfileRequest.setWeightUnit(mWeightUnit);
        }
        if (mProfileRequest.getHealthCardNumber() == null) {
            val healthCardNo = mUserProfile.getHealthCardNumber();
            mProfileRequest.setHealthCardNumber(healthCardNo);
        }
        if (mProfileRequest.getPrivateInsurance() == null) {
            val insuranceName = mUserProfile.getPrivateInsurance();
            mProfileRequest.setPrivateInsurance(insuranceName);
        }
        if (mProfileRequest.getPrivateInsuranceNumber() == null) {
            val insurancePolicyNo = mUserProfile.getPrivateInsuranceNumber();
            mProfileRequest.setPrivateInsuranceNumber(insurancePolicyNo);
        }
        if (mProfileRequest.getAllergies() == null) {
            val allergies = mUserProfile.getAllergies();
            mProfileRequest.setAllergies(allergies);
        }
        if (mProfileRequest.getMedicalConditions() == null) {
            val conditions = mUserProfile.getMedicalConditions();
            mProfileRequest.setMedicalConditions(conditions);
        }
        Laila.instance().setMProfileRequest(mProfileRequest);
    }

    //**********************************************************
    public void navigateToNextScreen(int index)
    //****************************************************************
    {
        mBinding.profileViewpager.setCurrentItem(index);
    }

    //*******************************************************************
    private void getProfile()
    //*******************************************************************
    {
        Laila.instance().is_get_profile = true;
        showLoadingDialog();
        mUpdateProfileViewModel.getProfile();
    }

    //******************************************************************************
    @Override
    public void onUpdateSuccessfully(@Nullable ProfileResponse response)
    //******************************************************************************
    {
        hideLoadingDialog();
        if (!Laila.instance().is_get_profile) {
            if (mImageUrl != null)
                uploadAvatar();
            Laila.instance().getMUser_U().getData().setProfile(response.getData().getUpdatedProfile());
            SharedPreferencesUtils.setValue(Constants.USER_DATA, Laila.instance().getMUser_U());
            AndroidUtil.displayAlertDialog(
                    response.getData().getMessage(),
                    AndroidUtil.getString(R.string.profile),
                    this,
                    AndroidUtil.getString(
                            R.string.ok),
                    (dialog, which) ->
                    {
                        if (which == -1) {
                            getProfile();
                        }
                    });
            return;
        }
        val profile = response.getData().getProfile();
        if (profile == null)
            return;
        Laila.instance().getMUser_U().getData().setProfile(profile);
        SharedPreferencesUtils.setValue(Constants.USER_DATA, Laila.instance().getMUser_U());
        Laila.instance().is_get_profile = false;
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }

    //************************************************
    private void uploadAvatar()
    //************************************************
    {
        val user_id = Laila.instance().getMUser_U().getData().getUser().getId();
        val token = Laila.instance().getMUser_U().getData().getUser().getToken();

        AvatarRequest avatarRequest = new AvatarRequest();
        avatarRequest.setFile(mImageUrl);
        avatarRequest.setUser_id(user_id);
        avatarRequest.setToken(token);

        showLoadingDialog();
        mAvatarViewModel.uploadAvatar(avatarRequest);
    }

    //************************************************
    @Override
    public void onUpdateFailed(@NonNull String errorMessage)
    //*************************************************
    {
        hideLoadingDialog();
        AndroidUtil.displayAlertDialog(errorMessage, AndroidUtil.getString(R.string.error), this);
    }

    //********************************************
    @Override
    public void onBackPressed()
    //********************************************
    {
        super.onBackPressed();
        hideLoadingDialog();
        Laila.instance().setMProfileRequest(null);

    }

    //******************************************************************************
    @Override
    public void onPermission(@NonNull String permission, @NonNull Permission.Status status)
    //******************************************************************************
    {
        if (AndroidUtil.checkPermission(Manifest.permission.CAMERA)
                && AndroidUtil.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                & !isCalled) {
            isCalled = true;
            selectImage();
        }
    }

    //*********************************************************
    @Override
    protected boolean showStatusBar()
    //*********************************************************
    {
        return false;
    }

    //*********************************************************
    @Override
    public void onSuccessfullyUploadAvatar(@Nullable AvatarResponse avatarResponse)
    //*********************************************************
    {
        hideLoadingDialog();
        Laila.instance().getMUser_U().getData().setAvatar(avatarResponse.getData().getAvatar());
        Laila.instance().getMUser_U().getData().getProfile().setAvatar(avatarResponse.getData().getAvatar().getAvatarUrl());
        SharedPreferencesUtils.setValue(Constants.USER_DATA, Laila.instance().getMUser_U());
    }

    //*********************************************************
    @Override
    public void onFailedUploadAvatar(@NonNull String errorMessage)
    //*********************************************************
    {
        hideLoadingDialog();
        AndroidUtil.displayAlertDialog(errorMessage, AndroidUtil.getString(R.string.error), this);
    }

    //*******************************************************************
    @Override
    public void onResume()
    //*******************************************************************
    {

        super.onResume();

        if (Laila.instance().getMUser_U().getData() == null || Laila.instance().getMUser_U().getData().getProfile() == null)
            return;
        val userDetail = Laila.instance().getMUser_U().getData().getProfile();

        if (userDetail == null)
            return;
        mBinding.userName.setText(userDetail.getFirstName() + " " + userDetail.getLastName());

        if (userDetail.getFirstName().isEmpty() || userDetail.getLastName().isEmpty()) {
            val email = userDetail.getEmail();
            String[] name = email.split("@");
            mBinding.userName.setText(name[0]);
        }

        if (isImageSelected) {
            getDecodeImage(mBase64Image);
            isImageSelected = false;
        }
    }

}