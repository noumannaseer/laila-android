package com.fantechlabs.lailaa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.adapter.MedicationListAdapter;
import com.fantechlabs.lailaa.databinding.ActivityMedicationBinding;
import com.fantechlabs.lailaa.models.Medication;
import com.fantechlabs.lailaa.models.response_models.DrugCheckResponse;
import com.fantechlabs.lailaa.models.response_models.MedicationResponse;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;
import com.fantechlabs.lailaa.utils.SharedPreferencesUtils;
import com.fantechlabs.lailaa.view_models.DeleteEventViewModel;
import com.fantechlabs.lailaa.view_models.DeleteMedicationViewModel;
import com.fantechlabs.lailaa.view_models.DrugCheckMedicationViewModel;

import java.util.ArrayList;
import java.util.Objects;

import lombok.val;

//**********************************************************
public class MedicationActivity extends BaseActivity
        implements DeleteMedicationViewModel.DeleteMedicationListener,
        DrugCheckMedicationViewModel.DrugCheckMedicationListener,
        DeleteEventViewModel.DeleteEventListener
//**********************************************************

{
    private ActivityMedicationBinding mBinding;
    private ArrayList<Medication> mMedicationList = new ArrayList<>();
    private MedicationListAdapter mMedicationListAdapter;
    private DeleteMedicationViewModel mDeleteMedicationViewModel;
    private DrugCheckMedicationViewModel mDrugCheckMedicationViewModel;
    private int mPosition, mEventPosition;
    private String mMedicineId;

    //**********************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //**********************************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_medication);
        setToolBar(mBinding.toolbar);
        initControl();
    }

    //**********************************************************
    public void setToolBar(Toolbar toolBar)
    //**********************************************************
    {
        setSupportActionBar(toolBar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //**********************************************************
    @Override
    protected boolean showStatusBar()
    //**********************************************************
    {
        return false;
    }

    //**********************************************************
    private void initControl()
    //**********************************************************
    {
        mDeleteMedicationViewModel = new DeleteMedicationViewModel(this);
        mDrugCheckMedicationViewModel = new DrugCheckMedicationViewModel(this);

        addMedications();
    }

    //**********************************************************
    private void addMedications()
    //**********************************************************
    {
        mBinding.addMedication.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), MedicationDetailsActivity.class)));
    }

    //**********************************************************
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    //**********************************************************
    {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //***************************************************
    @Override
    protected void onResume()
    //***************************************************
    {
        super.onResume();
        mMedicationList = new ArrayList<>();


        if (Laila.instance().getMUser() == null || Laila.instance().getMUser().getMedication() == null) {
            mBinding.noRecord.setVisibility(View.VISIBLE);
            mBinding.medicineRecyclerview.setVisibility(View.GONE);
            return;
        }

        val medication = Laila.instance().getMUser().getMedication();
        if (medication == null || medication.size() == 0)
            return;

        mBinding.noRecord.setVisibility(View.GONE);
        mBinding.medicineRecyclerview.setVisibility(View.VISIBLE);

        mMedicationList = Laila.instance().getMUser().getMedication();
        startRecyclerView();
    }

    //*******************************************************************
    @Override
    public void onDrugCheckSuccessfully(@Nullable DrugCheckResponse response)
    //*******************************************************************
    {
        hideLoadingDialog();
        Laila.instance().is_medicine_added = false;

        AndroidUtil.displayAlertDialog(
                response.getMsg(),
                AndroidUtil.getString(R.string.alert),
                this,
                AndroidUtil.getString(
                        R.string.ok),
                AndroidUtil.getString(
                        R.string.view_interactions),
                (dialog, which) -> {
                    if (which == -2) {
                        Intent interactionIntent = new Intent(this, MedicineInteractionActivity.class);
                        interactionIntent.putExtra(MedicineInteractionActivity.INTERACTION, response);
                        startActivity(interactionIntent);
                    }
                });
    }

    //*******************************************************************
    @Override
    public void onDrugCheckFailed(@NonNull String errorMessage)
    //*******************************************************************
    {
        hideLoadingDialog();
        AndroidUtil.displayAlertDialog(errorMessage, AndroidUtil.getString(R.string.alert), this);
    }

    //**************************************************************
    private void startRecyclerView()
    //**************************************************************
    {

        mMedicationListAdapter = new MedicationListAdapter(mMedicationList, new MedicationListAdapter.ListClickListener() {

            //******************************************************************
            @Override
            public void viewInteractions(int id)
            //******************************************************************
            {
                if (id == 0)
                    return;
                showLoadingDialog();
                mDrugCheckMedicationViewModel.drugCheckMedication(id);
            }

            //******************************************************************
            @Override
            public void onInformation(String rxDinNumber)
            //******************************************************************
            {
                Intent informationIntent = new Intent(MedicationActivity.this, InformationActivity.class);
                informationIntent.putExtra(InformationActivity.RXDINNUMBER, rxDinNumber);
                startActivity(informationIntent);
            }

            //******************************************************************
            @Override
            public void onDelete(int position, int id)
            //******************************************************************
            {
                mPosition = position;
                mMedicineId = String.valueOf(id);
                AndroidUtil.displayAlertDialog(
                        AndroidUtil.getString(
                                R.string.delete_item),
                        AndroidUtil.getString(
                                R.string.alert),
                        MedicationActivity.this,
                        AndroidUtil.getString(
                                R.string.ok),
                        AndroidUtil.getString(
                                R.string.cancel),
                        (dialog, which) -> {
                            if (which == -1) {
                                showLoadingDialog();
                                mDeleteMedicationViewModel.deleteMedication(mMedicineId);
                            }
                        });
            }

            //******************************************************************
            @Override
            public void onUpdate(int position)
            //******************************************************************
            {
                Laila.instance().setMMedicationPosition(position);
                Laila.instance().on_update_medicine = true;
                startActivity(new Intent(MedicationActivity.this, AddMedicationActivity.class));
            }

        }, this);
        mBinding.medicineRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mBinding.medicineRecyclerview.setAdapter(mMedicationListAdapter);

    }


    //******************************************************************
    @Override
    public void onSuccessfullyDeleteEvent(@Nullable String result)
    //******************************************************************
    {
        val events = Laila.instance().getMUser().getEvents();
        if (events == null || events.size() == 0)
            return;
        events.remove(mEventPosition);
        hideLoadingDialog();
        SharedPreferencesUtils.setValue(Constants.USER_DATA, Laila.instance().getMUser());
        AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.delete_medication), AndroidUtil.getString(R.string.success), this);
    }

    //******************************************************************
    @Override
    public void onFailedToDeleteEvent(@NonNull String error)
    //******************************************************************
    {
        hideLoadingDialog();
        AndroidUtil.displayAlertDialog(error, AndroidUtil.getString(R.string.error), this);
    }

    //*******************************************************************
    @Override
    public void onSuccessfully(@Nullable MedicationResponse medicationResponse)
    //*******************************************************************
    {
        if (!TextUtils.isEmpty(mMedicineId))
            Laila.instance().getMUser().getMedication().remove(mPosition);
        SharedPreferencesUtils.setValue(Constants.USER_DATA, Laila.instance().getMUser());
        startRecyclerView();
        hideLoadingDialog();
    }

    //*******************************************************************
    @Override
    public void onFailed(@NonNull String errorMessage)
    //*******************************************************************
    {
        hideLoadingDialog();
        AndroidUtil.displayAlertDialog(errorMessage, AndroidUtil.getString(R.string.error), this);
    }

}
