package com.fantechlabs.lailaa.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.permissions.Permission;
import com.google.zxing.Result;

import lombok.val;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

//***********************************************************
public class BarCodeScannerActivity extends AppCompatActivity
        implements ZXingScannerView.ResultHandler,
        Permission.OnResult, Permission.OnDenied
//***********************************************************
{
    private ZXingScannerView mScannerView;
    private static final String TAG = "qrCode";
    private Permission mPermission;

    //***********************************************************
    @Override
    protected void onCreate(Bundle savedInstanceState)
    //***********************************************************
    {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        initPermission();
    }

    //*********************************************************************
    private void initPermission()
    //*********************************************************************
    {
        mPermission = new Permission(
                new String[]{Manifest.permission.CAMERA}, BarCodeScannerActivity.this);
        mPermission.request(BarCodeScannerActivity.this);
    }

    //***********************************************************
    @Override
    public void onResume()
    //***********************************************************
    {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    //******************************************************************
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    //******************************************************************
    {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            mPermission.request(this);
    }

    //***********************************************************
    @Override
    public void onPause()
    //***********************************************************
    {
        super.onPause();
        mScannerView.stopCamera();
    }

    //***********************************************************
    @Override
    public void handleResult(Result rawResult)
    //***********************************************************
    {
        Log.d(TAG, rawResult.getText());
        val searchText = rawResult.getText();
        if (!TextUtils.isEmpty(searchText))
            checkNumber(searchText);
        //RxNumberFragment.QRResult = rawResult.getText();
        Laila.instance().setMSearchData(searchText);
        mScannerView.resumeCameraPreview(this);
        onBackPressed();
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

    //***********************************************************
    @Override
    public void onPermission(@NonNull String permission, @NonNull Permission.Status status)
    //***********************************************************
    {
        switch (status) {
            case Granted:
                mScannerView.startCamera();
                break;
            case DeniedForNow:
            case DeniedPermanently:
                break;
        }
    }

    //***********************************************************
    @Override
    public void onPermissionDenied(@NonNull String permission)
    //***********************************************************
    {
        AndroidUtil.toast(false, "" + permission);
    }
}