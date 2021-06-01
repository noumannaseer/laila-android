package com.fantechlabs.lailaa.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.adapter.AllergiesListAdapter;
import com.fantechlabs.lailaa.databinding.FragmentProfileThreeBinding;
import com.fantechlabs.lailaa.models.allergie_models.RetrieveXmlParcer;
import com.fantechlabs.lailaa.models.allergie_models.XmlParcer;
import com.fantechlabs.lailaa.models.updates.models.Profile;
import com.fantechlabs.lailaa.models.updates.request_models.ProfileRequest;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;

import org.xmlpull.v1.XmlPullParser;

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
    private TextView mAllergyTitle, mAllergyName;
    private EditText mAllergy;
    private ProfileRequest mProfileRequest;
    private Profile mProfile;
    private List<String> mHeightUnits, mWeightUnits;

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
        initViews();
        addAlergiesAndConditions();
        editTextWatcher();
    }

    //******************************************************
    private void initViews()
    //******************************************************
    {
        mProfileRequest = Laila.instance().getMProfileRequest();
        if (mProfileRequest == null)
            mProfileRequest = new ProfileRequest();
        mHeightUnits = new ArrayList<>();
        mWeightUnits = new ArrayList<>();
        mHeightUnits.add("CM");
        mHeightUnits.add("Inche");

        mWeightUnits.add("Kg");
        mWeightUnits.add("Lbs");
    }

    //******************************************************
    private void addAlergiesAndConditions()
    //******************************************************
    {
        if (mAllergiesList == null || mAllergiesList.size() == 0)
            mAllergiesList = new ArrayList<>();
        if (mConditionList == null || mConditionList.size() == 0)
            mConditionList = new ArrayList<>();
        mBinding.addAllergies.setOnClickListener(v -> openDialog("Allergies"));
        mBinding.addCondition.setOnClickListener(v -> openDialog("Conditions"));
    }

    //******************************************************************************
    private void openDialog(String text)
    //******************************************************************************
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_alergy_condition_dialog, null);
        mAllergyTitle = view.findViewById(R.id.alergy_condition_name_title);
        mAllergyName = view.findViewById(R.id.alergy_condition_name);
        mAllergy = view.findViewById(R.id.enter_alergies_name);

        mAllergyTitle.setText(text);
        mAllergyName.setText(text);
        mAllergy.setHint("Enter " + text + " " + AndroidUtil.getString(R.string.name));
        builder.setView(view)
                .setNegativeButton("Cancel", (dialog, i) -> dialog.cancel())
                .setPositiveButton("Save", (dialog, i) -> {

                    val inputText = mAllergy.getText().toString();

                    if (TextUtils.isEmpty(inputText))
                        return;
                    switch (text) {
                        case "Allergies":
                            mAllergiesList.add(inputText);
                            startAllergiesRecyclerView();
                            break;
                        case "Conditions":
                            mConditionList.add(inputText);
                            startConditionRecyclerView();
                            break;
                    }
                });

        builder.show();
    }

    //**************************************************************
    private void startAllergiesRecyclerView()
    //**************************************************************
    {

        if (mAllergiesList != null && mAllergiesList.size() > 0) {
            val allergy = AndroidUtil.stringJoin(mAllergiesList, ";");
            mProfileRequest.setAllergies(allergy);
            Laila.instance().setMProfileRequest(mProfileRequest);
        }
        mAllergiesListAdapter = new AllergiesListAdapter(mAllergiesList, new AllergiesListAdapter.ListClickListener() {
            @Override
            public void onDelete(int position) {
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

            @Override
            public void onClick(String title) {
            }
        });
        mBinding.allergiesRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.allergiesRecycleView.setAdapter(mAllergiesListAdapter);
    }

    //**************************************************************
    private void startConditionRecyclerView()
    //**************************************************************
    {
        if (mConditionList != null && mConditionList.size() > 0) {
            val condition = AndroidUtil.stringJoin(mConditionList, ";");
            mProfileRequest.setMedicalConditions(condition);
            Laila.instance().setMProfileRequest(mProfileRequest);
        }
        mAllergiesListAdapter = new AllergiesListAdapter(mConditionList, new AllergiesListAdapter.ListClickListener() {
            @Override
            public void onDelete(int position) {
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

            @Override
            public void onClick(String title) {

            }
        });

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

        mBinding.healthCardNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mProfileRequest.setHealthCardNumber(s.toString());
                Laila.instance().setMProfileRequest(mProfileRequest);

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
                mProfileRequest.setPrivateInsurance(s.toString());
                Laila.instance().setMProfileRequest(mProfileRequest);
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
                mProfileRequest.setPrivateInsuranceNumber(s.toString());
                Laila.instance().setMProfileRequest(mProfileRequest);

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
                    mProfileRequest.setHeight("");
                    Laila.instance().setMProfileRequest(mProfileRequest);
                    return;
                }
                mProfileRequest.setHeight(s.toString());
                Laila.instance().setMProfileRequest(mProfileRequest);
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
                    mProfileRequest.setWeight("");
                    Laila.instance().setMProfileRequest(mProfileRequest);
                    return;
                }
                mProfileRequest.setWeight(s.toString());
                Laila.instance().setMProfileRequest(mProfileRequest);
            }
        });
        mProfileRequest.setWeightUnit("Kg");
        mProfileRequest.setHeightUnit("CM");
        Laila.instance().setMProfileRequest(mProfileRequest);
    }

    //*******************************************************************
    @Override
    public void onResume()
    //*******************************************************************
    {
        super.onResume();
        edit();
        setData();
        setAllergiesAndCondition();
    }

    //*************************************************************
    @SuppressLint("SetTextI18n")
    private void setData()
    //*************************************************************
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

    //*************************************************************
    private void requestProfile()
    //*************************************************************
    {
        mBinding.healthCardNo.setText(mProfileRequest.getHealthCardNumber());
        mBinding.insuranceName.setText(mProfileRequest.getPrivateInsurance());
        mBinding.insuranceNumber.setText(mProfileRequest.getPrivateInsuranceNumber());
        mBinding.height.setText(mProfileRequest.getHeight());
        mBinding.weight.setText(mProfileRequest.getWeight());
    }

    //*************************************************************
    @SuppressLint("SetTextI18n")
    private void getProfileDate()
    //*************************************************************
    {
        val height = mProfile.getHeight();
        val weight = mProfile.getWeight();
        val healthCardNo = mProfile.getHealthCardNumber();
        val insuranceName = mProfile.getPrivateInsurance();
        val insuranceNumber = mProfile.getPrivateInsuranceNumber();
        val heightUnit = mProfile.getHeightUnit();
        val weightUnit = mProfile.getWeightUnit();
        val allergies = mProfile.getAllergies();
        val medicalCondition = mProfile.getMedicalConditions();

        mBinding.healthCardNo.setText(healthCardNo);
        mBinding.insuranceName.setText(insuranceName);
        mBinding.insuranceNumber.setText(insuranceNumber);
        mBinding.height.setText(String.valueOf(height));
        mBinding.weight.setText(String.valueOf(weight));

        if (!TextUtils.isEmpty(allergies)) {
            mAllergiesList = new ArrayList<>();
            String[] allergiesList = allergies.split(";");
            for (val allergy : allergiesList) {
                if (!TextUtils.isEmpty(allergy))
                    mAllergiesList.add(allergy);
            }

            startAllergiesRecyclerView();
        }

        if (!TextUtils.isEmpty(medicalCondition)) {
            mConditionList = new ArrayList<>();
            String[] conditionList = medicalCondition.split(";");
            for (val condition : conditionList) {
                if (!TextUtils.isEmpty(condition))
                    mConditionList.add(condition);
            }

            startConditionRecyclerView();
        }
    }

    //*************************************************************
    private void setAllergiesAndCondition()
    //*************************************************************
    {
        if (Laila.instance().getMUser_U().getData() == null || Laila.instance().getMUser_U().getData().getProfile() == null)
            return;
        val userProfile = Laila.instance().getMUser_U().getData().getProfile();
        val allergies = userProfile.getAllergies();
        val medicalCondition = userProfile.getMedicalConditions();

        if (!TextUtils.isEmpty(allergies)) {
            mAllergiesList = new ArrayList<>();
            String[] allergiesList = allergies.split(";");
            for (val allergy : allergiesList) {
                if (!TextUtils.isEmpty(allergy))
                    mAllergiesList.add(allergy);
            }

            startAllergiesRecyclerView();
        }

        if (!TextUtils.isEmpty(medicalCondition)) {
            mConditionList = new ArrayList<>();
            String[] conditionList = medicalCondition.split(";");
            for (val condition : conditionList) {
                if (!TextUtils.isEmpty(condition))
                    mConditionList.add(condition);
            }

            startConditionRecyclerView();
        }

    }
}