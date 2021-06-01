package com.fantechlabs.lailaa.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.adapter.AllergiesListAdapter;
import com.fantechlabs.lailaa.adapter.ResourcesListAdapter;
import com.fantechlabs.lailaa.databinding.ActivityResourcesBinding;
import com.fantechlabs.lailaa.models.Profile;
import com.fantechlabs.lailaa.models.SearchMedicine;
import com.fantechlabs.lailaa.models.allergie_models.AllergieListerner;
import com.fantechlabs.lailaa.models.allergie_models.XmlParcer;
import com.fantechlabs.lailaa.models.updates.request_models.ProfileRequest;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;
import com.fantechlabs.lailaa.view_models.SearchMedicationViewModel;

import java.util.ArrayList;
import java.util.List;

import lombok.val;

import static com.fantechlabs.lailaa.utils.AndroidUtil.getContext;

//*****************************************************
public class ResourcesActivity extends BaseActivity
//*****************************************************
{
    private ActivityResourcesBinding mBinding;
    private List<String> mAllergiesList;
    private List<String> mConditionList;
    private ResourcesListAdapter mResourcesListAdapter;
    private ProfileRequest mUser;

    //*****************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //*****************************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_resources);
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("");
        initControls();
    }

    //***********************************************************
    private void initControls()
    //***********************************************************
    {
        addAlergiesAndConditions();

        mBinding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                val diseaseName = mBinding.diseaseName.getText().toString();
                if (TextUtils.isEmpty(diseaseName)) {
                    AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.disease_name_required), AndroidUtil.getString(R.string.alert), ResourcesActivity.this);
                    return;
                }
                showLoadingDialog();
                new XmlParcer(new AllergieListerner() {
                    //***********************************************************
                    @Override
                    public void onExecutionCompleted()
                    //***********************************************************
                    {
                        hideLoadingDialog();
                        Intent intent = new Intent(ResourcesActivity.this, AlergieResourceDetails.class);
                        intent.putExtra(Constants.DISEASE_NAME, diseaseName);
                        startActivity(intent);
                    }

                    //***********************************************************
                    @Override
                    public void onExecutionFailed()
                    //***********************************************************
                    {
                        hideLoadingDialog();
                        Intent intent = new Intent(ResourcesActivity.this, AlergieResourceDetails.class);
                        startActivity(intent);
                    }
                }).execute(Constants.BASE_URL_Terms + diseaseName);
            }
        });
    }

    //******************************************************
    private void addAlergiesAndConditions()
    //******************************************************
    {
        if (mAllergiesList == null || mAllergiesList.size() == 0)
            mAllergiesList = new ArrayList<>();
        if (mConditionList == null || mConditionList.size() == 0)
            mConditionList = new ArrayList<>();
    }


    //**************************************************************
    private void startAllergiesRecyclerView()
    //**************************************************************
    {
        mUser = Laila.instance().getMProfileRequest();

        if (mAllergiesList != null && mAllergiesList.size() > 0) {
            val allergy = AndroidUtil.stringJoin(mAllergiesList, ";");
            mUser.setAllergies(allergy);
        }
        mResourcesListAdapter = new ResourcesListAdapter(mAllergiesList, new ResourcesListAdapter.ListClickListener() {

            //***********************************************************
            @Override
            public void onClick(String diseaseName)
            //***********************************************************
            {
                showLoadingDialog();
                new XmlParcer(new AllergieListerner() {
                    //***********************************************************
                    @Override
                    public void onExecutionCompleted()
                    //***********************************************************
                    {
                        hideLoadingDialog();
                        Intent intent = new Intent(ResourcesActivity.this, AlergieResourceDetails.class);
                        intent.putExtra(Constants.DISEASE_NAME, diseaseName);
                        startActivity(intent);
                    }

                    //***********************************************************
                    @Override
                    public void onExecutionFailed()
                    //***********************************************************
                    {
                        hideLoadingDialog();
                        Intent intent = new Intent(ResourcesActivity.this, AlergieResourceDetails.class);
                        startActivity(intent);
                    }
                }).execute(Constants.BASE_URL_Terms + diseaseName);
            }
        });
        mBinding.allergiesRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.allergiesRecycleView.setAdapter(mResourcesListAdapter);
    }

    //**************************************************************
    private void startConditionRecyclerView()
    //**************************************************************
    {
        mUser = Laila.instance().getMProfileRequest();

        if (mConditionList != null && mConditionList.size() > 0) {
            val condition = AndroidUtil.stringJoin(mConditionList, ";");
            mUser.setMedicalConditions(condition);
        }
        mResourcesListAdapter = new ResourcesListAdapter(mConditionList, new ResourcesListAdapter.ListClickListener() {
            @Override
            public void onClick(String title) {
                showLoadingDialog();
                new XmlParcer(new AllergieListerner() {
                    //***********************************************************
                    @Override
                    public void onExecutionCompleted()
                    //***********************************************************
                    {
                        hideLoadingDialog();
                        Intent intent = new Intent(ResourcesActivity.this, AlergieResourceDetails.class);
                        intent.putExtra(Constants.DISEASE_NAME, title);
                        startActivity(intent);
                    }

                    //***********************************************************
                    @Override
                    public void onExecutionFailed()
                    //***********************************************************
                    {
                        hideLoadingDialog();
                        Intent intent = new Intent(ResourcesActivity.this, AlergieResourceDetails.class);
                        startActivity(intent);
                    }
                }).execute(Constants.BASE_URL_Terms + title);
            }
        });

        mBinding.conditionRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.conditionRecycleView.setAdapter(mResourcesListAdapter);
    }


    //*******************************************************************
    @Override
    public void onResume()
    //*******************************************************************
    {
        super.onResume();
//        Laila.instance().setMProfileRequest(new ProfileRequest());
//        Laila.instance().getMProfileRequest().setProfile(new Profile());
//
//        if (Laila.instance().getMProfileRequest() == null || Laila.instance().getMProfileRequest().getProfile() == null)
//            return;
        setAllergiesAndCondition();
    }

    //*************************************************************
    private void setAllergiesAndCondition()
    //*************************************************************
    {
        if (Laila.instance().getMUser() == null || Laila.instance().getMUser().getProfile() == null)
            return;
        val userProfile = Laila.instance().getMUser().getProfile();
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


    //*****************************************************
    @Override
    protected boolean showStatusBar()
    //*****************************************************
    {
        return false;
    }
}