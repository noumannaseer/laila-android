package com.fantechlabs.lailaa.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.databinding.ActivityAddContactsBinding;
import com.fantechlabs.lailaa.models.Contact;
import com.fantechlabs.lailaa.models.response_models.PharmacyResponse;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;
import com.fantechlabs.lailaa.utils.SharedPreferencesUtils;
import com.fantechlabs.lailaa.view_models.AddPharmacyViewModel;

import java.util.ArrayList;

import lombok.Setter;
import lombok.val;

//*********************************************************
public class AddContactsActivity extends BaseActivity
        implements AddPharmacyViewModel.AddPharmacyListener
//*********************************************************
{
    private ActivityAddContactsBinding mBinding;
    private AddPharmacyViewModel mAddPharmacyViewModel;
    @Setter
    Contact mContact;
    private String mPhone, mName;
    private boolean mUpdateContact;
    private int mUpdateContactId, mContactId, mItemPosition;
    private int id;
    private ArrayList<Contact> mFilterContactList;

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
        mAddPharmacyViewModel = new AddPharmacyViewModel(this);
        mFilterContactList = new ArrayList<>();

        mBinding.toolbarText.setText(Laila.instance().getMContactType());
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
            mPhone = "1" + mPhone;
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
                .getMUser()
                .getContacts() == null || Laila.instance()
                .getMUser()
                .getContacts()
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

        val addPharmacyRequest = Laila.instance()
                .getMAddPharmacyRequest()
                .Builder();

        val contactType = Laila.instance().getMContactType();
        addPharmacyRequest.setFirst_name(mName);
        addPharmacyRequest.setPhone(mPhone);
        addPharmacyRequest.setContact_type(contactType);
        addPharmacyRequest.setUser_private_code(Laila.instance().getMUser().getProfile()
                .getUserPrivateCode());

        if (mUpdateContact) {
            if (Laila.instance()
                    .getMUser()
                    .getContacts() == null || Laila.instance()
                    .getMUser()
                    .getContacts()
                    .size() == 0)
                return;

            val contacts = Laila.instance().getMUser().getContacts();
            int pos = 0;
            for (Contact contact : contacts) {
                pos = pos + 1;
                if (contact.getId() == mContactId) {
                    val contactId = Laila.instance()
                            .getMUser()
                            .getContacts()
                            .get(pos - 1);
                    mUpdateContactId = contactId.getId();
                    addPharmacyRequest.setId(mUpdateContactId);
                    break;
                }
            }
        }
        Laila.instance().setMAddPharmacyRequest(addPharmacyRequest);
        showLoadingDialog();
        mAddPharmacyViewModel.addPharmacy(addPharmacyRequest);
    }

    //*************************************************************
    @Override
    public void onPharmacySuccessfullyAdded(@Nullable PharmacyResponse response)
    //*************************************************************
    {
        if (response.getContact() == null) {
            hideLoadingDialog();
            return;
        }
        setData(response.getContact());
        AndroidUtil.displayAlertDialog(
                AndroidUtil.getString(R.string.contact_added),
                AndroidUtil.getString(
                        R.string.contacts),
                this,
                AndroidUtil.getString(
                        R.string.ok),
                AndroidUtil.getString(
                        R.string.cancel),
                (dialog, which) -> {
                    if (which == -1) {
                        hideLoadingDialog();
                        gotoContactScreen();
                        return;
                    }
                    hideLoadingDialog();
                });
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
        val userDetails = Laila.instance().getMUser();
        if (userDetails == null)
            return;

        if (userDetails.getContacts() != null && userDetails.getContacts().size() != 0)
            for (int i = 0; i < userDetails.getContacts().size(); i++) {
                if (contact.getId().equals(userDetails.getContacts().get(i).getId())) {
                    userDetails.getContacts().set(i, contact);
                    return;
                }
            }
        if (userDetails.getContacts() == null)
            Laila.instance().getMUser().setContacts(new ArrayList<>());
        Laila.instance().getMUser().getContacts().add(0, contact);
        SharedPreferencesUtils.setValue(Constants.USER_DATA, Laila.instance()
                .getMUser());
    }

    //*************************************************************
    @Override
    public void onPharmacyFailedToAdded(@NonNull String errorMessage)
    //*************************************************************
    {
        hideLoadingDialog();
        AndroidUtil.displayAlertDialog(errorMessage, AndroidUtil.getString(R.string.error), this);
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
}