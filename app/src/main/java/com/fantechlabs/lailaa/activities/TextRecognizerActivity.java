package com.fantechlabs.lailaa.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.databinding.ActivityTextRecognizerBinding;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.permissions.Permission;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.util.List;

import lombok.val;

//*********************************************************************
public class TextRecognizerActivity extends BaseActivity
        implements Permission.OnResult, Permission.OnDenied
//*********************************************************************
{

    private ActivityTextRecognizerBinding mBinding;
    private static final String TAG = "textRecognizer";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap mImageBitmap;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private Permission mPermission;


    //*********************************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //*********************************************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_text_recognizer);
        initControl();
    }

    //*********************************************************************
    @Override
    protected boolean showStatusBar()
    //*********************************************************************
    {
        return false;
    }



    //*********************************************************************
    private void initControl()
    //*********************************************************************
    {
        initPermission();
    }

    //*********************************************************************
    private void initPermission()
    //*********************************************************************
    {
        mPermission = new Permission(
                new String[]{Manifest.permission.CAMERA},  TextRecognizerActivity.this);
        mPermission.request( TextRecognizerActivity.this);
    }

    //******************************************************************
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    //******************************************************************
    {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            mPermission.request(this);
    }

    //*********************************************************************
    private void takePictureIntent()
    //*********************************************************************
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    //*********************************************************************
    private void detectTextFromImage()
    //*********************************************************************
    {
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(mImageBitmap);
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        detector.processImage(firebaseVisionImage).addOnSuccessListener(firebaseVisionText ->
                displayTextFromImage(firebaseVisionText)).addOnFailureListener(e ->
        {
            AndroidUtil.toast(false, "Error : " + e.getLocalizedMessage());
            Log.d(TAG, "error " + e.getLocalizedMessage());
        });
    }

    //*********************************************************************
    private void displayTextFromImage(FirebaseVisionText firebaseVisionText)
    //*********************************************************************
    {
        List<FirebaseVisionText.TextBlock> blockList = firebaseVisionText.getTextBlocks();
        if (blockList.size() > 0 || blockList != null) {
            for (FirebaseVisionText.TextBlock b : firebaseVisionText.getTextBlocks()) {
                String text = b.getText();

                if (!TextUtils.isEmpty(text)) {
                    String[] textArray = text.split(" ");

                    if (textArray == null || textArray.length == 0)
                        return;
                    val searchText = textArray[0];
                    if (searchText == null || searchText.length() == 0)
                        return;
                    checkNumber(searchText);
                    Laila.instance().setMSearchData(searchText);
                    //AddMedicationFragment.TextRecognizerResult = textArray[0];
                    Log.d(TAG, text);
                    onBackPressed();
                }

            }
        }
    }

    //***********************************************************
    private void checkNumber(@NonNull String text)
    //***********************************************************
    {
        if (text.matches("[0-9]+")) {
            Laila.instance().Bar_code = true;
            return;
        }
        Laila.instance().text_recognizer = true;
    }

    //*********************************************************************
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    //*********************************************************************
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            mImageBitmap = (Bitmap) extras.get("data");
            mBinding.imageContainer.setImageBitmap(mImageBitmap);
            detectTextFromImage();
        }
    }

    //*********************************************************************
    @Override
    public void onPermission(@NonNull String permission, @NonNull Permission.Status status)
    //*********************************************************************
    {
        switch (status) {
            case Granted:
                takePictureIntent();
                break;
            case DeniedForNow:
            case DeniedPermanently:
                AndroidUtil.toast(false, status.name());
                break;
        }
    }

    //*********************************************************************
    @Override
    public void onPermissionDenied(@NonNull String permission)
    //*********************************************************************
    {
        AndroidUtil.toast(false, "" + permission);
    }

}