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

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.databinding.FragmentProfileThreeBinding;

import lombok.val;


//***********************************************************
public class ProfileThreeFragment extends BaseFragment
//***********************************************************
{

    private FragmentProfileThreeBinding mBinding;
    private View mRootView;

    //***********************************************************
    public ProfileThreeFragment()
    //***********************************************************
    {
        // Required empty public constructor
    }

    //**************************************************************************************************
    @Override
    public View onCreateViewBaseFragment(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    //**************************************************************************************************
    {
        if (mRootView == null) {
            mBinding = FragmentProfileThreeBinding.inflate(inflater, parent, false);
            mRootView = mBinding.getRoot();
        }
        initControls();

        return mRootView;
    }

    //***********************************************************
    private void initControls()
    //***********************************************************
    {
        editTextWatcher();
    }

    //******************************************************
    private void edit()
    //******************************************************
    {
        mBinding.height.requestFocus();
        mBinding.healthCardNo.setEnabled(true);
        mBinding.insuranceName.setEnabled(true);
        mBinding.insuranceNumber.setEnabled(true);
        mBinding.height.setEnabled(true);
        mBinding.weight.setEnabled(true);
        mBinding.weightUnit.setEnabled(true);
        mBinding.heightUnit.setEnabled(true);
    }

    //******************************************************
    private void editTextWatcher()
    //******************************************************
    {
        val mUser = Laila.instance().getMProfileRequest().getProfile();

        mBinding.healthCardNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mUser.setHealthCardNumber(s.toString());
            }
        });
        mBinding.insuranceName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mUser.setPrivateInsurance(s.toString());
            }
        });
        mBinding.insuranceNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mUser.setPrivateInsuranceNumber(s.toString());
            }
        });
        mBinding.height.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    mUser.setHeight(0);
                    return;
                }
                mUser.setHeight(Double.parseDouble(s.toString()));
            }
        });
        mBinding.weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    mUser.setWeight(0);
                    return;
                }
                mUser.setWeight(Double.parseDouble(s.toString()));
            }
        });
        mBinding.weightUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mUser.setWeight_unit(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mBinding.heightUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mUser.setHeight_unit(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    //*******************************************************************
    @Override
    public void onResume()
    //*******************************************************************
    {
        super.onResume();
        val check = Laila.instance().Edit_Profile;
        if (!check) {
            mBinding.weightUnit.setEnabled(false);
            mBinding.heightUnit.setEnabled(false);
        }
        if (Laila.instance().Edit_Profile)
            edit();
        if (Laila.instance().getMProfileRequest() == null || Laila.instance().getMProfileRequest().getProfile() == null)
            return;
        val userDetail = Laila.instance().getMProfileRequest().getProfile();

        mBinding.healthCardNo.setText(userDetail.getHealthCardNumber());
        mBinding.insuranceName.setText(userDetail.getPrivateInsurance());
        mBinding.insuranceNumber.setText(userDetail.getPrivateInsuranceNumber());
        setData();
    }

    //*************************************************************
    private void setData()
    //*************************************************************
    {
        val user = Laila.instance().getCurrentUserProfile();
        if (user.getHeight() != 0) {
            String userHeight = String.valueOf(user.getHeight());
            if (!TextUtils.isEmpty(userHeight))
                mBinding.height.setText(userHeight);
            if (!TextUtils.isEmpty(user.getHeight_unit()))
                setDropDownItems(user.getHeight_unit(), mBinding.heightUnit, R.array.height_unit);
        }

        if (user.getWeight() != 0) {
            String userWeight = String.valueOf(user.getWeight());
            if (!TextUtils.isEmpty(userWeight))
                mBinding.weight.setText(userWeight);
            if (!TextUtils.isEmpty(user.getWeight_unit()))
                setDropDownItems(user.getWeight_unit(), mBinding.weightUnit, R.array.weight_unit);
        }

    }


    //*************************************************************
    private void setDropDownItems(String type, Spinner view, int item)
    //*************************************************************
    {
        ArrayAdapter<CharSequence> strengthUnitAdapter = ArrayAdapter.createFromResource(getActivity(), item, android.R.layout.simple_spinner_dropdown_item);
        strengthUnitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        view.setAdapter(strengthUnitAdapter);

        if (type != null) {
            int spinnerPosition = strengthUnitAdapter.getPosition(type);
            view.setSelection(spinnerPosition);
        }
    }


}