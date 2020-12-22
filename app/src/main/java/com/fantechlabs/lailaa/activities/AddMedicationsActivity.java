package com.fantechlabs.lailaa.activities;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.databinding.ActivityAddMedicationsBinding;
import com.fantechlabs.lailaa.databinding.ActivityMedicationBinding;
import com.fantechlabs.lailaa.models.response_models.MedicationResponse;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;
import com.fantechlabs.lailaa.utils.DateUtils;
import com.fantechlabs.lailaa.utils.SharedPreferencesUtils;
import com.fantechlabs.lailaa.view_models.AddMedicationViewModel;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;

import static com.fantechlabs.lailaa.utils.AndroidUtil.getContext;

//*******************************************************************
public class AddMedicationsActivity extends BaseActivity
        implements View.OnClickListener,
        AddMedicationViewModel.AddMedicationListener
//*******************************************************************
{
    private ActivityAddMedicationsBinding mBinding;
    private String mMedicineName;
    private String mMedicineType;
    private int mUpdateMedicineId, mItemPosition;
    private AddMedicationViewModel mAddMedicationViewModel;
    private Date mSelectedDate;
    private boolean mUpdateMedicine;
    private MedicationResponse mMedicationResponses;

    //*******************************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //*******************************************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_medications);
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("");
        initControl();
    }

    //***********************************************************
    public void initControl()
    //***********************************************************
    {
        mAddMedicationViewModel = new AddMedicationViewModel(this);
        infoClickListener();
        intakeListeners();
        setData();
        mBinding.addPharmacy.setOnClickListener(v ->
        {
            if (AndroidUtil.mTooltip != null)
                AndroidUtil.mTooltip.dismiss();

            Intent pharmacyIntent = new Intent(
                    getContext(),
                    AddPharmacyActivity.class);
            startActivity(pharmacyIntent);
        });
        mBinding.addMedication.setOnClickListener(view -> validation());
    }

    //***********************************************************
    private void infoClickListener()
    //***********************************************************
    {
        mBinding.nameInfo.setOnClickListener(this);
        mBinding.rxDinInfo.setOnClickListener(this);
        mBinding.strengthInfo.setOnClickListener(this);
        mBinding.strengthUnitInfo.setOnClickListener(this);
        mBinding.amountInfo.setOnClickListener(this);
        mBinding.formUnitInfo.setOnClickListener(this);
        mBinding.reminderInfo.setOnClickListener(this);
        mBinding.refillDateInfo.setOnClickListener(this);
        mBinding.commentsInfo.setOnClickListener(this);
        mBinding.pharmacyInfo.setOnClickListener(this);
        mBinding.dispensedDateInfo.setOnClickListener(this);
        mBinding.dosageInfo.setOnClickListener(this);
        mBinding.reminderInfo.setOnClickListener(this);

    }

    //***********************************************************
    @Override
    protected void onResume()
    //***********************************************************
    {
        super.onResume();
        if (Laila.instance().is_pharmacy_added)
            Laila.instance().is_pharmacy_added = false;

        val onUpdateMedicine = Laila.instance().on_update_medicine;
        if (onUpdateMedicine) {
            onUpdate();
        }
    }

    //*************************************************************
    private void onUpdate()
    //*************************************************************
    {
        mUpdateMedicine = Laila.instance().on_update_medicine;
        mItemPosition = Laila.instance()
                .getMMedicationPosition();
        if (Laila.instance()
                .getMUser()
                .getMedication() == null || Laila.instance()
                .getMUser()
                .getMedication()
                .size() == 0)
            return;
        val updateMedication = Laila.instance().getMUser().getMedication().get(mItemPosition);

        Laila.instance().setMUpdateMedication(updateMedication);

        val medication = Laila.instance().getMUpdateMedication();

        if (mUpdateMedicine) {
            mBinding.addMedication.setText(R.string.update);
            mBinding.addMedication.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, R.color.button_background));

            if (medication == null)
                return;
            mUpdateMedicineId = medication.getId();
            val name = medication.getMedicationName();
            val din = medication.getDinRxNumber();
            val form = medication.getMedecineForm();
            val strength = medication.getStrength();
            val strengthUnit = medication.getStrengthUom();
            val dispensedDate = medication.getStartDate();
            val amount = medication.getAmount();
            val dosage = medication.getNumberOfPills().toString();
            val frequency = medication.getFrequency2();
            val refillDate = medication.getRefillDate();

            if (name != null)
                mBinding.mediName.setText(name);
            if (!TextUtils.isEmpty(din))
                mBinding.rxDinNumber.setText(din);
            if (!TextUtils.isEmpty(form))
                settingDropDownItems(form);
            if (!TextUtils.isEmpty(strength))
                mBinding.mediStrength.setText(strength);
            if (!TextUtils.isEmpty(strengthUnit))
                settingDropDownItems(strengthUnit);
            if (refillDate != null)
                mBinding.refillDate.setText(refillDate);
            if (!TextUtils.isEmpty(dispensedDate))
                mBinding.disDate.setText(dispensedDate);
            if (!TextUtils.isEmpty(amount))
                mBinding.amount.setText(amount);
            if (!TextUtils.isEmpty(dosage))
                mBinding.dosage.setText(dosage);
            if (frequency != null)
                frequencyDropDownItems(frequency);

        }
    }

    //*************************************************************
    private void frequencyDropDownItems(@lombok.NonNull String type)
    //*************************************************************
    {
        ArrayAdapter<CharSequence> strengthUnitAdapter = ArrayAdapter.createFromResource(
                this, R.array.frequency, android.R.layout.simple_spinner_dropdown_item);
        strengthUnitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.frequency.setAdapter(strengthUnitAdapter);

        if (type != null) {
            int spinnerPosition = strengthUnitAdapter.getPosition(type);
            mBinding.frequency.setSelection(spinnerPosition);
        }
    }

    //*************************************************************
    private void setData()
    //*************************************************************
    {
        val disDate = DateUtils.getCurrentDate("dd-MMM-yyyy");
        val time = DateUtils.getCurrentTime("hh:mm a").toUpperCase();
        mBinding.disDate.setText(disDate);
        mBinding.intakeTime1.setText(time);
        mBinding.intakeTime2.setText(time);
        mBinding.intakeTime3.setText(time);
        mBinding.intakeTime4.setText(time);

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
        val amount = mBinding.amount.getText().toString();
        val form = mBinding.formSpin.getSelectedItem().toString();
        val dispensedDate = mBinding.disDate.getText().toString();
        val dosage = mBinding.dosage.getText().toString();
        val refillDate = mBinding.refillDate.getText()
                .toString();
        val intakeTime1 = mBinding.intakeTime1.getText()
                .toString();
        val intakeTime2 = mBinding.intakeTime2.getText()
                .toString();
        val intakeTime3 = mBinding.intakeTime3.getText()
                .toString();
        val intakeTime4 = mBinding.intakeTime4.getText()
                .toString();
        String frequency = "";
        frequency = mBinding.frequency.getSelectedItem().toString();


        List<String> intakeTime = new ArrayList<>();


        if (TextUtils.isEmpty(intakeTime1)
                && mBinding.intake1.getVisibility() == View.VISIBLE) {
            AndroidUtil.displayAlertDialog("Intake time 1 is required", AndroidUtil.getString(R.string.alert), this);
            return;
        }
        if (!TextUtils.isEmpty(intakeTime1))
            intakeTime.add(intakeTime1);

        if (TextUtils.isEmpty(intakeTime2)
                && mBinding.intake2.getVisibility() == View.VISIBLE) {
            AndroidUtil.displayAlertDialog("Intake time 2 is required", AndroidUtil.getString(R.string.alert), this);
            return;
        }
        if (!TextUtils.isEmpty(intakeTime2))
            intakeTime.add(intakeTime2);

        if (TextUtils.isEmpty(intakeTime3)
                && mBinding.intake3.getVisibility() == View.VISIBLE) {
            AndroidUtil.displayAlertDialog("Intake time 3 is required", AndroidUtil.getString(R.string.alert), this);
            return;
        }
        if (!TextUtils.isEmpty(intakeTime3))
            intakeTime.add(intakeTime3);

        if (TextUtils.isEmpty(intakeTime4)
                && mBinding.intake4.getVisibility() == View.VISIBLE) {
            AndroidUtil.displayAlertDialog("Intake time 4 is required", AndroidUtil.getString(R.string.alert), this);
            return;
        }
        if (!TextUtils.isEmpty(intakeTime4))
            intakeTime.add(intakeTime4);

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
        if (TextUtils.isEmpty(amount)) {
            mBinding.amount.setError(AndroidUtil.getString(R.string.required));
            mBinding.amount.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(dosage)) {
            mBinding.dosage.setError(AndroidUtil.getString(R.string.required));
            mBinding.dosage.requestFocus();
            return;
        }
        val addMedicationRequest = Laila.instance()
                .getMAddMedicationRequest().Builder();
        if (mUpdateMedicineId != 0)
            addMedicationRequest.setId(mUpdateMedicineId);
        addMedicationRequest.setMedicationName(name);
        addMedicationRequest.setMedicationDin(din);
        addMedicationRequest.setFrom(form);
        addMedicationRequest.setMedicationStrength(strength);
        addMedicationRequest.setMedicationStrengthUnit(strengthUnit);
        addMedicationRequest.setDispensedAmount(amount);
        addMedicationRequest.setRefillDate(refillDate);
        addMedicationRequest.setDispensedDate(dispensedDate);
        addMedicationRequest.setNumber_of_pills(dosage);
        addMedicationRequest.setFrequency(frequency);
        addMedicationRequest.setIntakeTimeList(intakeTime);

        val dosageInInteger = stringToInteger(dosage);
        val dispensedAmount = stringToInteger(amount);
        val frequencySet = stringToInteger(frequency);
        if (dosageInInteger == 0 || dispensedAmount == 0 || frequencySet == 0)
            return;
        val refillDays = dispensedAmount / (dosageInInteger * frequencySet);
        val date = addDaysToDate(dispensedDate, refillDays);
        mBinding.refillDate.setText(date);


        val onUpdateMedicine = Laila.instance().on_update_medicine;
        if (onUpdateMedicine) {
            val medication = Laila.instance().getMUpdateMedication();
            medication.setMedicationName(name);
            medication.setDinRxNumber(din);
            medication.setMedecineForm(form);
            medication.setStrength(strength);
            medication.setStrengthUom(strengthUnit);
            medication.setStartDate(dispensedDate);
            medication.setAmount(amount);
            medication.setNumberOfPills(Integer.parseInt(dosage));
            medication.setFrequency2(frequency);
            medication.setIntakeTime(intakeTime);
            medication.setRefillDate(refillDate);
        }
        if (mUpdateMedicine) {
            if (Laila.instance()
                    .getMUser()
                    .getMedication() == null || Laila.instance()
                    .getMUser()
                    .getMedication()
                    .size() == 0)
                return;
            val medication = Laila.instance()
                    .getMUser()
                    .getMedication()
                    .get(mItemPosition);
            mUpdateMedicineId = medication.getId();
            addMedicationRequest.setId(mUpdateMedicineId);
        }

        Laila.instance().setMAddMedicationRequest(addMedicationRequest);
        showLoadingDialog();
        mAddMedicationViewModel.addMedication(addMedicationRequest);
    }


    //******************************************************************
    int stringToInteger(@androidx.annotation.NonNull String num)
    //******************************************************************
    {
        int myNum = 0;
        try {
            myNum = Integer.parseInt(num);
        } catch (NumberFormatException e) {
        }
        return myNum;
    }

    //*****************************************************
    @SneakyThrows
    private String addDaysToDate(@androidx.annotation.NonNull String date, @androidx.annotation.NonNull int days)
    //*****************************************************
    {
        String dateInString = date;  // Start date
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(dateInString));
        c.add(Calendar.DATE, days);
        Date resultdate = new Date(c.getTimeInMillis());
        dateInString = format.format(resultdate);
        System.out.println("String date:" + dateInString);
        return dateInString;
    }

    //*************************************************************
    private void intakeListeners()
    //*************************************************************
    {
        mBinding.intake1.setOnClickListener(V -> {
            timePicker(mBinding.intakeTime1);
        });
        mBinding.intake2.setOnClickListener(V -> {
            timePicker(mBinding.intakeTime2);
        });
        mBinding.intake3.setOnClickListener(V -> {
            timePicker(mBinding.intakeTime3);
        });
        mBinding.intake4.setOnClickListener(V -> {
            timePicker(mBinding.intakeTime4);
        });
        mBinding.intake1.setVisibility(View.VISIBLE);
        mBinding.intake2.setVisibility(View.GONE);
        mBinding.intake3.setVisibility(View.GONE);
        mBinding.intake4.setVisibility(View.GONE);
        intakeTimes();

        mBinding.disDate.setOnClickListener(v -> datePicker());

    }

    //*************************************************************
    private void intakeTimes()
    //*************************************************************
    {

        mBinding.frequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //*************************************************************
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            //*************************************************************
            {
                val frequency = parent.getItemAtPosition(position)
                        .toString();

                switch (frequency) {

                    case "1":
                        mBinding.intake1.setVisibility(View.VISIBLE);
                        mBinding.intake2.setVisibility(View.GONE);
                        mBinding.intake3.setVisibility(View.GONE);
                        mBinding.intake4.setVisibility(View.GONE);
                        break;
                    case "2":
                        mBinding.intake1.setVisibility(View.VISIBLE);
                        mBinding.intake2.setVisibility(View.VISIBLE);
                        mBinding.intake3.setVisibility(View.GONE);
                        mBinding.intake4.setVisibility(View.GONE);
                        break;
                    case "3":
                        mBinding.intake1.setVisibility(View.VISIBLE);
                        mBinding.intake2.setVisibility(View.VISIBLE);
                        mBinding.intake3.setVisibility(View.VISIBLE);
                        mBinding.intake4.setVisibility(View.GONE);
                        break;
                    case "4":
                        mBinding.intake1.setVisibility(View.VISIBLE);
                        mBinding.intake2.setVisibility(View.VISIBLE);
                        mBinding.intake3.setVisibility(View.VISIBLE);
                        mBinding.intake4.setVisibility(View.VISIBLE);
                        break;
                    default:
                        mBinding.intake1.setVisibility(View.GONE);
                        mBinding.intake2.setVisibility(View.GONE);
                        mBinding.intake3.setVisibility(View.GONE);
                        mBinding.intake4.setVisibility(View.GONE);
                        break;
                }
            }

            //*************************************************************
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            //*************************************************************
            {

            }
        });
    }


    //*******************************************
    private void timePicker(TextView intakeTime)
    //*******************************************
    {
        new SingleDateAndTimePickerDialog.Builder(this)
                .bottomSheet()
                .curved()
                .displayMinutes(true)
                .displayHours(true)
                .displayDays(false)
                .displayMonth(false)
                .displayYears(false)
                .displayDaysOfMonth(false)
                .listener(date ->
                {
                    mSelectedDate = date;
                    intakeTime.setText(
                            DateUtils.getDate(mSelectedDate.getTime(),
                                    "hh:mm a").toUpperCase());
                })
                .display();
    }

    //*******************************************
    private void datePicker()
    //*******************************************
    {
        new SingleDateAndTimePickerDialog.Builder(this)
                .bottomSheet()
                .curved()
                .displayMinutes(false)
                .displayHours(false)
                .displayDays(false)
                .displayMonth(true)
                .displayYears(true)
                .displayDaysOfMonth(true)
                .minDateRange(new Date())
                .listener(date ->
                {
                    mSelectedDate = date;
                    mBinding.disDate.setText(
                            DateUtils.getDate(mSelectedDate.getTime(),
                                    "dd-MMM-yyyy"));
                })
                .display();
    }

    //*************************************************************
    private void settingDropDownItems(@NonNull String unit)
    //*************************************************************
    {
        ArrayAdapter<CharSequence> strengthUnitAdapter = ArrayAdapter.createFromResource(this, R.array.strength_unit, android.R.layout.simple_spinner_dropdown_item);
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

    //*******************************************************************
    @Override
    protected boolean showStatusBar()
    //*******************************************************************
    {
        return false;
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
            case R.id.strength_unit_info:
                AndroidUtil.showToolTip(v, R.string.strength_info);
                break;
            case R.id.dispensed_date_info:
                AndroidUtil.showToolTip(v, R.string.dispensed_date_info);
                break;
            case R.id.amount_info:
                AndroidUtil.showToolTip(v, R.string.dispensed_amount_info);
                break;
            case R.id.dosage_info:
                AndroidUtil.showToolTip(v, R.string.dosage_info);
                break;
            case R.id.refill_date_info:
                AndroidUtil.showToolTip(v, R.string.refill_date_info);
                break;

            case R.id.add_medication_container:
                if (AndroidUtil.mTooltip != null)
                    AndroidUtil.mTooltip.dismiss();
                break;
        }
    }

    //*************************************************************
    @Override
    public void onSuccessfully(@Nullable MedicationResponse response)
    //*************************************************************
    {
        hideLoadingDialog();
        if (response.getMedication() == null) {
            return;
        }
        mMedicationResponses = response;

        if (Laila.instance()
                .getMUser()
                .getMedication() == null)
            Laila.instance()
                    .getMUser()
                    .setMedication(new ArrayList<>());

        if (mUpdateMedicine && mUpdateMedicineId == mMedicationResponses.getMedication().getId()) {
            Laila.instance().getMUser().getMedication().set(Laila.instance().getMMedicationPosition(), mMedicationResponses.getMedication());
            Laila.instance().on_update_medicine = false;

        } else {
            Laila.instance()
                    .getMUser()
                    .getMedication()
                    .add(0, mMedicationResponses.getMedication());
        }

        Laila.instance()
                .setMMedicationId(response.getMedication()
                        .getId());

        SharedPreferencesUtils.setValue(Constants.USER_DATA, Laila.instance().getMUser());
        Laila.instance().is_medicine_added = true;
        gotoViewMedications();
    }

    //*************************************************************
    private void gotoViewMedications()
    //*************************************************************
    {
        hideLoadingDialog();
        Intent medicationIntent = new Intent(this, MedicationActivity.class);
        medicationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(medicationIntent);
    }

    //*************************************************************
    @Override
    public void onFailed(@androidx.annotation.NonNull String errorMessage)
    //*************************************************************
    {
        hideLoadingDialog();
        AndroidUtil.displayAlertDialog(errorMessage, AndroidUtil.getString(R.string.error), this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Laila.instance().on_update_medicine = false;
    }
}