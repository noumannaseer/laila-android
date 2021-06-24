package com.aditum.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.aditum.Laila;
import com.aditum.R;
import com.aditum.adapter.CareTakerListAdapter;
import com.aditum.databinding.ActivityContactsBinding;
import com.aditum.models.updates.models.Contact;
import com.aditum.models.updates.response_models.EmergencyContactResponse;
import com.aditum.utils.AndroidUtil;
import com.aditum.utils.Constants;
import com.aditum.utils.SharedPreferencesUtils;
import com.aditum.view_models.EmergencyContactViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import lombok.val;

import static com.aditum.utils.Constants.PHARMACY;

//*************************************************************
public class ContactsActivity extends BaseActivity implements EmergencyContactViewModel.EmergencyContactListener
        //*************************************************************
{
    private ActivityContactsBinding mBinding;
    private CareTakerListAdapter mCareTakerListAdapter;
    private ArrayList<Contact> mCareTakerList;
    private ArrayList<Contact> mFilterContactList;
    private EmergencyContactViewModel mEmergencyContactViewModel;
    private String mContactTitle;
    private int mPosition;
    private String mContactId;

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
        getParcelable();
        initViews();
        addContact();
    }

    //**********************************************************
    private void initViews()
    //**********************************************************
    {
        mBinding.toolbarText.setText(mContactTitle);
        mEmergencyContactViewModel = new EmergencyContactViewModel(this);
    }

    //**********************************************************
    private void addContact()
    //**********************************************************
    {
        mBinding.addContact.setOnClickListener(view -> {
            if (!Laila.instance().getMContactType().equals(PHARMACY)) {
                Intent intent = new Intent(getApplicationContext(), AddContactsActivity.class);
                intent.putExtra(Constants.CONTACT_TITLE, mContactTitle);
                startActivity(intent);
                return;
            }
            Intent intent = new Intent(ContactsActivity.this, AddPharmacyActivity.class);
            startActivity(intent);
        });
    }

    //**********************************************************
    @Override
    protected void onResume()
    //**********************************************************
    {
        super.onResume();
        getContacts();
    }

    //**********************************************************
    private void getContacts()
    //**********************************************************
    {
        mCareTakerList = new ArrayList<>();
        mFilterContactList = new ArrayList<>();
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

    //**************************************************************
    private void startRecyclerView()
    //**************************************************************
    {
        if (mCareTakerList == null || mCareTakerList.size() == 0) {
            mBinding.noRecord.setVisibility(View.VISIBLE);
            mBinding.recyclerview.setVisibility(View.GONE);
            return;
        }
        val contactType = Laila.instance().getMContactType();
        for (val contact : mCareTakerList)
            if (contact.getContactType().equals(contactType))
                mFilterContactList.add(contact);

        if (mFilterContactList == null || mFilterContactList.size() == 0) {
            mBinding.noRecord.setVisibility(View.VISIBLE);
            mBinding.recyclerview.setVisibility(View.GONE);
            return;
        }

        mBinding.noRecord.setVisibility(View.GONE);
        mBinding.recyclerview.setVisibility(View.VISIBLE);

        mCareTakerListAdapter = new CareTakerListAdapter(mFilterContactList, new CareTakerListAdapter.ListClickListener() {
            //**************************************************
            @Override
            public void onUpdate(int position)
            //**************************************************
            {
                Laila.instance().setMContactPosition(position);
                Laila.instance().on_update_contact = true;
                Intent intent = new Intent(ContactsActivity.this, AddContactsActivity.class);
                intent.putExtra(Constants.CONTACT_TITLE, mContactTitle);
                intent.putParcelableArrayListExtra(Constants.CONTACT_LIST, mFilterContactList);
                startActivity(intent);
            }

            //**************************************************
            @Override
            public void onDelete(int position, int id)
            //**************************************************
            {
                mPosition = position;
                mContactId = String.valueOf(id);
                AndroidUtil.displayAlertDialog(
                        AndroidUtil.getString(
                                R.string.delete_item),
                        AndroidUtil.getString(
                                R.string.alert),
                        ContactsActivity.this,
                        AndroidUtil.getString(
                                R.string.ok),
                        AndroidUtil.getString(
                                R.string.cancel),
                        (dialog, which) -> {
                            if (which == -1) {
                                showLoadingDialog();
                                mEmergencyContactViewModel.deleteContact(mContactId);
                            }
                        });
            }
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

    //*********************************************************************************************
    @Override
    public void onSuccessfullyDeleteContacts(@Nullable @org.jetbrains.annotations.Nullable EmergencyContactResponse response)
    //*********************************************************************************************
    {
        mFilterContactList = new ArrayList<>();
        mCareTakerList = new ArrayList<>();

        Laila.instance().getMUser_U().getData().getContactList().remove(mPosition);
        SharedPreferencesUtils.setValue(Constants.USER_DATA, Laila.instance().getMUser_U());

        mCareTakerList = (ArrayList<Contact>) Laila.instance().getMUser_U().getData().getContactList();
        startRecyclerView();
        hideLoadingDialog();
    }

    //***************************************************************
    @Override
    public void onFailed(@NonNull @NotNull String errorMessage)
    //***************************************************************
    {
        hideLoadingDialog();
    }

    //*********************************************************************
    private void getParcelable()
    //*********************************************************************
    {
        Bundle args = getIntent().getExtras();
        if (args != null) {
            mContactTitle = args.getString(Constants.CONTACT_TITLE);
        }
    }
}