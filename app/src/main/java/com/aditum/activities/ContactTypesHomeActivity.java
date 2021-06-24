package com.aditum.activities;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.aditum.Laila;
import com.aditum.R;
import com.aditum.databinding.ActivityContactTypesHomeBinding;
import com.aditum.utils.Constants;

import lombok.val;

//***********************************************************
public class ContactTypesHomeActivity extends BaseActivity
//***********************************************************
{
    private ActivityContactTypesHomeBinding mBinding;

    //***********************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //***********************************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_contact_types_home);
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("");
        intiControls();
    }

    //***********************************************************
    private void intiControls()
    //***********************************************************
    {
        gotoFamilyDoctorContact();
        gotoSpecialistContact();
        gotoFamilyContact();
        gotoFriendsContact();
        gotoCareGiverContacts();
        gotoEmergencyContacts();
        gotoPharmacyActivity();
    }

    //***********************************************************
    private void gotoFamilyDoctorContact()
    //***********************************************************
    {
        val contactTitle = mBinding.familyDoctor.getText().toString();

        mBinding.addFamilyDoctor.setOnClickListener(view -> {
            Laila.instance().setMContactType(Constants.Family_Doctor_Contacts);
            Intent intent = new Intent(ContactTypesHomeActivity.this, ContactsActivity.class);
            intent.putExtra(Constants.CONTACT_TITLE, contactTitle);
            startActivity(intent);
        });
    }

    //***********************************************************
    private void gotoSpecialistContact()
    //***********************************************************
    {
        val contactTitle = mBinding.specialistText.getText().toString();

        mBinding.specialistDoctor.setOnClickListener(view -> {
            Laila.instance().setMContactType(Constants.Special);
            Intent intent = new Intent(ContactTypesHomeActivity.this, ContactsActivity.class);
            intent.putExtra(Constants.CONTACT_TITLE, contactTitle);
            startActivity(intent);
        });
    }

    //***********************************************************
    private void gotoFamilyContact()
    //***********************************************************
    {
        val contactTitle = mBinding.familyContactText.getText().toString();

        mBinding.familyContacts.setOnClickListener(view -> {
            Laila.instance().setMContactType(Constants.Family);
            Intent intent = new Intent(ContactTypesHomeActivity.this, ContactsActivity.class);
            intent.putExtra(Constants.CONTACT_TITLE, contactTitle);
            startActivity(intent);
        });
    }

    //***********************************************************
    private void gotoFriendsContact()
    //***********************************************************
    {
        val contactTitle = mBinding.friendsContactText.getText().toString();

        mBinding.friendsContacts.setOnClickListener(view -> {
            Laila.instance().setMContactType(Constants.Friends);
            Intent intent = new Intent(ContactTypesHomeActivity.this, ContactsActivity.class);
            intent.putExtra(Constants.CONTACT_TITLE, contactTitle);
            startActivity(intent);
        });
    }

    //***********************************************************
    private void gotoCareGiverContacts()
    //***********************************************************
    {
        val contactTitle = mBinding.careGiverText.getText().toString();

        mBinding.careGiver.setOnClickListener(view -> {
            Laila.instance().setMContactType(Constants.Caregiver);
            Intent intent = new Intent(ContactTypesHomeActivity.this, ContactsActivity.class);
            intent.putExtra(Constants.CONTACT_TITLE, contactTitle);
            startActivity(intent);
        });
    }

    //***********************************************************
    private void gotoEmergencyContacts()
    //***********************************************************
    {
        val contactTitle = mBinding.emergencyContactName.getText().toString();

        mBinding.emergencyContacts.setOnClickListener(view -> {
            Laila.instance().setMContactType(Constants.Emergency);
            Intent intent = new Intent(ContactTypesHomeActivity.this, ContactsActivity.class);
            intent.putExtra(Constants.CONTACT_TITLE, contactTitle);
            startActivity(intent);
        });
    }

    //***********************************************************
    private void gotoPharmacyActivity()
    //***********************************************************
    {
        val contactTitle = mBinding.pharmacyText.getText().toString();

        mBinding.pharmacy.setOnClickListener(view -> {
            Laila.instance().setMContactType(Constants.Pharmacy_Contacts);
            Intent intent = new Intent(ContactTypesHomeActivity.this, ContactsActivity.class);
            intent.putExtra(Constants.CONTACT_TITLE, contactTitle);
            startActivity(intent);
        });
    }

    //***********************************************************
    @Override
    protected boolean showStatusBar()
    //***********************************************************
    {
        return false;
    }
}