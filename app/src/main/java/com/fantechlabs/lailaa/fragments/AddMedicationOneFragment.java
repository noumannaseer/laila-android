package com.fantechlabs.lailaa.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.fantechlabs.lailaa.activities.AddMedicationActivity;
import com.fantechlabs.lailaa.activities.MedicationActivity;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.databinding.FragmentAddMedicationOneBinding;
import com.fantechlabs.lailaa.models.response_models.MedicationResponse;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;
import com.fantechlabs.lailaa.utils.SharedPreferencesUtils;
import com.fantechlabs.lailaa.view_models.AddMedicationViewModel;

import java.util.ArrayList;

import lombok.NonNull;
import lombok.val;


//***********************************************************
public class AddMedicationOneFragment extends BaseFragment
        implements View.OnClickListener,
        AddMedicationViewModel.AddMedicationListener
//***********************************************************

{

    public static final String TAG = "medicine-content";
    private FragmentAddMedicationOneBinding mBinding;
    private View mRootView;
    private String mMedicineName;
    private String mMedicineType;
    private int mUpdateMedicineId;
    private AddMedicationViewModel mAddMedicationViewModel;


    //***********************************************************
    public AddMedicationOneFragment()
    //***********************************************************
    {
        // Required empty public constructor
    }

    //***********************************************************
    @Override
    public View onCreateViewBaseFragment(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    //***********************************************************
    {
        if (mRootView == null) {
            mBinding = FragmentAddMedicationOneBinding.inflate(inflater, parent, false);
            mRootView = mBinding.getRoot();
            initControl();
        }
        return mRootView;
    }

    //***********************************************************
    public void initControl()
    //***********************************************************
    {
        mAddMedicationViewModel = new AddMedicationViewModel(this);
        infoClickListener();
        setData();
        val onUpdateMedicine = Laila.instance().on_update_medicine;
        mBinding.medicationType.setOnCheckedChangeListener((group, checkedId) ->
        {
            mBinding.addBtn.setVisibility(View.GONE);
            mBinding.navigateNext.setVisibility(View.VISIBLE);

            val isCheck = mBinding.prescribed.isChecked();
            if (!isCheck) {
                mBinding.navigateNext.setVisibility(View.GONE);
                mBinding.addBtn.setVisibility(View.VISIBLE);
                mBinding.addBtn.setText(onUpdateMedicine ? R.string.update : R.string.save);
                mBinding.addBtn.setBackgroundTintList(
                        ContextCompat.getColorStateList(getActivity(), onUpdateMedicine ? R.color.button_background : R.color.darkBlue));
            }
//            RXCare.instance().is_over_the_counter = isCheck? false : true;
        });
        mBinding.navigateNext.setOnClickListener(v -> validation());
        mBinding.addBtn.setOnClickListener(v -> validation());
    }

    //***********************************************************
    private void infoClickListener()
    //***********************************************************
    {
        mBinding.nameInfo.setOnClickListener(this);
        mBinding.rxDinInfo.setOnClickListener(this);
        mBinding.formUnitInfo.setOnClickListener(this);
        mBinding.strengthInfo.setOnClickListener(this);
        mBinding.strengthUnitInfo.setOnClickListener(this);
        mBinding.addMedicationOneMainView.setOnClickListener(this);
    }

    //*************************************************************
    @Override
    public void onClick(View v)
    //*************************************************************
    {
        switch (v.getId()) {
            case R.id.name_info:
                AndroidUtil.showToolTip(v, R.string.name_info);
                break;
            case R.id.rx_din_info:
                AndroidUtil.showToolTip(v, R.string.rx_din_info);
                break;
            case R.id.strength_info:
                AndroidUtil.showToolTip(v, R.string.strength_info);
                break;
            case R.id.add_medication_one_main_view:
                if (AndroidUtil.mTooltip != null)
                    AndroidUtil.mTooltip.dismiss();
                break;
        }
    }

    //*************************************************************
    private void setData()
    //*************************************************************
    {
        val onUpdateMedicine = Laila.instance().on_update_medicine;
        if (onUpdateMedicine) {
            onUpdate();
            return;
        }
        try {
            val medication = Laila.instance().getMSearchMedicine();
            val medicineName = Laila.instance().getMSearchMedicine().getBrandName();
            val title = Laila.instance().getMSearchMedicine().getTitle();

            if (medication == null)
                return;

            val din = Laila.instance().getMSearchMedicine().getDrugIdentificationNumber();
            if (!TextUtils.isEmpty(din))
                mBinding.rxDinNumber.setText(din);
            if (TextUtils.isEmpty(medicineName)) {
                mBinding.mediName.setText(title);
                return;
            }

            val check = medicineName.contains("%");
            if (check) {
                mBinding.mediName.setText(medicineName);
                return;
            }

            String[] medicineContent = Laila.instance()
                    .getMSearchMedicine()
                    .getBrandName()
                    .split(" ");

            if (medicineContent.length == 0) {
                return;
            }

            for (val content : medicineContent) {
                Log.d(TAG, "setData: " + content);
            }

            if (medicineContent == null || medicineContent.length == 0)
                return;
            if (medicineContent[0] == null || medicineContent[0].length() == 0)
                return;
            mMedicineName = medicineContent[0];
            if (medicineContent[1] == null || medicineContent[1].length() == 0)
                return;
            val medicineType = medicineContent[1].toLowerCase();

            if (medicineType.equals("tab") || medicineType.equals("tabs") || medicineType.equals(
                    "tablets") || medicineType.equals("tablet"))
                mMedicineType = "Tablet";
            if (medicineType.equals("caps") || medicineType.equals("cap") || medicineType.equals(
                    "caplet") || medicineType.equals("caplets"))
                mMedicineType = "Capsule";
            if (medicineType.equals("injs") || medicineType.equals("inj") || medicineType.equals(
                    "injection") || medicineType.equals("injections"))
                mMedicineType = "Injection";
            if (medicineType.equals("drop") || medicineType.equals("drops"))
                mMedicineType = "Drops";
            if (medicineType.equals("inhaler") || medicineType.equals(
                    "inhalers") || medicineType.equals("inhal") || medicineType.equals("inhals"))
                mMedicineType = "InHaler";
            if (medicineType.equals("teaspoon") || medicineType.equals(
                    "teaspoons") || medicineType.equals("teas"))
                mMedicineType = "Teaspoon";

            if (mMedicineType.length() == 0 || mMedicineType == null)
                return;
            settingDropDownItems(mMedicineType);

            if (medicineContent[2] == null || medicineContent[2].length() == 0)
                return;
            val medicine_strength = medicineContent[2];

            String[] medicineStrengthArray = medicine_strength.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");

            for (val strength : medicineStrengthArray) {
                Log.d(TAG, "medicineData: " + strength);
            }

            if (medicineStrengthArray == null || medicineStrengthArray.length == 0)
                return;
            if (medicineStrengthArray[0].length() == 0 || medicineContent[0] == null)
                return;
            val medicineStrength = medicineStrengthArray[0];

            if (medicineStrengthArray[1].length() == 0 || medicineContent[1] == null)
                return;
            val medicineStrengthUnit = medicineStrengthArray[1];

            if (TextUtils.isEmpty(din))
                return;

            if (medicineStrengthUnit == null || medicineStrengthUnit.length() == 0)
                return;
            settingDropDownItems(medicineStrengthUnit);

            mBinding.mediName.setText(mMedicineName);
            mBinding.mediStrength.setText(medicineStrength);
        } catch (Exception e) {
            mBinding.mediName.setText(mMedicineName);
            Log.d(TAG, "Exception : " + e);
        }
    }

    //*************************************************************
    private void onUpdate()
    //*************************************************************
    {
        if (Laila.instance().getMUser() == null)
            return;
        val onUpdateMedicine = Laila.instance().on_update_medicine;
        val position = Laila.instance().getMMedicationPosition();
        if (Laila.instance()
                .getMUser()
                .getMedication() == null || Laila.instance()
                .getMUser()
                .getMedication()
                .size() == 0)
            return;
        val updateMedication = Laila.instance().getMUser().getMedication().get(position);

        Laila.instance().setMUpdateMedication(updateMedication);

        val medication = Laila.instance().getMUpdateMedication();

        if (onUpdateMedicine) {
            if (medication == null)
                return;
            val name = medication.getMedicationName();
            val din = medication.getDinRxNumber();
            val from = medication.getMedecineForm();
            val strength = medication.getStrength();
            val strengthUnit = medication.getStrengthUom();
            val prescribed = medication.getPrescribed();
            val overTheCounter = medication.getPrescribed();
            mUpdateMedicineId = medication.getId();

            if (overTheCounter == 0) {
                mBinding.navigateNext.setVisibility(View.GONE);
                mBinding.addBtn.setVisibility(View.VISIBLE);
                mBinding.addBtn.setText(R.string.update);
                mBinding.addBtn.setBackgroundTintList(
                        ContextCompat.getColorStateList(getActivity(), R.color.button_background));
            }

            if (name == null)
                return;
            mBinding.mediName.setText(name);
            if (din == null)
                return;
            if (!TextUtils.isEmpty(from))
                settingDropDownItems(from);
            mBinding.rxDinNumber.setText(din);
            if (strength == null)
                return;
            mBinding.mediStrength.setText(strength);
            if (strengthUnit == null)
                return;
            settingDropDownItems(strengthUnit);
            if (prescribed == 0)
                mBinding.overTheCounter.setChecked(true);
            if (prescribed == 1)
                mBinding.prescribed.setChecked(true);
        }
    }

    //*************************************************************
    private void validation()
    //*************************************************************
    {
        if (AndroidUtil.mTooltip != null)
            AndroidUtil.mTooltip.dismiss();

        val name = mBinding.mediName.getText()
                .toString();
        val din = mBinding.rxDinNumber.getText()
                .toString();
        val strength = mBinding.mediStrength.getText().toString();
        val strengthUnit = mBinding.strengthUnit.getSelectedItem().toString();
        val from = mBinding.formSpin.getSelectedItem().toString();
        val prescribed = mBinding.prescribed.isChecked();
        val overTheCounter = mBinding.overTheCounter.isChecked();

        if (TextUtils.isEmpty(name)) {
            mBinding.mediName.setError(AndroidUtil.getString(R.string.required));
            mBinding.mediName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(din)) {
            mBinding.rxDinNumber.setError(AndroidUtil.getString(R.string.required));
            mBinding.rxDinNumber.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(strength)) {
            mBinding.mediStrength.setError(AndroidUtil.getString(R.string.required));
            mBinding.mediStrength.requestFocus();
            return;
        }

        val addMedicationRequest = Laila.instance()
                .getMAddMedicationRequest().Builder();
        if (mUpdateMedicineId != 0)
            addMedicationRequest.setId(mUpdateMedicineId);
        addMedicationRequest.setMedicationName(name);
        addMedicationRequest.setMedicationDin(din);
        addMedicationRequest.setFrom(from);
        addMedicationRequest.setMedicationStrength(strength);
        addMedicationRequest.setMedicationStrengthUnit(strengthUnit);
        addMedicationRequest.setPrescribed(prescribed ? 1 : 0);

        Laila.instance().setMAddMedicationRequest(addMedicationRequest);

        val onUpdateMedicine = Laila.instance().on_update_medicine;
        if (onUpdateMedicine) {
            val medication = Laila.instance().getMUpdateMedication();
            medication.setMedicationName(name);
            medication.setDinRxNumber(din);
            medication.setMedecineForm(from);
            medication.setStrength(strength);
            medication.setStrengthUom(strengthUnit);
            medication.setPrescribed(prescribed ? 1 : 0);
        }

        if (prescribed) {
            ((AddMedicationActivity) getActivity()).navigateToScreen(1);
            return;
        }
        showLoadingDialog();
        mAddMedicationViewModel.addMedication(addMedicationRequest);
    }

    //*************************************************************
    private void settingDropDownItems(@NonNull String unit)
    //*************************************************************
    {
        ArrayAdapter<CharSequence> strengthUnitAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.strength_unit, android.R.layout.simple_spinner_dropdown_item);
        strengthUnitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.strengthUnit.setAdapter(strengthUnitAdapter);

        String[] unitSplits = unit.split("/");

        if (unitSplits.length == 0 || unitSplits == null)
            return;
        if (unitSplits[0].length() == 0 || unitSplits[0] == null)
            return;

        val strength_unit = unitSplits[0].toLowerCase();

        if (strength_unit != null) {
            int spinnerPosition = strengthUnitAdapter.getPosition(strength_unit);
            mBinding.strengthUnit.setSelection(spinnerPosition);
        }
    }

    //*************************************************************
    @Override
    public void onSuccessfully(@Nullable MedicationResponse response)
    //*************************************************************
    {
        if (response.getMedication() == null) {
            hideLoadingDialog();
            return;
        }
        if (Laila.instance()
                .getMUser()
                .getMedication() == null)
            Laila.instance()
                    .getMUser()
                    .setMedication(new ArrayList<>());

        val onUpdateMedicine = Laila.instance().on_update_medicine;
        if (onUpdateMedicine && mUpdateMedicineId == response.getMedication().getId()) {
            Laila.instance().getMUser().getMedication().set(Laila.instance().getMMedicationPosition(), response.getMedication());
            Laila.instance().on_update_medicine = false;
        } else {
            Laila.instance()
                    .getMUser()
                    .getMedication()
                    .add(0, response.getMedication());
        }

        Laila.instance()
                .setMMedicationId(response.getMedication()
                        .getId());

        SharedPreferencesUtils.setValue(Constants.USER_DATA, Laila.instance().getMUser());
        Laila.instance().is_medicine_added = true;
        goToHome();
    }

    //*************************************************************
    @Override
    public void onFailed(@androidx.annotation.NonNull String errorMessage)
    //*************************************************************
    {
        hideLoadingDialog();
    }

    //*************************************************************
    private void goToHome()
    //*************************************************************
    {
        hideLoadingDialog();
        Intent medicationIntent = new Intent(getContext(), MedicationActivity.class);
        medicationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(medicationIntent);
        getActivity().finish();
    }

}
