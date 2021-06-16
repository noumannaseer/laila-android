package com.aditum.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aditum.Laila;
import com.aditum.R;
import com.aditum.adapter.MedicationListAdapter;
import com.aditum.databinding.ActivityMedicationBinding;
import com.aditum.models.updates.models.Medication;
import com.aditum.models.updates.response_models.MedicationResponse;
import com.aditum.models.updates.response_models.MedicineInteractionResponse;
import com.aditum.utils.AndroidUtil;
import com.aditum.utils.Constants;
import com.aditum.utils.SharedPreferencesUtils;
import com.aditum.view_models.AddMedicationViewModel;
import com.aditum.view_models.DeleteEventViewModel;
import com.aditum.view_models.DeleteMedicationViewModel;
import com.aditum.view_models.DrugCheckMedicationViewModel;

import java.util.ArrayList;
import java.util.Objects;

import lombok.val;

//**********************************************************
public class MedicationActivity extends BaseActivity
        implements DeleteMedicationViewModel.DeleteMedicationListener,
        DrugCheckMedicationViewModel.DrugCheckMedicationListener,
        DeleteEventViewModel.DeleteEventListener,
        AddMedicationViewModel.AddMedicationListener
//**********************************************************

{
    private ActivityMedicationBinding mBinding;
    private ArrayList<Medication> mMedicationList = new ArrayList<>();
    private MedicationListAdapter mMedicationListAdapter;
    private DeleteMedicationViewModel mDeleteMedicationViewModel;
    private DrugCheckMedicationViewModel mDrugCheckMedicationViewModel;
    private AddMedicationViewModel mAddMedicationViewModel;
    private DeleteEventViewModel mDeleteEventViewModel;
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
        initViewModels();
        getMedications();
        addMedications();

    }

    //**********************************************************
    private void getMedications()
    //**********************************************************
    {
        if (Laila.instance().from_update_medication) {
            Laila.instance().from_update_medication = false;
            showLoadingDialog();
            mAddMedicationViewModel.getMedications();
            return;
        }
        mMedicationList = new ArrayList<>();
        if (Laila.instance().getMUser_U() == null)
            return;
        if (Laila.instance().getMUser_U().getData() == null)
            return;
        mMedicationList = Laila.instance().getMUser_U().getData().getMedicationList();

        if (mMedicationList == null || Laila.instance().from_update_medication) {
            Laila.instance().from_update_medication = false;
            showLoadingDialog();
            mAddMedicationViewModel.getMedications();
            return;
        }
        startRecyclerView();
    }

    //**********************************************************
    private void initViewModels()
    //**********************************************************
    {
        mDeleteMedicationViewModel = new DeleteMedicationViewModel(this);
        mDrugCheckMedicationViewModel = new DrugCheckMedicationViewModel(this);
        mDeleteEventViewModel = new DeleteEventViewModel(this);
        mAddMedicationViewModel = new AddMedicationViewModel(this);
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
//        val medicationList = Laila.instance().getMUser_U().getData().getMedicationList();
//        if (medicationList == null) {
//            mBinding.noRecord.setVisibility(View.VISIBLE);
//            mBinding.medicineRecyclerview.setVisibility(View.GONE);
//            return;
//        }
//        mBinding.noRecord.setVisibility(View.GONE);
//        mBinding.medicineRecyclerview.setVisibility(View.VISIBLE);
    }

    //*******************************************************************
    @Override
    public void onDrugCheckSuccessfully(@Nullable MedicineInteractionResponse response)
    //*******************************************************************
    {
        hideLoadingDialog();
        Laila.instance().is_medicine_added = false;

        AndroidUtil.displayAlertDialog(
                response.getData().getMessage(),
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
//        if (!TextUtils.isEmpty(mMedicineId))
//            getMedicationEvent();
        Laila.instance().getMUser_U().getData().getMedicationList().remove(mPosition);
        SharedPreferencesUtils.setValue(Constants.USER_DATA, Laila.instance().getMUser_U());
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

    //*******************************************************************
    @Override
    public void onSuccessfullyGetMedications(@Nullable MedicationResponse medicationResponse)
    //*******************************************************************
    {
        hideLoadingDialog();
        val responseMedicationList = medicationResponse.getData().getMedicationList();

        if (responseMedicationList == null) {
            mBinding.noRecord.setVisibility(View.VISIBLE);
            mBinding.medicineRecyclerview.setVisibility(View.GONE);
            return;
        }
        mBinding.noRecord.setVisibility(View.GONE);
        mBinding.medicineRecyclerview.setVisibility(View.VISIBLE);

        mMedicationList = new ArrayList<>();
        mMedicationList = responseMedicationList;
        Laila.instance().getMUser_U().getData().setMedicationList(mMedicationList);
        SharedPreferencesUtils.setValue(Constants.USER_DATA, Laila.instance().getMUser_U());
        startRecyclerView();

    }

    //*******************************************************************
    @Override
    public void onFailedGetMedications(@NonNull String errorMessage)
    //*******************************************************************
    {
        hideLoadingDialog();
        AndroidUtil.displayAlertDialog(errorMessage, AndroidUtil.getString(R.string.error), this);
    }

}
