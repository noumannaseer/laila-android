package com.fantechlabs.lailaa.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.activities.AddMedicationActivity;
import com.fantechlabs.lailaa.databinding.FragmentAddMedicationTwoBinding;
import com.fantechlabs.lailaa.models.Events;
import com.fantechlabs.lailaa.models.updates.models.Medication;
import com.fantechlabs.lailaa.models.updates.models.ResponseEvent;
import com.fantechlabs.lailaa.models.updates.request_models.AddMedicationRequest;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;
import com.fantechlabs.lailaa.utils.DateUtils;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lombok.val;

//***********************************************************
public class AddMedicationTwoFragment
        extends BaseFragment implements View.OnClickListener
//***********************************************************

{
    public static final String TAG = "medicine";
    private FragmentAddMedicationTwoBinding mBinding;
    private View mRootView;
    private Date mSelectedDate;
    private String mMedicineType;
    private List<ResponseEvent> mEventsList;
    private AddMedicationRequest mAddMedicationRequestCopy;
    private boolean checkCurrentStateData = true;

    //***********************************************************
    public AddMedicationTwoFragment()
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
            mBinding = FragmentAddMedicationTwoBinding.inflate(inflater, parent, false);
            mRootView = mBinding.getRoot();
            initControl();
        }
        return mRootView;
    }

    //***********************************************************
    private void initControl()
    //***********************************************************
    {
        mAddMedicationRequestCopy = new AddMedicationRequest();
        if (!checkCurrentStateData)
            editTextWatcher();
        infoClickListener();
        setData();
        timePickerListener();
        intakeTimes();
        navigateToScreens();
        datePickerListener();
    }

    //***********************************************************
    private void navigateToScreens()
    //***********************************************************
    {
        mBinding.navigateNext.setOnClickListener(v -> {
            if (AndroidUtil.mTooltip != null)
                AndroidUtil.mTooltip.dismiss();
            validation();
        });
        mBinding.navigateBack.setOnClickListener(v ->
        {
            if (AndroidUtil.mTooltip != null)
                AndroidUtil.mTooltip.dismiss();
            ((AddMedicationActivity) getActivity()).navigateToScreen(
                    0);
        });
    }

    //***********************************************************
    private void datePickerListener()
    //***********************************************************
    {
        mBinding.disDate.setOnClickListener(v -> datePicker());
    }

    //***********************************************************
    private void infoClickListener()
    //***********************************************************
    {
        mBinding.dispensedDateInfo.setOnClickListener(this);
        mBinding.amountInfo.setOnClickListener(this);
        mBinding.dosageInfo.setOnClickListener(this);
        mBinding.refillInfo.setOnClickListener(this);
        mBinding.addMedicationTwoMainView.setOnClickListener(this);
    }

    //***********************************************************
    private void timePickerListener()
    //***********************************************************
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
    }

    //*************************************************************
    @Override
    public void onClick(View v)
    //*************************************************************
    {
        switch (v.getId()) {
            case R.id.dispensed_date_info:
                AndroidUtil.showToolTip(v, R.string.dispensed_date_info);
                break;
            case R.id.amount_info:
                AndroidUtil.showToolTip(v, R.string.dispensed_amount_info);
                break;
            case R.id.dosage_info:
                AndroidUtil.showToolTip(v, R.string.dosage_info);
                break;
            case R.id.refill_info:
                AndroidUtil.showToolTip(v, R.string.no_of_refill_info);
                break;
            case R.id.add_medication_two_main_view:
            case R.id.add_medication_two_container:
                if (AndroidUtil.mTooltip != null)
                    AndroidUtil.mTooltip.dismiss();
                break;
        }
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
        new SingleDateAndTimePickerDialog.Builder(getActivity())
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

        val medication = Laila.instance()
                .getMSearchMedicine_U();
        if (medication == null)
            return;

        val check = Laila.instance()
                .getMSearchMedicine_U()
                .getBrandName()
                .contains("%");
        if (check)
            return;

        String[] medicineData = Laila.instance()
                .getMSearchMedicine_U()
                .getBrandName()
                .split(" ");

        if (medicineData.length == 0)
            return;

        for (val content : medicineData) {
            Log.d(TAG, "Data: " + content);
        }
        if (medicineData.length > 1) {
            if (medicineData[1].length() == 0 || medicineData[1] == null)
                return;
            val medicineType = medicineData[1].toLowerCase();

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
        }
    }

    //***************************************************************
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    //***************************************************************
    {
        if (isVisibleToUser) {
            val onUpdateMedicine = Laila.instance().on_update_medicine;
            if (onUpdateMedicine)
                onUpdate();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    //*************************************************************
    private void onUpdate()
    //*************************************************************
    {
        val onUpdateMedicine = Laila.instance().on_update_medicine;
        val updateMedication = Laila.instance().getMUpdateMedication();

        if (onUpdateMedicine) {
            if (updateMedication == null)
                return;
            val medicationId = updateMedication.getId();
            val dispensedDate = updateMedication.getDispensedDate();
            val dispensedAmount = updateMedication.getDispensedAmount();
            val dosage = updateMedication.getDosage().toString();
            val refillNo = updateMedication.getNumRefills().toString();
            val frequency = updateMedication.getFrequency();

            String disDate = "";

            if (Laila.instance().from_third_screen) {
                disDate = DateUtils.getUpdateDateFromTimeStamp(dispensedDate, "dd-MMM-yyyy");
                Laila.instance().from_third_screen = false;
            } else
                disDate = DateUtils.getDateFromTimeStamp(dispensedDate, "dd-MMM-yyyy");

            mBinding.disDate.setText("");
            mBinding.disDate.setText(disDate);
            if (!TextUtils.isEmpty(dispensedAmount))
                mBinding.disAmount.setText(dispensedAmount);
            if (!TextUtils.isEmpty(dosage))
                mBinding.dosage.setText(dosage);
            if (frequency != null)
                frequencyDropDownItems(frequency);
            if (!TextUtils.isEmpty(refillNo))
                mBinding.noOfRefills.setText(refillNo);

            if (Laila.instance().getMUser_U() == null || Laila.instance().getMUser_U().getData().getEventsList() == null)
                return;
            val events = Laila.instance().getMUser_U().getData().getEventsList();
            mEventsList = new ArrayList<>();

            for (val event : events) {
                if (event.getMedicationId() != null)
                    if (event.getMedicationId().equals(Integer.toString(medicationId)))
                        mEventsList.add(event);
            }
            if (mEventsList == null || mEventsList.size() == 0)
                return;
            val size = mEventsList.size();
            switch (size) {
                case 1:
                    mBinding.intakeTime1.setText(mEventsList.get(0).getTimeSchedule());
                    break;
                case 2:
                    mBinding.intakeTime1.setText(mEventsList.get(0).getTimeSchedule());
                    mBinding.intakeTime2.setText(mEventsList.get(1).getTimeSchedule());
                    break;
                case 3:
                    mBinding.intakeTime1.setText(mEventsList.get(0).getTimeSchedule());
                    mBinding.intakeTime2.setText(mEventsList.get(1).getTimeSchedule());
                    mBinding.intakeTime3.setText(mEventsList.get(2).getTimeSchedule());
                    break;
                case 4:
                    mBinding.intakeTime1.setText(mEventsList.get(0).getTimeSchedule());
                    mBinding.intakeTime2.setText(mEventsList.get(1).getTimeSchedule());
                    mBinding.intakeTime3.setText(mEventsList.get(2).getTimeSchedule());
                    mBinding.intakeTime4.setText(mEventsList.get(3).getTimeSchedule());
                    break;
            }
        }
    }

    //*************************************************************
    private void validation()
    //*************************************************************

    {
        String frequency = "";
        val disDate = mBinding.disDate.getText()
                .toString();
        val disAmount = mBinding.disAmount.getText()
                .toString();
        val dosage = mBinding.dosage.getText()
                .toString();
        frequency = mBinding.frequency.getSelectedItem().toString();
        String noRefills = mBinding.noOfRefills.getText()
                .toString();

        val onUpdateMedicine = Laila.instance().on_update_medicine;
        List<String> intakeTime = new ArrayList<>();

        if (TextUtils.isEmpty(disDate)) {
            AndroidUtil.toast(false, "Dispense date is required");
            return;
        }
        if (TextUtils.isEmpty(disAmount)) {
            mBinding.disAmount.setError(AndroidUtil.getString(R.string.required));
            mBinding.disAmount.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(dosage)) {
            mBinding.dosage.setError(AndroidUtil.getString(R.string.required));
            mBinding.dosage.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(noRefills)) {
            mBinding.noOfRefills.setError(AndroidUtil.getString(R.string.required));
            mBinding.noOfRefills.requestFocus();
            return;
        }

        val intakeTime1 = mBinding.intakeTime1.getText()
                .toString();
        val intakeTime2 = mBinding.intakeTime2.getText()
                .toString();
        val intakeTime3 = mBinding.intakeTime3.getText()
                .toString();
        val intakeTime4 = mBinding.intakeTime4.getText()
                .toString();

        if (TextUtils.isEmpty(intakeTime1)
                && mBinding.intake1.getVisibility() == View.VISIBLE) {
            AndroidUtil.displayAlertDialog("Intake time 1 is required", AndroidUtil.getString(R.string.alert), getActivity());
            return;
        }
        if (!TextUtils.isEmpty(intakeTime1))
            intakeTime.add(intakeTime1);

        if (TextUtils.isEmpty(intakeTime2)
                && mBinding.intake2.getVisibility() == View.VISIBLE) {
            AndroidUtil.displayAlertDialog("Intake time 2 is required", AndroidUtil.getString(R.string.alert), getActivity());
            return;
        }
        if (!TextUtils.isEmpty(intakeTime2))
            intakeTime.add(intakeTime2);

        if (TextUtils.isEmpty(intakeTime3)
                && mBinding.intake3.getVisibility() == View.VISIBLE) {
            AndroidUtil.displayAlertDialog("Intake time 3 is required", AndroidUtil.getString(R.string.alert), getActivity());
            return;
        }
        if (!TextUtils.isEmpty(intakeTime3))
            intakeTime.add(intakeTime3);

        if (TextUtils.isEmpty(intakeTime4)
                && mBinding.intake4.getVisibility() == View.VISIBLE) {
            AndroidUtil.displayAlertDialog("Intake time 4 is required", AndroidUtil.getString(R.string.alert), getActivity());
            return;
        }
        if (!TextUtils.isEmpty(intakeTime4))
            intakeTime.add(intakeTime4);

        val addMedicationRequest = Laila.instance()
                .getMAddMedicationRequest();
        addMedicationRequest.setDispensedDate(disDate);
        addMedicationRequest.setDispensedAmount(disAmount);
        addMedicationRequest.setDosage(Integer.parseInt(dosage));
        addMedicationRequest.setFrequency(frequency);
        addMedicationRequest.setIntakeTimeList(intakeTime);
        if (noRefills.isEmpty())
            noRefills = "0";
        addMedicationRequest.setNumRefills(Integer.parseInt(noRefills));
        Laila.instance()
                .setMAddMedicationRequest(addMedicationRequest);

        if (onUpdateMedicine) {
            val dispensedLongDate = DateUtils.getDateFromStringFormat(disDate, Constants.DATE_FORMAT).getTime();
            val updateMedication = Laila.instance().getMUpdateMedication();

            Gson gson = new Gson();
            String json = gson.toJson(updateMedication);
            Medication medicationClone = gson.fromJson(json, Medication.class);
            Laila.instance().setMUpdateMedicationClone(medicationClone);

            updateMedication.setDispensedDate(dispensedLongDate);
            updateMedication.setDispensedAmount(disAmount);
            updateMedication.setDosage(Integer.valueOf(dosage));
            updateMedication.setFrequency(frequency);
            updateMedication.setIntakeTimeList(intakeTime);
            updateMedication.setNumRefills(Integer.valueOf(noRefills));
            Laila.instance().setMUpdateMedication(updateMedication);
        }
        ((AddMedicationActivity) getActivity()).navigateToScreen(2);
    }

    //*******************************************
    private void datePicker()
    //*******************************************
    {
        new SingleDateAndTimePickerDialog.Builder(getActivity())
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
                                    Constants.DATE_FORMAT));
                })
                .display();
    }

    //*************************************************************
    private void frequencyDropDownItems(@lombok.NonNull String type)
    //*************************************************************
    {
        ArrayAdapter<CharSequence> strengthUnitAdapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.frequency, android.R.layout.simple_spinner_dropdown_item);
        strengthUnitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.frequency.setAdapter(strengthUnitAdapter);

        if (type != null) {
            int spinnerPosition = strengthUnitAdapter.getPosition(type);
            mBinding.frequency.setSelection(spinnerPosition);
        }
    }

    //*************************************************************
    @SuppressLint("SetTextI18n")
    @Override
    public void onResume()
    //*************************************************************
    {
        super.onResume();
        if (!checkCurrentStateData)
            return;
        checkCurrentStateData = false;
        editTextWatcher();
        val medication = Laila.instance().getMAddMedicationRequestCopy();
        if (medication == null)
            return;
        if (Laila.instance().on_update_medicine) {
            val updateMedication = Laila.instance().getMUpdateMedication();
            mBinding.disAmount.setText(updateMedication.getDispensedAmount());
            mBinding.dosage.setText(updateMedication.getDosage().toString());
            mBinding.noOfRefills.setText(updateMedication.getNumRefills().toString());
            return;
        }
        if (medication.getDispensedDate() != null)
            mBinding.disDate.setText(medication.getDispensedDate());
        mBinding.disAmount.setText(medication.getDispensedAmount());
        if (medication.getDosage() != null)
            mBinding.dosage.setText(medication.getDosage().toString());
        if (medication.getNumRefills() != null)
            mBinding.noOfRefills.setText(medication.getNumRefills().toString());

    }

    //***********************************************************
    private void editTextWatcher()
    //***********************************************************
    {
        mBinding.disDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mAddMedicationRequestCopy.setDispensedDate(s.toString());
                Laila.instance().setMAddMedicationRequestCopy(mAddMedicationRequestCopy);
            }
        });
        mBinding.disAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mAddMedicationRequestCopy.setDispensedAmount(s.toString());
                Laila.instance().setMAddMedicationRequestCopy(mAddMedicationRequestCopy);
            }
        });
        mBinding.dosage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mAddMedicationRequestCopy.setDosage(Integer.valueOf(s.toString()));
                Laila.instance().setMAddMedicationRequestCopy(mAddMedicationRequestCopy);
            }
        });
        mBinding.frequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mAddMedicationRequestCopy.setFrequency(parent.getSelectedItem().toString());
                Laila.instance().setMAddMedicationRequestCopy(mAddMedicationRequestCopy);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mBinding.noOfRefills.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mAddMedicationRequestCopy.setNumRefills(Integer.valueOf(s.toString()));
                Laila.instance().setMAddMedicationRequestCopy(mAddMedicationRequestCopy);
            }
        });
    }
}
