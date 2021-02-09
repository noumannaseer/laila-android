package com.fantechlabs.lailaa.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.activities.AlergieResourceDetails;
import com.fantechlabs.lailaa.activities.ResourcesActivity;
import com.fantechlabs.lailaa.adapter.AllergiesListAdapter;
import com.fantechlabs.lailaa.databinding.FragmentHandoutsBinding;
import com.fantechlabs.lailaa.databinding.FragmentReadingMaterialsBinding;
import com.fantechlabs.lailaa.models.Profile;
import com.fantechlabs.lailaa.models.allergie_models.AllergieListerner;
import com.fantechlabs.lailaa.models.allergie_models.XmlParcer;
import com.fantechlabs.lailaa.request_models.ProfileRequest;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;
import lombok.val;

//****************************************************************************
public class HandoutsFragment extends BaseFragment
//****************************************************************************
{
    private FragmentHandoutsBinding mBinding;
    private List<String> mConditionList;
    private AllergiesListAdapter mAllergiesListAdapter;
    private Profile mUser;

    //****************************************************************************
    @Override
    public @NonNull View onCreateViewBaseFragment(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    //****************************************************************************
    {
        if (mRootView == null) {
            mBinding = FragmentHandoutsBinding.inflate(inflater, parent, false);
            mRootView = mBinding.getRoot();
        }
        initControls();
        return mRootView;
    }

    //******************************************************
    private void initControls()
    //******************************************************
    {
        addConditions();
    }

    //******************************************************
    private void addConditions()
    //******************************************************
    {
        if (mConditionList == null || mConditionList.size() == 0)
            mConditionList = new ArrayList<>();
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
        setConditions();
    }

    //*************************************************************
    private void setConditions()
    //*************************************************************
    {
        if (Laila.instance().getMUser() == null || Laila.instance().getMUser().getProfile() == null)
            return;
        val userProfile = Laila.instance().getMUser().getProfile();
        val medicalCondition = userProfile.getMedicalConditions();

        if (!TextUtils.isEmpty(medicalCondition)) {
            mConditionList = new ArrayList<>();
            String[] conditionList = medicalCondition.split(";");
            for (val condition : conditionList) {
                if (!TextUtils.isEmpty(condition))
                    mConditionList.add(condition);
            }
            mBinding.conditionRecycleView.setVisibility(View.VISIBLE);
            mBinding.noRecord.setVisibility(View.GONE);
            startConditionRecyclerView();
            return;
        }
        mBinding.conditionRecycleView.setVisibility(View.GONE);
        mBinding.noRecord.setVisibility(View.VISIBLE);

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
                        getContext(),
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
                showLoadingDialog();
                new XmlParcer(new AllergieListerner() {
                    //***********************************************************
                    @Override
                    public void onExecutionCompleted()
                    //***********************************************************
                    {
                        hideLoadingDialog();
                        Intent intent = new Intent(getContext(), AlergieResourceDetails.class);
                        intent.putExtra(Constants.DISEASE_NAME, title);
                        startActivity(intent);
                    }

                    //***********************************************************
                    @Override
                    public void onExecutionFailed()
                    //***********************************************************
                    {
                        hideLoadingDialog();
                        Intent intent = new Intent(getContext(), AlergieResourceDetails.class);
                        startActivity(intent);
                    }
                }).execute(Constants.BASE_URL_Terms + title);
            }
        });

        mBinding.conditionRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.conditionRecycleView.setAdapter(mAllergiesListAdapter);
    }

}
