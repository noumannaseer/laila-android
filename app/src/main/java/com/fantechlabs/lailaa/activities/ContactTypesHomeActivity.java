package com.fantechlabs.lailaa.activities;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.databinding.ActivityContactTypesHomeBinding;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;

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
        val familyDoctorContact = mBinding.familyDoctorText.getText().toString();

        mBinding.addFamilyDoctor.setOnClickListener(view -> {
            Laila.instance().setMContactType(familyDoctorContact);
            Intent intent = new Intent(ContactTypesHomeActivity.this, ContactsActivity.class);
            startActivity(intent);
        });
    }

    //***********************************************************
    private void gotoSpecialistContact()
    //***********************************************************
    {
        val specialistContact = mBinding.specialistText.getText().toString();

        mBinding.specialistDoctor.setOnClickListener(view -> {
            Laila.instance().setMContactType(specialistContact);
            Intent intent = new Intent(ContactTypesHomeActivity.this, ContactsActivity.class);
            startActivity(intent);
        });
    }

    //***********************************************************
    private void gotoFamilyContact()
    //***********************************************************
    {
        val familyContact = mBinding.familyContactText.getText().toString();

        mBinding.familyContacts.setOnClickListener(view -> {
            Laila.instance().setMContactType(familyContact);
            Intent intent = new Intent(ContactTypesHomeActivity.this, ContactsActivity.class);
            startActivity(intent);
        });
    }

    //***********************************************************
    private void gotoFriendsContact()
    //***********************************************************
    {
        val friendsContact = mBinding.friendsContactText.getText().toString();

        mBinding.friendsContacts.setOnClickListener(view -> {
            Laila.instance().setMContactType(friendsContact);
            Intent intent = new Intent(ContactTypesHomeActivity.this, ContactsActivity.class);
            startActivity(intent);
        });
    }

    //***********************************************************
    private void gotoCareGiverContacts()
    //***********************************************************
    {
        val caregiver = mBinding.careGiverText.getText().toString();

        mBinding.careGiver.setOnClickListener(view -> {
            Laila.instance().setMContactType(caregiver);
            Intent intent = new Intent(ContactTypesHomeActivity.this, ContactsActivity.class);
            startActivity(intent);
        });
    }

    //***********************************************************
    private void gotoEmergencyContacts()
    //***********************************************************
    {
        val emergencyContact = mBinding.emergencyContactName.getText().toString();


        mBinding.emergencyContacts.setOnClickListener(view -> {
            Laila.instance().setMContactType(emergencyContact);
            Intent intent = new Intent(ContactTypesHomeActivity.this, ContactsActivity.class);
            startActivity(intent);
        });
    }

    //***********************************************************
    private void gotoPharmacyActivity()
    //***********************************************************
    {
        val pharmacy = mBinding.pharmacyText.getText().toString();

        mBinding.pharmacy.setOnClickListener(view -> {
            Laila.instance().setMContactType(pharmacy);
            Intent intent = new Intent(ContactTypesHomeActivity.this, ContactsActivity.class);
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