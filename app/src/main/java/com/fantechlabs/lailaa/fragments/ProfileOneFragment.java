package com.fantechlabs.lailaa.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.VirtualLayout;
import androidx.core.content.ContextCompat;

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.databinding.FragmentProfileOneBinding;
import com.fantechlabs.lailaa.databinding.LayoutAlergyConditionDialogBindingImpl;
import com.fantechlabs.lailaa.models.updates.models.Profile;
import com.fantechlabs.lailaa.models.updates.request_models.ProfileRequest;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.DateUtils;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import lombok.val;
import rx.internal.util.LinkedArrayList;


//***********************************************************
public class ProfileOneFragment extends BaseFragment
//***********************************************************
{
    private FragmentProfileOneBinding mBinding;
    private View mRootView;
    private Date mSelectedDate;
    private ProfileRequest mProfileRequest;
    private ArrayList<String> mLocalLanguages = new ArrayList<String>();
    private Profile mProfile;

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
        if (mRootView == null) {
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
        setUserIdAndToken();
        editTextWatcher();
        setDob();
        edit();
    }

    //******************************************************
    private void setDob()
    //******************************************************
    {
        val defaultDob = DateUtils.getCurrentDate("dd-MMM-yyyy");
        mBinding.dob.setText(defaultDob);
        mBinding.dob.setOnClickListener(v -> datePicker());
    }

    //******************************************************
    private void editTextWatcher()
    //******************************************************
    {
        mBinding.gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mProfileRequest.setGender(parent.getSelectedItem().toString());
                Laila.instance().setMProfileRequest(mProfileRequest);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mBinding.bloodGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mProfileRequest.setBloodType(parent.getSelectedItem().toString());
                Laila.instance().setMProfileRequest(mProfileRequest);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mBinding.language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mProfileRequest.setPrefLang(parent.getSelectedItem().toString());
                Laila.instance().setMProfileRequest(mProfileRequest);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mBinding.firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Laila.instance().is_edit_profile_fields = true;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mProfileRequest.setFirstName(s.toString());
                Laila.instance().setMProfileRequest(mProfileRequest);
            }
        });
        mBinding.lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Laila.instance().is_edit_profile_fields = true;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mProfileRequest.setLastName(s.toString());
                Laila.instance().setMProfileRequest(mProfileRequest);
            }
        });
        mBinding.dob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Laila.instance().is_edit_profile_fields = true;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mProfileRequest.setDateOfBirth(s.toString());
                Laila.instance().setMProfileRequest(mProfileRequest);
            }
        });

        mBinding.organDonar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mProfileRequest.setOrganDonor(adapterView.getSelectedItem().toString());
                Laila.instance().setMProfileRequest(mProfileRequest);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    //******************************************************
    private void setUserIdAndToken()
    //******************************************************
    {
        val user = Laila.instance().getMUser_U().getData().getUser();
        val user_id = user.getId();
        val user_token = user.getToken();

        mProfileRequest = Laila.instance().getMProfileRequest();
        if (mProfileRequest == null)
            mProfileRequest = new ProfileRequest();
        mProfileRequest.setUserId(user_id);
        mProfileRequest.setToken(user_token);
        Laila.instance().setMProfileRequest(mProfileRequest);

    }

    //******************************************************
    private void edit()
    //******************************************************
    {
        mBinding.firstName.requestFocus();
        mBinding.firstName.setEnabled(true);
        mBinding.lastName.setEnabled(true);
        mBinding.language.setEnabled(true);
        mBinding.organDonar.setEnabled(true);
        mBinding.email.setEnabled(false);
        mBinding.dob.setEnabled(true);
        mBinding.bloodGroup.setEnabled(true);
        mBinding.gender.setEnabled(true);
    }

    //*******************************************************************
    @Override
    public void onResume()
    //*******************************************************************
    {
        super.onResume();
        setUserLanguage();
        setData();
    }

    //*****************************************
    @SuppressLint("SetTextI18n")
    private void setData()
    //*****************************************
    {
        mProfile = Laila.instance().getMUser_U().getData().getProfile();
        if (mProfile == null)
            mProfile = new Profile();

        mProfileRequest = Laila.instance().getMProfileRequest();
        if (mProfileRequest.getHeight() != null || mProfileRequest.getWeight() != null) {
            requestProfile();
            return;
        }
        getProfileDate();
    }

    //*****************************************
    private void requestProfile()
    //*****************************************
    {
        val user_email = Laila.instance().getMUser_U().getData().getUser().getEmail();
        mBinding.firstName.setText(mProfileRequest.getFirstName());
        mBinding.lastName.setText(mProfileRequest.getLastName());
        mBinding.email.setText(user_email);

        setDropDownItemsWithRequest(mProfileRequest);
    }

    //*****************************************
    private void getProfileDate()
    //*****************************************
    {
        val user_email = Laila.instance().getMUser_U().getData().getUser().getEmail();
        mBinding.firstName.setText(mProfile.getFirstName());
        mBinding.lastName.setText(mProfile.getLastName());
        mBinding.email.setText(user_email);


        if (mProfile.getDateOfBirth() != 0) {
            val dob = DateUtils.getGivenDate(mProfile.getDateOfBirth(), "dd-MMM-yyyy");
            mBinding.dob.setText(dob);
        }
        setDropDownItemsWithProfile(mProfile);
        getOrganDonars();
    }

    //*****************************************
    private void getOrganDonars()
    //*****************************************
    {
        if (mProfile.getOrganDonor().equals("Y")) {
//            organYes();
            return;
        }
//        organNo();
    }


    //*****************************************
    private void setUserLanguage()
    //*****************************************
    {
        mLocalLanguages = AndroidUtil.getLanguagesList();
        setLanguages(mBinding.language, mLocalLanguages);
    }

    //*****************************************
    private void setDropDownItems(Profile profile)
    //*****************************************
    {
        dropDownItems(mBinding.gender, R.array.gender, profile.getGender());
        setLanguagesDropDownItems(mBinding.language, mLocalLanguages, profile.getPrefLang());
        dropDownItems(mBinding.bloodGroup, R.array.blood, profile.getBloodType());
        dropDownItems(mBinding.organDonar, R.array.organ_donar, profile.getOrganDonor());
    }

    //*****************************************
    private void setLanguages(Spinner spinner, ArrayList<String> languages)
    //*****************************************
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

    //*****************************************
    private void setDropDownItemsWithProfile(Profile profile)
    //*****************************************
    {
        dropDownItems(mBinding.gender, R.array.gender, profile.getGender());
        if (profile.getFirstName() == null)
            return;
        if (profile.getFirstName().isEmpty())
            setLanguagesDropDownItems(mBinding.language, mLocalLanguages, AndroidUtil.getCurrentDeviceLanguage());
        else
            setLanguagesDropDownItems(mBinding.language, mLocalLanguages, profile.getPrefLang());
        dropDownItems(mBinding.bloodGroup, R.array.blood, profile.getBloodType());
    }

    //*****************************************
    private void setDropDownItemsWithRequest(ProfileRequest profileRequest)
    //*****************************************
    {
        dropDownItems(mBinding.gender, R.array.gender, profileRequest.getGender());
        setLanguagesDropDownItems(mBinding.language, mLocalLanguages, profileRequest.getPrefLang());
        dropDownItems(mBinding.bloodGroup, R.array.blood, profileRequest.getBloodType());
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

        if (type != null) {
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
