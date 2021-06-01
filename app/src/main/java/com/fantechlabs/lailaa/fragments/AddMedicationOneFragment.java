package com.fantechlabs.lailaa.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.fantechlabs.lailaa.activities.AddMedicationActivity;
import com.fantechlabs.lailaa.activities.MedicationActivity;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.databinding.FragmentAddMedicationOneBinding;
import com.fantechlabs.lailaa.models.updates.request_models.AddMedicationRequest;
import com.fantechlabs.lailaa.models.updates.response_models.MedicationResponse;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;
import com.fantechlabs.lailaa.utils.SharedPreferencesUtils;
import com.fantechlabs.lailaa.view_models.AddMedicationViewModel;

import java.util.ArrayList;
import java.util.List;

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
    private String mMedicineType = "";
    private int mUpdateMedicineId;
    private AddMedicationViewModel mAddMedicationViewModel;
    private AddMedicationRequest mAddMedicationRequest;
    private String mMedicineStrengthUnit;
    private List<String> mFormUnits, mStrengthUnits;

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
        initViewModels();
        dropDownItemsList();
        infoClickListener();
        setData();
        medicationType();
        navigateToNextScreen();
    }



    //***********************************************************
    private void dropDownItemsList()
    //***********************************************************
    {
        mFormUnits.add("Teaspoon");
        mFormUnits.add("Tablet");
        mFormUnits.add("Capsule");
        mFormUnits.add("Drops");
        mFormUnits.add("Injection");
        mFormUnits.add("Inhaler");

        mStrengthUnits.add("mg");
        mStrengthUnits.add("cc");
        mStrengthUnits.add("mcg");
        mStrengthUnits.add("iu");
        mStrengthUnits.add("meq");
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

    //***********************************************************
    private void navigateToNextScreen()
    //***********************************************************
    {
        mBinding.navigateNext.setOnClickListener(v -> validation());
        mBinding.addBtn.setOnClickListener(v -> validation());
    }

    //***********************************************************
    private void initViewModels()
    //***********************************************************
    {
        mFormUnits = new ArrayList<>();
        mStrengthUnits = new ArrayList<>();
        mAddMedicationViewModel = new AddMedicationViewModel(this);
    }

    //***********************************************************
    private void medicationType()
    //***********************************************************
    {
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
        });
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
            case R.id.form_unit_info:
                AndroidUtil.showToolTip(v, R.string.enter_medicine_form_unit);
                break;
            case R.id.strength_unit_info:
                AndroidUtil.showToolTip(v, R.string.strength_info);
                break;
            case R.id.add_medication_one_main_view:
            case R.id.add_medication_one_container:
                if (AndroidUtil.mTooltip != null)
                    AndroidUtil.mTooltip.dismiss();
                break;
        }
    }

    //*************************************************************
    @SuppressLint("SetTextI18n")
    private void setData()
    //*************************************************************
    {
        val onUpdateMedicine = Laila.instance().on_update_medicine;
        if (onUpdateMedicine) {
            onUpdate();
            return;
        }
        val medication = Laila.instance().getMSearchMedicine_U();
        if (medication == null)
            return;
        val medicineName = Laila.instance().getMSearchMedicine_U().getBrandName();
        val title = Laila.instance().getMSearchMedicine_U().getBrandName();


        val din = Laila.instance().getMSearchMedicine_U().getDrugIdentificationNumber();
        if (!TextUtils.isEmpty(din.toString()))
            mBinding.rxDinNumber.setText(din.toString());
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
                .getMSearchMedicine_U()
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
        if (medicineContent.length > 1) {
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
            else
                mMedicineType = "";
            if (mMedicineType.length() != 0)
                setDropDownItems(mBinding.formSpin, mFormUnits, mMedicineType);
        }

        if (medicineContent.length > 2) {
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

            if (medicineStrengthArray.length > 1) {
                mMedicineStrengthUnit = medicineStrengthArray[1];
                if (mMedicineStrengthUnit == null || mMedicineStrengthUnit.length() == 0)
                    return;
                setDropDownItems(mBinding.strengthUnit, mStrengthUnits, mMedicineStrengthUnit);

            }
            if (medicineStrength.matches("[0-9]+"))
                mBinding.mediStrength.setText(medicineStrength);
            else
                mBinding.mediStrength.setText("");
        }

        if (TextUtils.isEmpty(din.toString()))
            return;
        mBinding.mediName.setText(mMedicineName);
    }

    //*************************************************************
    private void setDropDownItems(Spinner spinner, List<String> itemList, String name)
    //*************************************************************
    {
        ArrayAdapter<String> countryListAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, itemList);
        countryListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(countryListAdapter);

        int spinnerPosition = countryListAdapter.getPosition(name);
        spinner.setSelection(spinnerPosition);
    }

    //*************************************************************
    private void onUpdate()
    //*************************************************************
    {
        if (Laila.instance().getMUser_U() == null)
            return;
        val onUpdateMedicine = Laila.instance().on_update_medicine;
        val position = Laila.instance().getMMedicationPosition();
        val medicationList = Laila.instance()
                .getMUser_U()
                .getData()
                .getMedicationList();

        if (medicationList == null || medicationList.size() == 0)
            return;
        val updateMedication = Laila.instance().getMUser_U().getData().getMedicationList().get(position);

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
                settingFormDropDown(from);
            mBinding.rxDinNumber.setText(din.toString());
            if (strength == null)
                return;
            mBinding.mediStrength.setText(strength.toString());
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
    @SuppressLint("SetTextI18n")
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

        if (!strength.matches("\\d+(?:\\.\\d+)?")) {
            mBinding.mediStrength.setError(AndroidUtil.getString(R.string.strength_should_be_only_digits));
            mBinding.mediStrength.requestFocus();
            return;
        }
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

        if (Laila.instance().getMAddMedicationRequest() == null)
            mAddMedicationRequest = Laila.instance()
                    .getMAddMedicationRequest().Builder();
        else
            mAddMedicationRequest = Laila.instance().getMAddMedicationRequest();

        if (mUpdateMedicineId != 0)
            mAddMedicationRequest.setId(mUpdateMedicineId);
        mAddMedicationRequest.setMedicationName(name);
        mAddMedicationRequest.setDinRxNumber(din);
        mAddMedicationRequest.setMedecineForm(from);
        mAddMedicationRequest.setStrength(Integer.parseInt(strength));
        mAddMedicationRequest.setStrengthUom(strengthUnit);
        mAddMedicationRequest.setPrescribed(prescribed ? 1 : 0);

        Laila.instance().setMAddMedicationRequest(mAddMedicationRequest);

        val onUpdateMedicine = Laila.instance().on_update_medicine;
        if (onUpdateMedicine) {
            val medication = Laila.instance().getMUpdateMedication();
            medication.setMedicationName(name);
            medication.setDinRxNumber(din);
            medication.setMedecineForm(from);
            medication.setStrength(Integer.parseInt(strength));
            medication.setStrengthUom(strengthUnit);
            medication.setPrescribed(prescribed ? 1 : 0);
            Laila.instance().setMUpdateMedication(medication);

            if (prescribed) {
                ((AddMedicationActivity) getActivity()).navigateToScreen(1);
                return;
            }
            showLoadingDialog();
            mAddMedicationViewModel.updateMedication(mAddMedicationRequest);
            return;
        }
        if (prescribed) {
            ((AddMedicationActivity) getActivity()).navigateToScreen(1);
            return;
        }
        mAddMedicationViewModel.addMedication(mAddMedicationRequest);
    }

    //*************************************************************
    private void settingFormDropDown(@NonNull String unit)
    //*************************************************************
    {
        ArrayAdapter<CharSequence> strengthUnitAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.from_type, android.R.layout.simple_spinner_dropdown_item);
        strengthUnitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.formSpin.setAdapter(strengthUnitAdapter);
        int spinnerPosition = strengthUnitAdapter.getPosition(unit);
        mBinding.formSpin.setSelection(spinnerPosition);
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
        hideLoadingDialog();
        Laila.instance().setMAddMedicationRequest(null);
        if (response.getData() == null) {
            return;
        }
        val user = Laila.instance().getMUser_U().getData();
        if (user.getMedicationList() == null)
            user.setMedicationList(new ArrayList<>());
        if (response.getData().getMedication() == null) {
            Laila.instance().on_update_medicine = false;
            goToHome();
            return;
        }
        val onUpdateMedicine = Laila.instance().on_update_medicine;
        if (onUpdateMedicine && mUpdateMedicineId == response.getData().getMedication().getId()) {
            user.getMedicationList().set(Laila.instance().getMMedicationPosition(), response.getData().getMedication());
            Laila.instance().on_update_medicine = false;
        } else
            Laila.instance()
                    .getMUser_U()
                    .getData()
                    .getMedicationList()
                    .add(response.getData().getMedication());

        Laila.instance()
                .setMMedicationId(response.getData().getMedication()
                        .getId());

        SharedPreferencesUtils.setValue(Constants.USER_DATA, Laila.instance().getMUser_U());
        Laila.instance().is_medicine_added = true;
        goToHome();
    }

    //*************************************************************
    @Override
    public void onFailed(@androidx.annotation.NonNull String errorMessage)
    //*************************************************************
    {
        hideLoadingDialog();
        AndroidUtil.displayAlertDialog(errorMessage, "Error", getActivity());
    }

    @Override
    public void onSuccessfullyGetMedications(@Nullable MedicationResponse medicationResponse) {

    }

    @Override
    public void onFailedGetMedications(@androidx.annotation.NonNull String errorMessage) {

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

    //*************************************************************
    @SuppressLint("SetTextI18n")
    @Override
    public void onResume()
    //*************************************************************
    {
        super.onResume();
        if (Laila.instance().on_update_medicine) {
            val updateMedication = Laila.instance().getMUpdateMedication();
            if (updateMedication == null)
                return;
            mBinding.mediName.setText(updateMedication.getMedicationName());
            mBinding.rxDinNumber.setText(updateMedication.getDinRxNumber());
            mBinding.mediStrength.setText(updateMedication.getStrength().toString());
            setDropDownItems(mBinding.strengthUnit, mStrengthUnits, updateMedication.getStrengthUom());
            return;
        }
        val medication = Laila.instance().getMAddMedicationRequest();
        if (medication != null) {
            mBinding.mediName.setText(medication.getMedicationName());
            mBinding.rxDinNumber.setText(medication.getDinRxNumber());
            mBinding.mediStrength.setText(medication.getStrength().toString());
            setDropDownItems(mBinding.strengthUnit, mStrengthUnits, medication.getStrengthUom());
            setDropDownItems(mBinding.formSpin, mFormUnits, medication.getMedecineForm());
        }
    }
}
