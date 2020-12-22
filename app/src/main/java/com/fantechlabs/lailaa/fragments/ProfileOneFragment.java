package com.fantechlabs.lailaa.fragments;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.databinding.FragmentProfileOneBinding;
import com.fantechlabs.lailaa.models.Profile;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.DateUtils;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;

import java.util.ArrayList;
import java.util.Date;

import lombok.val;


//***********************************************************
public class ProfileOneFragment extends BaseFragment
//***********************************************************
{
    private FragmentProfileOneBinding mBinding;
    private View mRootView;
    private Date mSelectedDate;
    private Profile mUser;
    private ArrayList<String> mLocalLanguages=new ArrayList<String>();

    //***********************************************************
    public ProfileOneFragment()
    //***********************************************************
    {
        // Required empty public constructor
    }

    //*******************************************************************************************************
    @NonNull
    @Override
    public View onCreateViewBaseFragment(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    //*******************************************************************************************************
    {
        if (mRootView == null)
        {
            mBinding = FragmentProfileOneBinding.inflate(inflater, parent, false);
            mRootView = mBinding.getRoot();
        }
        initControls();
        return mRootView;
    }

    //******************************************************
    private void initControls()
    //******************************************************
    {
        editTextWatcher();
        mBinding.dob.setOnClickListener(v -> datePicker());
        if (Laila.instance().Edit_Profile)
            edit();
    }

    //******************************************************
    private void editTextWatcher()
    //******************************************************
    {
        mUser = Laila.instance().getMProfileRequest().getProfile();
        val check = Laila.instance().Edit_Profile;

        mUser.setUserPrivateCode(Laila.instance().getMUser().getProfile().getUserPrivateCode());
        mUser.setUserType(1);
        mUser.setFavMusic("music");
        mUser.setIsNotifications(1);
        mUser.setIsAudio(1);

        mBinding.gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                mUser.setGender(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        mBinding.bloodGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mUser.setBloodType(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mBinding.language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mUser.setPrefLang(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mBinding.firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mUser.setFirstName(s.toString());
            }
        });
        mBinding.lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mUser.setLastName(s.toString());
            }
        });
        mBinding.dob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mUser.setDateOfBirth(s.toString());
            }
        });

        mBinding.organYes.setOnClickListener(v-> organYes());
        mBinding.organNo.setOnClickListener(v->  organNo());
    }

    //******************************************************
    private void organYes()
    //******************************************************
    {
        mBinding.organNo.setBackgroundTintList(
                ContextCompat.getColorStateList(getActivity(), R.color.darkBlue));
        mBinding.organYes.setBackgroundTintList(
                ContextCompat.getColorStateList(getActivity(), R.color.green));
        mBinding.organYes.setTextColor(AndroidUtil.getColor(R.color.green));
        mBinding.organNo.setTextColor(AndroidUtil.getColor(R.color.darkBlue));
        mUser.setOrganDonor("Y");
    }

    //******************************************************
    private void organNo()
    //******************************************************
    {
        mBinding.organYes.setBackgroundTintList(
                ContextCompat.getColorStateList(getActivity(), R.color.darkBlue));
        mBinding.organNo.setBackgroundTintList(
                ContextCompat.getColorStateList(getActivity(), R.color.green));
        mBinding.organYes.setTextColor(AndroidUtil.getColor(R.color.darkBlue));
        mBinding.organNo.setTextColor(AndroidUtil.getColor(R.color.green));
        mUser.setOrganDonor("N");
    }

    //******************************************************
    private void edit()
    //******************************************************
    {
        mBinding.firstName.requestFocus();
        mBinding.firstName.setEnabled(true);
        mBinding.lastName.setEnabled(true);
        mBinding.language.setEnabled(true);
        mBinding.organYes.setEnabled(true);
        mBinding.organNo.setEnabled(true);
        mBinding.dob.setEnabled(true);
        mBinding.bloodGroup.setEnabled(true);
        mBinding.gender.setEnabled(true);
        mBinding.language.setEnabled(true);
    }

    //*******************************************************************
    @Override
    public void onResume()
    //*******************************************************************
    {
        super.onResume();
        mLocalLanguages =  AndroidUtil.getLanguagesList();
        setLanguages(mBinding.language, mLocalLanguages);
        if (Laila.instance().getMProfileRequest() == null || Laila.instance().getMProfileRequest().getProfile() == null)
            return;
        val userDetail = Laila.instance().getMProfileRequest().getProfile();
        val check = Laila.instance().Edit_Profile;
        if (!check)
        {
            mBinding.bloodGroup.setEnabled(false);
            mBinding.gender.setEnabled(false);
            mBinding.language.setEnabled(false);
        }
        mBinding.firstName.setText(userDetail.getFirstName());
        mBinding.lastName.setText(userDetail.getLastName());
        mBinding.email.setText(userDetail.getEmail());
        mBinding.dob.setText(userDetail.getDateOfBirth());
        dropDownItems(mBinding.gender, R.array.gender, userDetail.getGender());
        setLanguagesDropDownItems(mBinding.language, mLocalLanguages, userDetail.getPrefLang());
        dropDownItems(mBinding.bloodGroup, R.array.blood, userDetail.getBloodType());

        val checkOrgan = TextUtils.equals(userDetail.getOrganDonor(), "Y");

        if (checkOrgan)
        {
            organYes();
            return;
        }
            organNo();

    }

    //*********************************************************************************************
    private void setLanguages(Spinner spinner, ArrayList<String> languages)
    //*********************************************************************************************
    {
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, languages);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(countryAdapter);
    }

    //*******************************************
    private void datePicker()
    //*******************************************
    {
        new SingleDateAndTimePickerDialog.Builder(getActivity())
                .bottomSheet()
                .curved()
                .displayMinutes(false)
                .displayHours(false)
                .displayDays(false)
                .displayMonth(true)
                .displayYears(true)
                .displayDaysOfMonth(true)
                .listener(date -> {
                    mSelectedDate = date;
                    mBinding.dob.setText(
                            DateUtils.getDate(mSelectedDate.getTime(),
                                    "dd-MMM-yyyy"));
                })
                .display();
    }

    //*************************************************************
    private void dropDownItems(Spinner spinner, int array, String type)
    //*************************************************************
    {
        ArrayAdapter<CharSequence> strengthUnitAdapter = ArrayAdapter.createFromResource(getActivity(), array, android.R.layout.simple_spinner_dropdown_item);
        strengthUnitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(strengthUnitAdapter);

        if (type == null || type.length() == 0)
            return;

        if (type != null)
        {
            int spinnerPosition = strengthUnitAdapter.getPosition(type);
            spinner.setSelection(spinnerPosition);
        }
    }

    //*************************************************************
    private void setLanguagesDropDownItems(Spinner spinner, ArrayList<String> itemList, String name)
    //*************************************************************
    {
        ArrayAdapter<String> countryListAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, itemList);
        countryListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(countryListAdapter);

        int spinnerPosition = countryListAdapter.getPosition(name);
        spinner.setSelection(spinnerPosition);
    }
}
