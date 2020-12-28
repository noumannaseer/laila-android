package com.fantechlabs.lailaa.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fantechlabs.lailaa.activities.BarCodeScannerActivity;
import com.fantechlabs.lailaa.activities.TextRecognizerActivity;
import com.fantechlabs.lailaa.databinding.FragmentQRScannerBinding;

//***********************************************************
public class QRScannerFragment extends BaseFragment
//***********************************************************

{

    private static final String TAG = "qrCode";
    private FragmentQRScannerBinding mBinding;
    private View mRootView;
    private static final int REQUEST_CODE_QR_SCAN = 101;
    public static String QRResult;


    //***********************************************************
    public QRScannerFragment()
    //***********************************************************
    {
        // Required empty public constructor
    }

    //*************************************************************************************************
    @Override
    public View onCreateViewBaseFragment(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    //*************************************************************************************************
    {
        if (mRootView == null) {
            mBinding = FragmentQRScannerBinding.inflate(inflater, parent, false);
            mRootView = mBinding.getRoot();
        }
        initControl();

        return mRootView;
    }

    //**********************************************
    private void initControl()
    //**********************************************
    {

        mBinding.searchBarCodeButton.setOnClickListener(v ->
        {
            startActivity(new Intent(getActivity(), BarCodeScannerActivity.class));
        });
        mBinding.searchByCodeImage.setOnClickListener(v ->
        {
            startActivity(new Intent(getActivity(), TextRecognizerActivity.class));
        });
    }

    //**************************************************************
    @Override
    public void onDestroy()
    //**************************************************************
    {
        super.onDestroy();
    }
}
