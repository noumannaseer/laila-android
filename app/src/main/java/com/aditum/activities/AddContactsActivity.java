package com.aditum.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.aditum.Laila;
import com.aditum.R;
import com.aditum.databinding.ActivityAddContactsBinding;
import com.aditum.models.updates.models.Contact;
import com.aditum.models.updates.request_models.EmergencyContactRequest;
import com.aditum.models.updates.response_models.EmergencyContactResponse;
import com.aditum.utils.AndroidUtil;
import com.aditum.utils.Constants;
import com.aditum.utils.SharedPreferencesUtils;
import com.aditum.view_models.EmergencyContactViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import lombok.Setter;
import lombok.val;

import static com.aditum.utils.Constants.CONTACT;

//*********************************************************
public class AddContactsActivity extends BaseActivity
        implements EmergencyContactViewModel.EmergencyContactListener
//*********************************************************
{
    private ActivityAddContactsBinding mBinding;
    @Setter
    Contact mContact;
    private String mPhone, mName;
    private boolean mUpdateContact;
    private int mUpdateContactId, mContactId, mItemPosition;
    private int id;
    private ArrayList<Contact> mFilterContactList;
    private EmergencyContactViewModel mEmergencyContactViewModel;
    private EmergencyContactRequest mEmergencyContactRequest;

    //*********************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //*********************************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_contacts);
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("");
        intiControls();
    }

    //***********************************************************
    @SuppressLint("SetTextI18n")
    private void intiControls()
    //***********************************************************
    {
        initViews();
        saveContacts();
    }

    //*************************************************************
    private void initViews()
    //*************************************************************
    {
        mFilterContactList = new ArrayList<>();
        mEmergencyContactViewModel = new EmergencyContactViewModel(this);
        mBinding.toolbarText.setText(Laila.instance().getMContactType());
    }

    //*************************************************************
    private void saveContacts()
    //*************************************************************
    {
        mBinding.save.setOnClickListener(v ->
        {
            mName = mBinding.contactName.getText().toString();
            mPhone = mBinding.contactNo.getText().toString();

            if (TextUtils.isEmpty(mName)) {
                mBinding.contactName.setError(AndroidUtil.getString(R.string.required));
                mBinding.contactName.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(mPhone)) {
                mBinding.contactNo.setError(AndroidUtil.getString(R.string.phone_required));
                mBinding.contactNo.requestFocus();
                return;
            }
            if (!TextUtils.isDigitsOnly(mPhone)) {
                mBinding.contactNo.setError(AndroidUtil.getString(R.string.phone_digit));
                mBinding.contactNo.requestFocus();
                return;
            }
            if (mPhone.length() != 10) {
                mBinding.contactNo.setError(AndroidUtil.getString(R.string.phone_check));
                mBinding.contactNo.requestFocus();
                return;
            }
//            mPhone = "1" + mPhone;
            saveCareTaker();
        });
    }

    //*************************************************************
    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume()
    //*************************************************************
    {
        super.onResume();
        getParcelable();
        val onUpdateContact = Laila.instance().on_update_contact;
        if (onUpdateContact) {
            mBinding.contactTypeTitle.setText(AndroidUtil.getString(R.string.update) + " " + Laila.instance().getMContactType());
            onUpdate();
            return;
        }
        mBinding.contactTypeTitle.setText(AndroidUtil.getString(R.string.add) + " " + Laila.instance().getMContactType());
    }

    //*************************************************************
    private void onUpdate()
    //*************************************************************
    {
        mUpdateContact = Laila.instance().on_update_contact;
        mItemPosition = Laila.instance()
                .getMContactPosition();

        if (Laila.instance()
                .getMUser_U()
                .getData()
                .getContactList() == null || Laila.instance()
                .getMUser_U()
                .getData()
                .getContactList()
                .size() == 0)
            return;

        val filterContact = mFilterContactList.get(mItemPosition);

        mContactId = filterContact.getId();

        Laila.instance().setMUpdateContact(mFilterContactList.get(Laila.instance().getMContactPosition()));

        if (mUpdateContact) {
            mBinding.save.setText(R.string.update);
            mBinding.save.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, R.color.button_background));

            val contact = Laila.instance().getMUpdateContact();
            if (contact == null)
                return;

            mName = contact.getFirstName();
            mPhone = contact.getPhone();
            Laila.instance().setMContactType(contact.getContactType());

            mBinding.contactName.setText(mName);
            mBinding.contactNo.setText(mPhone);

        }
    }

    //*************************************************************
    private void saveCareTaker()
    //*************************************************************
    {
        val user_token = Laila.instance().getMUser_U().getData().getUser().getToken();
        val user_id = Laila.instance().getMUser_U().getData().getUser().getId().toString();

        mEmergencyContactRequest = Laila.instance()
                .getMEmergencyContactRequest()
                .Builder();

        mEmergencyContactRequest.setFirstName(mName);
        mEmergencyContactRequest.setPhone(mPhone);
        mEmergencyContactRequest.setContactType(CONTACT);
        mEmergencyContactRequest.setUserId(user_id);
        mEmergencyContactRequest.setToken(user_token);

        showLoadingDialog();
        if (id != 0) {
            Laila.instance().on_update_contact = true;
            mEmergencyContactRequest.setContactId(String.valueOf(id));
            mEmergencyContactViewModel.updateEmergencyContact(mEmergencyContactRequest, id);
            return;
        }
        mEmergencyContactViewModel.createEmergencyContact(mEmergencyContactRequest);
    }

    //**********************************************♪
    private void gotoContactScreen()
    //**********************************************
    {
        Intent intent = new Intent(getApplicationContext(), ContactsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    //**********************************************
    private void setData(@Nullable Contact contact)
    //**********************************************
    {
        val userDetails = Laila.instance().getMUser_U();
        if (userDetails == null)
            return;
        val contactList = userDetails.getData().getContactList();

        if (contactList == null)
            userDetails.getData().setContactList(new ArrayList<>());
        userDetails.getData().getContactList().add(contact);
        SharedPreferencesUtils.setValue(Constants.USER_DATA, Laila.instance()
                .getMUser_U());
    }

    //*********************************************************************
    private void getParcelable()
    //*********************************************************************
    {
        Bundle args = getIntent().getExtras();
        if (args != null) {
            mFilterContactList = args.getParcelableArrayList(Constants.CONTACT_LIST);
        }
    }

    //******************************************
    @Override
    public void onBackPressed()
    //******************************************
    {
        super.onBackPressed();
        Laila.instance().on_update_contact = false;

    }

    //*********************************************************
    @Override
    protected boolean showStatusBar()
    //*********************************************************d
    {
        return false;
    }

    //*********************************************************d
    @Override
    public void onSuccessfullyCreateContact(@Nullable @org.jetbrains.annotations.Nullable EmergencyContactResponse response)
    //*********************************************************d
    {
        hideLoadingDialog();
        if (response.getData().getContact() == null)
            return;

        setData(response.getData().getContact());

        AndroidUtil.displayAlertDialog(
                AndroidUtil.getString(R.string.emergency_contact_added),
                AndroidUtil.getString(
                        R.string.emergency_contact),
                this,
                AndroidUtil.getString(
                        R.string.ok),
                (dialog, which) -> {
                    if (which == -1) {
                        hideLoadingDialog();
                        gotoContactScreen();
                        return;
                    }
                    hideLoadingDialog();
                });
    }

    //*********************************************************d
    @Override
    public void onSuccessfullyGetContacts(@Nullable @org.jetbrains.annotations.Nullable EmergencyContactResponse response)
    //*********************************************************d
    {

    }

    //*********************************************************d
    @Override
    public void onFailed(@NonNull @NotNull String errorMessage)
    //*********************************************************d
    {
        hideLoadingDialog();
        AndroidUtil.displayAlertDialog(errorMessage, AndroidUtil.getString(R.string.alert), this);
    }
}