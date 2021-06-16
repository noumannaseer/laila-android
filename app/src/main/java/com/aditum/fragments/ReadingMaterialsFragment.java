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
import com.aditum.databinding.FragmentReadingMaterialsBinding;
import com.aditum.models.allergie_models.AllergieListerner;
import com.aditum.models.allergie_models.XmlParcer;
import com.aditum.models.updates.request_models.ProfileRequest;
import com.aditum.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;
import lombok.val;

//*****************************************************************************
public class ReadingMaterialsFragment extends BaseFragment
//*****************************************************************************
{
    private FragmentReadingMaterialsBinding mBinding;
    private List<String> mAllergiesList;
    private ResourcesListAdapter mResourcesListAdapter;
    private ProfileRequest mUser;

    //*****************************************************************************
    @Override
    public @NonNull View onCreateViewBaseFragment(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    //*****************************************************************************
    {
        if (mRootView == null) {
            mBinding = FragmentReadingMaterialsBinding.inflate(inflater, parent, false);
            mRootView = mBinding.getRoot();
        }
        initControls();
        return mRootView;
    }

    //*****************************************************************************
    private void initControls()
    //*****************************************************************************
    {
        addAlergies();
    }

    //******************************************************
    private void addAlergies()
    //******************************************************
    {
        if (mAllergiesList == null || mAllergiesList.size() == 0)
            mAllergiesList = new ArrayList<>();
    }

    @Override
    public void onResume() {
        super.onResume();
        val user = Laila.instance().getMUser_U().getData();
        val profile = Laila.instance().getMUser_U().getData().getProfile();
        if (user == null || profile == null)
            return;
        setAllergies();
    }

    //*************************************************************
    private void setAllergies()
    //*************************************************************
    {
        val userProfile = Laila.instance().getMUser_U().getData().getProfile();
        val allergies = userProfile.getAllergies();

        if (!TextUtils.isEmpty(allergies)) {
            mAllergiesList = new ArrayList<>();
            String[] allergiesList = allergies.split(";");
            for (val allergy : allergiesList) {
                if (!TextUtils.isEmpty(allergy))
                    mAllergiesList.add(allergy);
            }
            mBinding.allergiesRecycleView.setVisibility(View.VISIBLE);
            mBinding.noRecord.setVisibility(View.GONE);
            startAllergiesRecyclerView();
            return;
        }
        mBinding.allergiesRecycleView.setVisibility(View.GONE);
        mBinding.noRecord.setVisibility(View.VISIBLE);
    }

    //**************************************************************
    private void startAllergiesRecyclerView()
    //**************************************************************
    {
//        mUser = Laila.instance().getMProfileRequest();

//        if (mAllergiesList != null && mAllergiesList.size() > 0) {
//            val allergy = AndroidUtil.stringJoin(mAllergiesList, ";");
//            mUser.setAllergies(allergy);
//        }
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
                        Intent intent = new Intent(getContext(), AlergieResourceDetails.class);
                        intent.putExtra(Constants.DISEASE_NAME, diseaseName);
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
                }).execute(Constants.BASE_URL_Terms + diseaseName);
            }
        });
        mBinding.allergiesRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.allergiesRecycleView.setAdapter(mResourcesListAdapter);
    }

}
