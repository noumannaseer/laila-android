package com.fantechlabs.lailaa.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.activities.AddMedicationActivity;
import com.fantechlabs.lailaa.databinding.FragmentAddMedicationTwoBinding;
import com.fantechlabs.lailaa.models.Events;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.DateUtils;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;

import java.util.ArrayList;
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
    private List<Events> mEventsList;

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
        infoClickListener();
        setData();
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
        mBinding.navigateNext.setOnClickListener(v -> {
            if (AndroidUtil.mTooltip != null)
                AndroidUtil.mTooltip.dismiss();
            validation();
        });
        mBinding.disDate.setOnClickListener(v -> datePicker());
        mBinding.navigateBack.setOnClickListener(v ->
        {
            if (AndroidUtil.mTooltip != null)
                AndroidUtil.mTooltip.dismiss();
            ((AddMedicationActivity) getActivity()).navigateToScreen(
                    0);
        });
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
            case R.id.refill_date_info:
                AndroidUtil.showToolTip(v, R.string.no_of_refill_info);
                break;
            case R.id.add_medication_two_main_view:
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

        try {
            val medication = Laila.instance()
                    .getMSearchMedicine();
            if (medication == null)
                return;

            val check = Laila.instance()
                    .getMSearchMedicine()
                    .getBrandName()
                    .contains("%");
            if (check)
                return;

            String[] medicineData = Laila.instance()
                    .getMSearchMedicine()
                    .getBrandName()
                    .split(" ");

            if (medicineData.length == 0)
                return;

            for (val content : medicineData) {
                Log.d(TAG, "Data: " + content);
            }
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

            if (mMedicineType.length() == 0 || mMedicineType == null)
                return;
        } catch (Exception e) {
            Log.d(TAG, "Exception : " + e);
        }

    }

    //***************************************************************
    @Override
    public void onResume()
    //***************************************************************
    {
        super.onResume();

        getActivity().getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        val onUpdateMedicine = Laila.instance().on_update_medicine;
        if (onUpdateMedicine)
            onUpdate();
    }

    //*************************************************************
    private void onUpdate()
    //*************************************************************
    {
        val onUpdateMedicine = Laila.instance().on_update_medicine;
        val medication = Laila.instance().getMUpdateMedication();

        if (onUpdateMedicine) {
            if (medication == null)
                return;
            val medicationId = medication.getId();
            val dispensedDate = medication.getStartDate();
            val dispensedAmount = medication.getAmount();
            val dosage = medication.getNumberOfPills().toString();
            val refillNo = medication.getNumRefills().toString();
            val frequency = medication.getFrequency2();

            if (!TextUtils.isEmpty(dispensedDate))
                mBinding.disDate.setText(dispensedDate);
            if (!TextUtils.isEmpty(dispensedAmount))
                mBinding.disAmount.setText(dispensedAmount);
            if (!TextUtils.isEmpty(dosage))
                mBinding.dosage.setText(dosage);
            if (frequency != null)
                frequencyDropDownItems(frequency);
            if (!TextUtils.isEmpty(refillNo))
                mBinding.noOfRefills.setText(refillNo);

            if (Laila.instance().getMUser() == null || Laila.instance().getMUser().getEvents() == null)
                return;
            val events = Laila.instance().getMUser().getEvents();
            mEventsList = new ArrayList<>();

            for (val event : events) {
                if (event.getMedicationId().equals(medicationId))
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
//        if (!RXCare.instance().is_over_the_counter)
        frequency = mBinding.frequency.getSelectedItem().toString();
        val noRefills = mBinding.noOfRefills.getText()
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
        addMedicationRequest.setNumber_of_pills(dosage);
        addMedicationRequest.setFrequency(frequency);
        addMedicationRequest.setIntakeTimeList(intakeTime);
        addMedicationRequest.setNoOfRefills(noRefills);

        Laila.instance()
                .setMAddMedicationRequest(addMedicationRequest);

        if (onUpdateMedicine) {
            val medication = Laila.instance().getMUpdateMedication();
            medication.setStartDate(disDate);
            medication.setAmount(disAmount);
            medication.setNumberOfPills(Integer.valueOf(dosage));
            medication.setFrequency2(frequency);
            medication.setIntakeTime(intakeTime);
            medication.setNumRefills(Integer.valueOf(noRefills));
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
                                    "dd-MMM-yyyy"));
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

}
