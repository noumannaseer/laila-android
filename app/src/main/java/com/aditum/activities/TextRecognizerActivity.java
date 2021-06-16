package com.aditum.activities;

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

import com.aditum.Laila;
import com.aditum.R;
import com.aditum.databinding.ActivityTextRecognizerBinding;
import com.aditum.utils.AndroidUtil;
import com.aditum.utils.permissions.Permission;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.nio.CharBuffer;

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
                new String[]{Manifest.permission.CAMERA}, TextRecognizerActivity.this);
        mPermission.request(TextRecognizerActivity.this);
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
//        FirebaseApp.initializeApp(getApplicationContext());
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
    private String getRxBlock(FirebaseVisionText firebaseVisionText)
    //*********************************************************************
    {
        val list = firebaseVisionText.getTextBlocks();
        if (list == null || list.size() == 0)
            return null;
        for (FirebaseVisionText.TextBlock textBlock : firebaseVisionText.getTextBlocks()) {
            String text = textBlock.getText();
            Log.d(TAG, "TextBlock - > " + textBlock.getText());
            if (!TextUtils.isEmpty(text)) {
                text = text.toLowerCase();
                if (text.contains("rx") || text.contains("din"))
                    return text;
            }
        }

        return null;
    }

    //*********************************************************************
    private void displayTextFromImage(FirebaseVisionText firebaseVisionText)
    //*********************************************************************
    {
        String rxNumber = null;
        String rxBlock = getRxBlock(firebaseVisionText);
        if (TextUtils.isEmpty(rxBlock)) {
            Log.d(TAG, "Block not found!");
            return;
        }
        Log.d(TAG, "rxBlock -> " + rxBlock);
        String[] textArray = rxBlock.split("\n");
        for (int i = 0; i < textArray.length; i++) {
            Log.d(TAG, "textArray[i] - > index = " + i + " value = > " + textArray[i]);
            if (textArray[i].contains("rx") || textArray[i].contains("din")) {
                rxNumber = textArray[i];
                break;
            }
        }
        Log.d(TAG, "RxNumber - >" + rxNumber);
        rxNumber = stripNonDigitsV2(rxNumber);
        Log.d(TAG, "PostStrip - >" + rxNumber);

        if (rxNumber == null || rxNumber.isEmpty()) {
            AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.there_not_any_rx_num_match), AndroidUtil.getString(R.string.alert), this);
            return;
        }
        Laila.instance().setMSearchData(rxNumber);
        Laila.instance().text_recognizer = true;
        Laila.instance().from_image_din = true;

        onBackPressed();
//        List<FirebaseVisionText.TextBlock> blockList = firebaseVisionText.getTextBlocks();
//        if (blockList.size() > 0 || blockList != null) {
//            for (FirebaseVisionText.TextBlock b : firebaseVisionText.getTextBlocks()) {
//                String text = b.getText();
//
//                if (!TextUtils.isEmpty(text)) {
//                    String[] textArray = text.split(" ");
//
//                    if (textArray == null || textArray.length == 0)
//                        return;
//                    val searchText = textArray[0];
//                    if (searchText == null || searchText.length() == 0)
//                        return;
//                    checkNumber(searchText);
//                    Laila.instance().setMSearchData(searchText);
//                    //AddMedicationFragment.TextRecognizerResult = textArray[0];
//                    Log.d(TAG, text);
//                    onBackPressed();
//                }
//
//            }
//        }
    }

    //***********************************************************
    public static String stripNonDigitsV2(CharSequence input)
    //***********************************************************
    {
        Log.d(TAG, "stripNonDigitsV2" + input);
        if (input == null)
            return null;
        if (input.length() == 0)
            return "";

        char[] result = new char[input.length()];
        int cursor = 0;
        CharBuffer buffer = CharBuffer.wrap(input);

        while (buffer.hasRemaining()) {
            char chr = buffer.get();
            if (chr > 47 && chr < 58)
                result[cursor++] = chr;
        }

        return new String(result, 0, cursor);
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