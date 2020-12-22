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

import com.developers.imagezipper.ImageZipper;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.adapter.ProfileViewPagerAdapter;
import com.fantechlabs.lailaa.databinding.ActivityHomeBinding;
import com.fantechlabs.lailaa.databinding.ActivityProfileBinding;
import com.fantechlabs.lailaa.fragments.ProfileOneFragment;
import com.fantechlabs.lailaa.fragments.ProfileThreeFragment;
import com.fantechlabs.lailaa.fragments.ProfileTwoFragment;
import com.fantechlabs.lailaa.models.Profile;
import com.fantechlabs.lailaa.models.ProfileImages;
import com.fantechlabs.lailaa.models.response_models.UpdateProfileResponse;
import com.fantechlabs.lailaa.request_models.ProfileRequest;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;
import com.fantechlabs.lailaa.utils.Permission;
import com.fantechlabs.lailaa.utils.SharedPreferencesUtils;
import com.fantechlabs.lailaa.view_models.UpdateProfileViewModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;
import lombok.val;

//*********************************************************
public class ProfileActivity extends BaseActivity
        implements UpdateProfileViewModel.UpdateProfileListener,
        Permission.OnResult
//*********************************************************
{
    private ActivityProfileBinding mBinding;
    private ProfileViewPagerAdapter mProfileViewPagerAdapter;
    private Uri mUri;
    private String mBase64Image;
    private boolean isImageSelected = false;
    private ProfileRequest mUser;
    private UpdateProfileViewModel mUpdateProfileViewModel;
    private Permission mPermission;
    private final int PICK_IMAGE_CAMERA = 1;
    private AlertDialog.Builder builder;
    private boolean isCalled = false;

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
        initControl();

    }

    //*****************************************************************
    private void initControl()
    //*****************************************************************
    {
        Laila.instance().setMProfileRequest(new ProfileRequest());
        Laila.instance().getMProfileRequest().setProfile(new Profile());

        if (Laila.instance().getMUser() == null || Laila.instance().getMUser().getProfile() == null)
            return;

        val userProfile = Laila.instance().getMUser().getProfile();

        val newProfile = AndroidUtil.cloneObject(userProfile);

        Laila.instance().getMProfileRequest().setProfile((Profile) newProfile);

        val mUser = Laila.instance().getMProfileRequest();

        mUser.getProfile().setPublicCode(Laila.instance().getMUser().getProfile().getPublicCode());

        mUpdateProfileViewModel = new UpdateProfileViewModel(this);

        mBinding.profileTab.setupWithViewPager(mBinding.profileViewpager);
        imageClick();
        setupViewPager();

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
        val check = Laila.instance().Edit_Profile;
        if (check) {
            onUpdate();
            return;
        }
        mBinding.updateButton.setImageResource(R.drawable.check);
        Laila.instance().Edit_Profile = true;
        imageClick();
        ProfileTwoFragment.mCounter = 0;
        setupViewPager();
    }

    //*****************************************************************
    private void setupViewPager()
    //*****************************************************************
    {
        mProfileViewPagerAdapter = new ProfileViewPagerAdapter(getSupportFragmentManager());
        mProfileViewPagerAdapter.addFragment(new ProfileOneFragment(), AndroidUtil.getString(R.string.about_me));
        mProfileViewPagerAdapter.addFragment(new ProfileTwoFragment(), AndroidUtil.getString(R.string.address));
        mProfileViewPagerAdapter.addFragment(new ProfileThreeFragment(), AndroidUtil.getString(R.string.health));
        mBinding.profileViewpager.setAdapter(mProfileViewPagerAdapter);
    }

    //*********************************************
    private void imageClick()
    //*********************************************
    {
        val check = Laila.instance().Edit_Profile;
        if (check)
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

    //******************************************************************************
    public void getDecodeImage(@NonNull String image)
    //******************************************************************************
    {
        val imageBytes = Base64.decode(image, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        mBinding.profileImage.setImageBitmap(decodedImage);
    }

    //******************************************************************************
    private void onUpdate()
    //******************************************************************************
    {

        if (Laila.instance().getMProfileRequest().getProfile() == null && Laila.instance().getMProfileRequest().getImages() == null) {
            AndroidUtil.displayAlertDialog("Please Update fields", AndroidUtil.getString(R.string.update), this);
            return;
        }

        val profile = Laila.instance().getMProfileRequest().getProfile();

        val firstName = profile.getFirstName();
        val lastName = profile.getLastName();
        val lang = profile.getPrefLang();
        val zipCode = profile.getAddressPobox();
        val country = profile.getAddressCountry();
        val city = profile.getAddressCity();
        val phoneNo = profile.getPhone();
        val height = profile.getHeight();
        val weight = profile.getWeight();
        val dob = profile.getDateOfBirth();
        val address1 = profile.getAddressLine1();
        val addressProvince = profile.getAddressProvince();


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
            AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.address1_required), AndroidUtil.getString(R.string.alert), this);
            return;
        }
        if (TextUtils.isEmpty(zipCode)) {
            AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.zip_code_required), AndroidUtil.getString(R.string.alert), this);
            return;
        }
        if (TextUtils.isEmpty(country)) {
            AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.country_required), AndroidUtil.getString(R.string.alert), this);
            return;
        }
        if (TextUtils.isEmpty(addressProvince)) {
            AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.province_required), AndroidUtil.getString(R.string.alert), this);
            return;
        }
        if (TextUtils.isEmpty(city)) {
            AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.city_required), AndroidUtil.getString(R.string.alert), this);
            return;
        }
        if (TextUtils.isEmpty(phoneNo)) {
            AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.phone_required), AndroidUtil.getString(R.string.alert), this);
            return;
        }
        if (height == 0) {
            AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.height_required), AndroidUtil.getString(R.string.alert), this);
            return;
        }
        if (weight == 0) {
            AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.weight_required), AndroidUtil.getString(R.string.alert), this);
            return;
        }

        showLoadingDialog();
        mUpdateProfileViewModel.updateProfile(Laila.instance().getMProfileRequest());
    }

    //******************************************************************************
    @Override
    public void onUpdateSuccessfully(@Nullable UpdateProfileResponse response)
    //******************************************************************************
    {
        hideLoadingDialog();

        if (response.getSuccess().getImages() != null) {
            List<ProfileImages> images = new ArrayList<>();
            images.add(0, response.getSuccess().getImages());
            Laila.instance().getMUser().setImages(images);
        }

        Laila.instance().getMUser().setProfile(Laila.instance().getMProfileRequest().getProfile());
        SharedPreferencesUtils.setValue(Constants.USER_DATA, Laila.instance().getMUser());

        AndroidUtil.displayAlertDialog(
                response.getSuccess().getResult(),
                AndroidUtil.getString(R.string.profile),
                this,
                AndroidUtil.getString(
                        R.string.ok),
                (dialog, which) ->
                {
                    if (which == -1) {
                        Intent intent = new Intent(this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                });

    }

    //******************************************************************************
    @Override
    public void onUpdateFailed(@NonNull String errorMessage)
    //******************************************************************************
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
}