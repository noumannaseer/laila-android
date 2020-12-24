package com.fantechlabs.lailaa.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.adapter.AllergiesListAdapter;
import com.fantechlabs.lailaa.databinding.FragmentProfileThreeBinding;
import com.fantechlabs.lailaa.utils.AndroidUtil;

import java.util.ArrayList;
import java.util.List;

import lombok.val;


//***********************************************************
public class ProfileThreeFragment extends BaseFragment
//***********************************************************
{

    private FragmentProfileThreeBinding mBinding;
    private View mRootView;
    private List<String> mAllergiesList;
    private List<String> mConditionList;
    private AllergiesListAdapter mAllergiesListAdapter;
    private String mAllergies, mCondition;

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

        addAlergiesAndConditions();
        editTextWatcher();
    }

    //******************************************************
    private void addAlergiesAndConditions()
    //******************************************************
    {
        if (mAllergiesList == null || mAllergiesList.size() == 0)
            mAllergiesList = new ArrayList<>();
        if (mConditionList == null || mConditionList.size() == 0)
            mConditionList = new ArrayList<>();
        mBinding.addAllergies.setOnClickListener(v -> inputAlert("Allergies"));
        mBinding.addCondition.setOnClickListener(v -> inputAlert("Condition"));
    }

    //******************************************************************************
    private void inputAlert(String text)
    //******************************************************************************
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(text + " " + AndroidUtil.getString(R.string.name));
        builder.setMessage(AndroidUtil.getString(R.string.enter_name) + " " + text.toLowerCase());

        final EditText input = new EditText(getActivity());

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Enter " + text + " " + AndroidUtil.getString(R.string.name));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(20, 10, 20, 10);
        input.setLayoutParams(params);
        builder.setView(input);

        builder.setPositiveButton("save", (dialog, which) ->
        {
            val inputText = input.getText().toString();
            if (TextUtils.isEmpty(inputText))
                return;
            switch (text) {
                case "Allergies":
                    mAllergiesList.add(inputText);
                    startAllergiesRecyclerView();
                    break;
                case "Condition":
                    mConditionList.add(inputText);
                    startConditionRecyclerView();
                    break;
            }

        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    //**************************************************************
    private void startAllergiesRecyclerView()
    //**************************************************************
    {
        val mUser = Laila.instance().getMProfileRequest().getProfile();
        String allergiesList;

        if (mAllergiesList != null && mAllergiesList.size() > 0) {
            val allergy = AndroidUtil.stringJoin(mAllergiesList, ";");
            mUser.setAllergies(allergy);
        }

        mAllergiesListAdapter = new AllergiesListAdapter(mAllergiesList, position ->
        {
            AndroidUtil.displayAlertDialog(
                    AndroidUtil.getString(
                            R.string.delete_item),
                    AndroidUtil.getString(
                            R.string.delete),
                    getActivity(),
                    AndroidUtil.getString(
                            R.string.ok),
                    AndroidUtil.getString(
                            R.string.cancel),
                    (dialog, which) -> {
                        if (which == -1) {
                            mAllergiesList.remove(position);
                            startAllergiesRecyclerView();
                        }
                    });
        }

        );
        mBinding.allergiesRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.allergiesRecycleView.setAdapter(mAllergiesListAdapter);
    }

    //**************************************************************
    private void startConditionRecyclerView()
    //**************************************************************
    {
        val mUser = Laila.instance().getMProfileRequest().getProfile();

        if (mConditionList != null && mConditionList.size() > 0) {
            val condition = AndroidUtil.stringJoin(mConditionList, ";");
            mUser.setMedicalConditions(condition);
        }

        mAllergiesListAdapter = new AllergiesListAdapter(mConditionList, position ->
        {
            AndroidUtil.displayAlertDialog(
                    AndroidUtil.getString(
                            R.string.delete_item),
                    AndroidUtil.getString(
                            R.string.delete),
                    getActivity(),
                    AndroidUtil.getString(
                            R.string.ok),
                    AndroidUtil.getString(
                            R.string.cancel),
                    (dialog, which) -> {
                        if (which == -1) {
                            mConditionList.remove(position);
                            startConditionRecyclerView();
                        }
                    });
        }
        );
        mBinding.conditionRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.conditionRecycleView.setAdapter(mAllergiesListAdapter);
    }

    //******************************************************
    private void edit()
    //******************************************************
    {
        mBinding.height.requestFocus();
        mBinding.weight.setEnabled(true);
        mBinding.height.setEnabled(true);
        mBinding.healthCardNo.setEnabled(true);
        mBinding.insuranceName.setEnabled(true);
        mBinding.insuranceNumber.setEnabled(true);
        mBinding.addAllergies.setEnabled(true);
        mBinding.addCondition.setEnabled(true);
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
        mUser.setWeight_unit("Kg");
        mUser.setHeight_unit("Feet");
    }

    //*******************************************************************
    @Override
    public void onResume()
    //*******************************************************************
    {
        super.onResume();
        val check = Laila.instance().Edit_Profile;
        if (!check) {
            mBinding.weight.setEnabled(false);
            mBinding.height.setEnabled(false);
            mBinding.healthCardNo.setEnabled(false);
            mBinding.insuranceName.setEnabled(false);
            mBinding.insuranceNumber.setEnabled(false);
            mBinding.addAllergies.setEnabled(false);
            mBinding.addCondition.setEnabled(false);
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
        }

        if (user.getWeight() != 0) {
            String userWeight = String.valueOf(user.getWeight());
            if (!TextUtils.isEmpty(userWeight))
                mBinding.weight.setText(userWeight);
        }

    }

}