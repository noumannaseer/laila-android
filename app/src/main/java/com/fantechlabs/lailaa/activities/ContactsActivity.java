package com.fantechlabs.lailaa.activities;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;

import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.adapter.CareTakerListAdapter;
import com.fantechlabs.lailaa.databinding.ActivityContactsBinding;
import com.fantechlabs.lailaa.models.Contact;
import com.fantechlabs.lailaa.utils.Constants;

import java.util.ArrayList;

import lombok.val;

import static com.fantechlabs.lailaa.utils.Constants.PHARMACY;

//*************************************************************
public class ContactsActivity extends BaseActivity
        //*************************************************************
{
    private ActivityContactsBinding mBinding;
    private CareTakerListAdapter mCareTakerListAdapter;
    private ArrayList<Contact> mCareTakerList;

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
        mBinding.toolbarText.setText(Laila.instance().getMContactType());
        mBinding.addContact.setOnClickListener(view -> {
            if (!Laila.instance().getMContactType().equals(PHARMACY)) {
                startActivity(new Intent(getApplicationContext(), AddContactsActivity.class));
                return;
            }
            Intent intent = new Intent(ContactsActivity.this, AddPharmacyActivity.class);
            startActivity(intent);
        });
    }

    //*********************************************************************
    private void getCareTakerList()
    //*********************************************************************
    {
        if (Laila.instance().getMUser() == null ||
                Laila.instance().getMUser().getContacts() == null)
            return;
        val userDetails = Laila.instance().getMUser().getContacts();

        val contactTypes = Laila.instance().getMContactType();

        mCareTakerList = new ArrayList<>();
        for (Contact contact : userDetails) {
            val userPrivateCode = contact.getUserPrivateCode();
            val contactType = contact.getContactType();
            if (userPrivateCode
                    .equals(Laila.instance().getMUser().getProfile().getUserPrivateCode()) &&
                    contactType.equals(contactTypes)) {
                mCareTakerList.add(contact);
            }
        }
        startRecyclerView();
    }

    //**************************************************************
    private void startRecyclerView()
    //**************************************************************
    {
        if (mCareTakerList == null || mCareTakerList.size() == 0)
            return;

        mCareTakerListAdapter = new CareTakerListAdapter(mCareTakerList, new CareTakerListAdapter.ListClickListener() {
            //**************************************************************
            @Override
            public void onUpdate(int position)
            //**************************************************************
            {
                Laila.instance().setMContactPosition(position);
                Laila.instance().on_update_contact = true;
                Intent intent = new Intent(ContactsActivity.this, AddContactsActivity.class);
                intent.putParcelableArrayListExtra(Constants.CONTACT_LIST, mCareTakerList);
                startActivity(intent);
            }
        }, this);
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerview.setAdapter(mCareTakerListAdapter);
    }

    //**************************************************************
    @Override
    protected void onResume()
    //**************************************************************
    {
        super.onResume();

        getCareTakerList();
        if (mCareTakerList == null || mCareTakerList.size() == 0) {
            mBinding.noRecord.setVisibility(View.VISIBLE);
            mBinding.recyclerview.setVisibility(View.GONE);
            return;
        }
        mBinding.noRecord.setVisibility(View.GONE);
        mBinding.recyclerview.setVisibility(View.VISIBLE);
    }

    //*************************************************************
    @Override
    protected boolean showStatusBar()
    //*************************************************************
    {
        return false;
    }
}