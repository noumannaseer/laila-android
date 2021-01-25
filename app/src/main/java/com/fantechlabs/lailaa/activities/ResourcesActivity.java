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
import com.fantechlabs.lailaa.databinding.ActivityResourcesBinding;
import com.fantechlabs.lailaa.models.Profile;
import com.fantechlabs.lailaa.models.SearchMedicine;
import com.fantechlabs.lailaa.models.allergie_models.AllergieListerner;
import com.fantechlabs.lailaa.models.allergie_models.XmlParcer;
import com.fantechlabs.lailaa.request_models.ProfileRequest;
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
    private AllergiesListAdapter mAllergiesListAdapter;
    private TextView mAllergyTitle, mAllergyName;
    private EditText mAllergy;
    private Profile mUser;
    private SearchMedicationViewModel mSearchMedicationViewModel;
    private Runnable mSearchRunnable;

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

//                moveToBrowserActivity(diseaseName, Constants.BASE_URL_SEARCH_TERMS + diseaseName + ".html");

            }
        });
    }

    //********************************************
    private void moveToBrowserActivity(String title, String url)
    //********************************************
    {
        Intent browserIntent = new Intent(ResourcesActivity.this, BrowserActivity.class);
        browserIntent.putExtra(BrowserActivity.SCREEN_URL, url);
        browserIntent.putExtra(BrowserActivity.INFO_TITLE, title);
        startActivity(browserIntent);
    }

    //**************************************************************
    private void goToAddMedication()
    //**************************************************************
    {
        Intent addMedicationIntent = new Intent(getContext(), AddMedicationActivity.class);
        getContext().startActivity(addMedicationIntent);
    }

    //******************************************************
    private void addAlergiesAndConditions()
    //******************************************************
    {
        if (mAllergiesList == null || mAllergiesList.size() == 0)
            mAllergiesList = new ArrayList<>();
        if (mConditionList == null || mConditionList.size() == 0)
            mConditionList = new ArrayList<>();
//        mBinding.addAllergies.setOnClickListener(v -> openDialog("Allergies"));
//        mBinding.addCondition.setOnClickListener(v -> openDialog("Conditions"));
    }

    //******************************************************************************
    private void openDialog(String text)
    //******************************************************************************
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();

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
        mUser = Laila.instance().getMProfileRequest().getProfile();

        if (mAllergiesList != null && mAllergiesList.size() > 0) {
            val allergy = AndroidUtil.stringJoin(mAllergiesList, ";");
            mUser.setAllergies(allergy);
        }
        mAllergiesListAdapter = new AllergiesListAdapter(mAllergiesList, new AllergiesListAdapter.ListClickListener() {
            @Override
            public void onDelete(int position) {
                AndroidUtil.displayAlertDialog(
                        AndroidUtil.getString(
                                R.string.delete_item),
                        AndroidUtil.getString(
                                R.string.delete),
                        ResourcesActivity.this,
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
        mBinding.allergiesRecycleView.setAdapter(mAllergiesListAdapter);
    }

    //**************************************************************
    private void startConditionRecyclerView()
    //**************************************************************
    {
        mUser = Laila.instance().getMProfileRequest().getProfile();

        if (mConditionList != null && mConditionList.size() > 0) {
            val condition = AndroidUtil.stringJoin(mConditionList, ";");
            mUser.setMedicalConditions(condition);
        }
        mAllergiesListAdapter = new AllergiesListAdapter(mConditionList, new AllergiesListAdapter.ListClickListener() {
            @Override
            public void onDelete(int position) {
                AndroidUtil.displayAlertDialog(
                        AndroidUtil.getString(
                                R.string.delete_item),
                        AndroidUtil.getString(
                                R.string.delete),
                        ResourcesActivity.this,
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

        mBinding.conditionRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.conditionRecycleView.setAdapter(mAllergiesListAdapter);
    }


    //*******************************************************************
    @Override
    public void onResume()
    //*******************************************************************
    {
        super.onResume();
        Laila.instance().setMProfileRequest(new ProfileRequest());
        Laila.instance().getMProfileRequest().setProfile(new Profile());

        if (Laila.instance().getMProfileRequest() == null || Laila.instance().getMProfileRequest().getProfile() == null)
            return;
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