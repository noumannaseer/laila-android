package com.fantechlabs.lailaa.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.adapter.CareTakerListAdapter;
import com.fantechlabs.lailaa.databinding.ActivityContactsBinding;
import com.fantechlabs.lailaa.models.updates.models.Contact;
import com.fantechlabs.lailaa.models.updates.response_models.EmergencyContactResponse;
import com.fantechlabs.lailaa.utils.Constants;
import com.fantechlabs.lailaa.view_models.EmergencyContactViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import lombok.val;

import static com.fantechlabs.lailaa.utils.Constants.PHARMACY;

//*************************************************************
public class ContactsActivity extends BaseActivity implements EmergencyContactViewModel.EmergencyContactListener
        //*************************************************************
{
    private ActivityContactsBinding mBinding;
    private CareTakerListAdapter mCareTakerListAdapter;
    private ArrayList<Contact> mCareTakerList;
    private EmergencyContactViewModel mEmergencyContactViewModel;

    //*************************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //*************************************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_contacts);
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
        initViews();
        getEmergencyContacts();
//        if (mCareTakerList == null || mCareTakerList.size() == 0) {
//            mBinding.noRecord.setVisibility(View.VISIBLE);
//            mBinding.recyclerview.setVisibility(View.GONE);
//            return;
//        }
//        mBinding.noRecord.setVisibility(View.GONE);
//        mBinding.recyclerview.setVisibility(View.VISIBLE);
        addContact();
    }

    //**********************************************************
    private void initViews()
    //**********************************************************
    {
        mBinding.toolbarText.setText(Laila.instance().getMContactType());
        mEmergencyContactViewModel = new EmergencyContactViewModel(this);
    }

    //**********************************************************
    private void addContact()
    //**********************************************************
    {
        mBinding.addContact.setOnClickListener(view -> {
            if (!Laila.instance().getMContactType().equals(PHARMACY)) {
                startActivity(new Intent(getApplicationContext(), AddContactsActivity.class));
                return;
            }
            Intent intent = new Intent(ContactsActivity.this, AddPharmacyActivity.class);
            startActivity(intent);
        });
    }

    //**********************************************************
    private void getEmergencyContacts()
    //**********************************************************
    {
        mCareTakerList = new ArrayList<>();
        if (Laila.instance().getMUser_U().getData() == null)
            return;
        mCareTakerList = (ArrayList<Contact>) Laila.instance().getMUser_U().getData().getContactList();

        if (mCareTakerList == null) {
            showLoadingDialog();
            mEmergencyContactViewModel.getEmergencyContacts();
            return;
        }
        startRecyclerView();
    }

    //*********************************************************************,
    private void getCareTakerList()
    //*********************************************************************
    {
//        if (Laila.instance().getMUser() == null ||
//                Laila.instance().getMUser().getContacts() == null)
//            return;
//        val userDetails = Laila.instance().getMUser().getContacts();
//
//        val contactTypes = Laila.instance().getMContactType();
//
//        mCareTakerList = new ArrayList<>();
//        for (Contact contact : userDetails) {
//            val userPrivateCode = contact.getUserPrivateCode();
//            val contactType = contact.getContactType();
//            if (userPrivateCode
//                    .equals(Laila.instance().getMUser().getProfile().getUserPrivateCode()) &&
//                    contactType.equals(contactTypes)) {
//                mCareTakerList.add(contact);
//            }
//        }
//        startRecyclerView();
    }

    //**************************************************************
    private void startRecyclerView()
    //**************************************************************
    {
        if (mCareTakerList == null || mCareTakerList.size() == 0)
            return;

        mCareTakerListAdapter = new CareTakerListAdapter(mCareTakerList, position -> {
            Laila.instance().setMContactPosition(position);
            Laila.instance().on_update_contact = true;
            Intent intent = new Intent(ContactsActivity.this, AddContactsActivity.class);
            intent.putParcelableArrayListExtra(Constants.CONTACT_LIST, mCareTakerList);
            startActivity(intent);
        }, this);
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerview.setAdapter(mCareTakerListAdapter);
    }

    //*************************************************************
    @Override
    protected boolean showStatusBar()
    //*************************************************************
    {
        return false;
    }

    //***************************************************************
    @Override
    public void onSuccessfullyCreateContact(@Nullable @org.jetbrains.annotations.Nullable EmergencyContactResponse response)
    //***************************************************************
    {
        hideLoadingDialog();
    }

    //***************************************************************
    @Override
    public void onSuccessfullyGetContacts(@Nullable @org.jetbrains.annotations.Nullable EmergencyContactResponse response)
    //***************************************************************
    {
        hideLoadingDialog();
        if (response == null)
            return;
        ArrayList<Contact> contactList = (ArrayList<Contact>) response.getData().getContactList();
        if (contactList == null || contactList.size() == 0)
            return;
        mCareTakerList = contactList;
        Laila.instance().getMUser_U().getData().setContactList(mCareTakerList);
        startRecyclerView();
    }

    //***************************************************************
    @Override
    public void onFailed(@NonNull @NotNull String errorMessage)
    //***************************************************************
    {
        hideLoadingDialog();
    }
}