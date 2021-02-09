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
import com.fantechlabs.lailaa.databinding.FragmentProfileOneBinding;
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

//*****************************************************************************
public class ReadingMaterialsFragment extends BaseFragment
//*****************************************************************************
{
    private FragmentReadingMaterialsBinding mBinding;
    private List<String> mAllergiesList;
    private AllergiesListAdapter mAllergiesListAdapter;
    private Profile mUser;

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
        Laila.instance().setMProfileRequest(new ProfileRequest());
        Laila.instance().getMProfileRequest().setProfile(new Profile());

        if (Laila.instance().getMProfileRequest() == null || Laila.instance().getMProfileRequest().getProfile() == null)
            return;
        setAllergies();
    }

    //*************************************************************
    private void setAllergies()
    //*************************************************************
    {
        if (Laila.instance().getMUser() == null || Laila.instance().getMUser().getProfile() == null)
            return;
        val userProfile = Laila.instance().getMUser().getProfile();
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
                        getContext(),
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
        mBinding.allergiesRecycleView.setAdapter(mAllergiesListAdapter);
    }

}
