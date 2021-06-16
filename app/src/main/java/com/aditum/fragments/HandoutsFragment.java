package com.aditum.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.aditum.Laila;
import com.aditum.activities.AlergieResourceDetails;
import com.aditum.adapter.ResourcesListAdapter;
import com.aditum.databinding.FragmentHandoutsBinding;
import com.aditum.models.allergie_models.AllergieListerner;
import com.aditum.models.allergie_models.XmlParcer;
import com.aditum.utils.Constants;

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
    private ResourcesListAdapter mResourcesListAdapter;
//    private ProfileRequest mUser;

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
        if (Laila.instance().getMUser_U().getData() == null || Laila.instance().getMUser_U().getData().getProfile() == null)
            return;
        setConditions();
    }

    //*************************************************************
    private void setConditions()
    //*************************************************************
    {

        val userProfile = Laila.instance().getMUser_U().getData().getProfile();
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
//        mUser = Laila.instance().getMProfileRequest();
//
//        if (mConditionList != null && mConditionList.size() > 0) {
//            val condition = AndroidUtil.stringJoin(mConditionList, ";");
//            mUser.setMedicalConditions(condition);
//        }
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
        mBinding.conditionRecycleView.setAdapter(mResourcesListAdapter);
    }

}
