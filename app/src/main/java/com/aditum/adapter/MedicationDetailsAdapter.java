package com.aditum.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.aditum.fragments.AddMedicationFragment;
import com.aditum.fragments.QRScannerFragment;
import com.aditum.fragments.RxNumberFragment;

//****************************************************************
public class MedicationDetailsAdapter extends FragmentStatePagerAdapter
//****************************************************************

{

    private int no_of_tabs;

    //****************************************************************
    public MedicationDetailsAdapter(@NonNull FragmentManager fm, int no_of_tabs)
    //****************************************************************
    {
        super(fm);
        this.no_of_tabs = no_of_tabs;
    }

    //****************************************************************
    @NonNull
    @Override
    public Fragment getItem(int position)
    //****************************************************************
    {
        switch (position) {
            case 0:
                AddMedicationFragment addMedicationFragment = new AddMedicationFragment();
                return addMedicationFragment;
            case 1:
                QRScannerFragment qrScannerFragment = new QRScannerFragment();
                return qrScannerFragment;
            case 2:
                RxNumberFragment rxNumberFragment = new RxNumberFragment();
                return rxNumberFragment;
            default:
                return null;
        }
    }

    //****************************************************************
    @Override
    public int getCount()
    //****************************************************************
    {
        return no_of_tabs;
    }
}
