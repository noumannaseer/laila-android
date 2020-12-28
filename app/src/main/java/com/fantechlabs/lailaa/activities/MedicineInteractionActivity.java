package com.fantechlabs.lailaa.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fantechlabs.lailaa.adapter.MedicineInteractionsAdapter;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.databinding.ActivityMedicineInteractionBinding;
import com.fantechlabs.lailaa.models.response_models.DrugCheckResponse;


//****************************************************************
public class MedicineInteractionActivity extends BaseActivity
//****************************************************************
{

    private ActivityMedicineInteractionBinding mBinding;
    public static final String INTERACTION = "INTERACTION";
    public static final String INTERACTION_MSG = "INTERACTION_MSG";
    private DrugCheckResponse mDrugCheckResponse;
    private String mInteractionMsg;
    private MedicineInteractionsAdapter mMedicineInteractionsAdapter;


    //****************************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //****************************************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_medicine_interaction);
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("");
        initControl();
    }

    //****************************************************************
    @Override
    protected boolean showStatusBar()
    //****************************************************************
    {
        return false;
    }

    //*************************************
    private void initControl()
    //*************************************
    {
        getParcelable();
        mBinding.interactionNo.setText(mDrugCheckResponse.getMsg());
        startRecyclerView();
    }

    //**************************************************************
    private void startRecyclerView()
    //**************************************************************
    {
        if (mDrugCheckResponse.getInteractionMsgs() == null || mDrugCheckResponse.getInteractionMsgs().size() == 0)
            return;

        mMedicineInteractionsAdapter = new MedicineInteractionsAdapter(mDrugCheckResponse.getInteractionMsgs(), this);
        mBinding.interactionRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mBinding.interactionRecyclerview.setAdapter(mMedicineInteractionsAdapter);
    }


    //******************************************************************
    private void getParcelable()
    //******************************************************************
    {
        if (getIntent().getExtras()
                .containsKey(INTERACTION)) {
            mDrugCheckResponse = getIntent().getParcelableExtra(INTERACTION);
//            mInteractionMsg = getIntent().getStringExtra(INTERACTION_MSG);
        }
    }
}