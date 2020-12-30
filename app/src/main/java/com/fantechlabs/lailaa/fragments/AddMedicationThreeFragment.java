package com.fantechlabs.lailaa.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.activities.AddMedicationActivity;
import com.fantechlabs.lailaa.activities.AddPharmacyActivity;
import com.fantechlabs.lailaa.activities.MedicationActivity;
import com.fantechlabs.lailaa.databinding.FragmentAddMedicationThreeBinding;
import com.fantechlabs.lailaa.models.Events;
import com.fantechlabs.lailaa.models.response_models.MedicationResponse;
import com.fantechlabs.lailaa.models.response_models.MedicineEventResponse;
import com.fantechlabs.lailaa.request_models.AddEventRequest;
import com.fantechlabs.lailaa.request_models.AddMedicineEventRequest;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;
import com.fantechlabs.lailaa.utils.SharedPreferencesUtils;
import com.fantechlabs.lailaa.view_models.AddMedicationViewModel;
import com.fantechlabs.lailaa.view_models.MedicineEventViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lombok.SneakyThrows;
import lombok.val;

import static com.fantechlabs.lailaa.utils.Constants.SELECT_PHARMACY;

//***********************************************************
public class AddMedicationThreeFragment
        extends BaseFragment
        implements AddMedicationViewModel.AddMedicationListener,
        MedicineEventViewModel.MedicineEventCompleteListener,
        View.OnClickListener
//***********************************************************
{

    private FragmentAddMedicationThreeBinding mBinding;
    private View mRootView;
    private AddMedicationViewModel mAddMedicationViewModel;
    private ArrayList<String> mPharmacyList;
    private MedicationResponse mMedicationResponses;
    public static String TAG = "refillDate";
    private int mUpdateMedicineId, mItemPosition;
    private boolean mUpdateMedicine;
    private MedicineEventViewModel mMedicineEventViewModel;
    private boolean mDeliveryType;


    //***********************************************************
    public AddMedicationThreeFragment()
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
            mBinding = FragmentAddMedicationThreeBinding.inflate(inflater, parent, false);
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
        mAddMedicationViewModel = new AddMedicationViewModel(this);
        mMedicineEventViewModel = new MedicineEventViewModel(this);
        mBinding.saveBtn.setOnClickListener(v -> validation());

        setDeliveryType();

        mBinding.addPharmacy.setOnClickListener(v ->
        {
            if (AndroidUtil.mTooltip != null)
                AndroidUtil.mTooltip.dismiss();

            Intent pharmacyIntent = new Intent(
                    getContext(),
                    AddPharmacyActivity.class);
            startActivity(pharmacyIntent);
        });
        mBinding.navigateBack.setOnClickListener(v ->
        {
            if (AndroidUtil.mTooltip != null)
                AndroidUtil.mTooltip.dismiss();
            ((AddMedicationActivity) getActivity()).navigateToScreen(1);
        });

    }

    //***********************************************************
    private void infoClickListener()
    //***********************************************************
    {
        mBinding.refillDateInfo.setOnClickListener(this);
        mBinding.addMedicationThreeMainView.setOnClickListener(this);
    }

    //*************************************************************
    @Override
    public void onClick(View v)
    //*************************************************************
    {
        switch (v.getId()) {
            case R.id.refill_date_info:
                AndroidUtil.showToolTip(v, R.string.refill_date_info);
                break;
            case R.id.add_medication_three_main_view:
                if (AndroidUtil.mTooltip != null)
                    AndroidUtil.mTooltip.dismiss();
                break;
        }
    }

    //*************************************************************
    private void setDeliveryType()
    //*************************************************************
    {

        mBinding.pharmacy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            //*************************************************************
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            //*************************************************************
            {
                val itemPosition = mBinding.pharmacy.getSelectedItemPosition();
                if (Laila.instance()
                        .getMUser() == null || Laila.instance()
                        .getMUser()
                        .getContacts() == null)
                    return;
                if (itemPosition == 0)
                    return;
                val preferredPharmacy = Laila.instance()
                        .getMUser()
                        .getContacts()
                        .get(itemPosition - 1)
                        .getIs_preferred();

                mBinding.medicineDelivery.setVisibility(!TextUtils.isEmpty(preferredPharmacy) ? preferredPharmacy.equals("true") ? View.VISIBLE : View.GONE : View.GONE);
                mDeliveryType = !TextUtils.isEmpty(preferredPharmacy) ? preferredPharmacy.equals("true") ? true : false : false;

            }

            //*************************************************************
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            //*************************************************************
            {

            }
        });
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

        val medication = Laila.instance().getMUpdateMedication();

        if (mUpdateMedicine) {
            mBinding.saveBtn.setText(R.string.update);
            mBinding.saveBtn.setBackgroundTintList(
                    ContextCompat.getColorStateList(getActivity(), R.color.button_background));
            if (medication == null)
                return;

            val pharmacy = medication.getPharmacy();
            val deliverType = medication.getDelivery_type();

            mBinding.pickup.setChecked(deliverType == 1 ? false : true);
            mBinding.delivery.setChecked(deliverType == 1 ? true : false);
            setPharmacyToDropDown(pharmacy);
        }
    }

    //*************************************************************
    private void setPharmacyToDropDown(int id)
    //*************************************************************
    {
        ArrayList<String> pharmacyNameList = new ArrayList<>();
        String name = "";

        if (Laila.instance().getMUser() == null || Laila.instance().getMUser().getContacts() == null)
            return;
        val pharmacyList = Laila.instance().getMUser().getContacts();
        pharmacyNameList.add(SELECT_PHARMACY);
        for (val pharmacy : pharmacyList) {
            if (pharmacy.getId().equals(id))
                name = pharmacy.getFirstName();
            pharmacyNameList.add(pharmacy.getFirstName());
        }
        if (TextUtils.isEmpty(name) && id == 0) {
            setPharmacySpinner();
            return;
        }
        pharmacyDropDownItems(pharmacyNameList, name);
    }

    //*************************************************************
    private void validation()
    //*************************************************************
    {
        if (AndroidUtil.mTooltip != null)
            AndroidUtil.mTooltip.dismiss();

        val addMedicationRequest = Laila.instance().getMAddMedicationRequest();
        val prescribed = addMedicationRequest.getPrescribed();

        val deliveryType = mBinding.pickup.isChecked();

        val refillDate = mBinding.refillDate.getText().toString();
        String pharmacyId = "";

        val position = mBinding.pharmacy.getSelectedItemPosition();
        val pharmacyText = mBinding.pharmacy.getSelectedItem().toString();
        if (prescribed == 1)
            if (TextUtils.isEmpty(pharmacyText) || pharmacyText.equals(SELECT_PHARMACY)) {
                AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.pharmacy_required), AndroidUtil.getString(R.string.alert), getActivity());
                return;
            }

        if (Laila.instance()
                .getMUser() == null || Laila.instance()
                .getMUser()
                .getContacts() == null)
            return;
        if (position > 0) {
            pharmacyId = Laila.instance()
                    .getMUser()
                    .getContacts()
                    .get(position - 1)
                    .getId()
                    .toString();
        }

        addMedicationRequest.setPharmacy(pharmacyId);
        addMedicationRequest.setRefillDate(refillDate);

        if (mDeliveryType)
            addMedicationRequest.setDeliveryType(deliveryType ? 0 : 1);

        val onUpdateMedicine = Laila.instance().on_update_medicine;
        if (onUpdateMedicine) {
            val medication = Laila.instance().getMUpdateMedication();
            if (!TextUtils.isEmpty(pharmacyId))
                medication.setPharmacy(Integer.valueOf(pharmacyId));
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

        Laila.instance()
                .setMAddMedicationRequest(addMedicationRequest);

        showLoadingDialog();

        mAddMedicationViewModel.addMedication(addMedicationRequest);
    }

    //*************************************************************
    @Override
    public void onResume()
    //*************************************************************
    {
        super.onResume();
        getActivity().getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        refillDate();

        if (Laila.instance().is_pharmacy_added)
            Laila.instance().is_pharmacy_added = false;

        val onUpdateMedicine = Laila.instance().on_update_medicine;
        if (onUpdateMedicine) {
            onUpdate();
            return;
        }
        setPharmacySpinner();
    }

    //*************************************************************
    private void setPharmacySpinner()
    //*************************************************************
    {
        mPharmacyList = new ArrayList<>();
        mPharmacyList.add(SELECT_PHARMACY);

        if (Laila.instance().getMUser() != null && Laila.instance().getMUser().getContacts() != null)
            for (val i : Laila.instance()
                    .getMUser()
                    .getContacts()) {
                if (!TextUtils.isEmpty(i.getFirstName()))
                    mPharmacyList.add(i.getFirstName());
            }

        ArrayAdapter<String> pharmacyAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                mPharmacyList);
        mBinding.pharmacy.setAdapter(pharmacyAdapter);
    }

    //*************************************************************
    private void pharmacyDropDownItems(ArrayList<String> pharmacyNameList, String name)
    //*************************************************************
    {
        ArrayAdapter<String> pharmacyListAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, pharmacyNameList);
        pharmacyListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.pharmacy.setAdapter(pharmacyListAdapter);

        int spinnerPosition = pharmacyListAdapter.getPosition(String.valueOf(name));
        mBinding.pharmacy.setSelection(spinnerPosition);
    }

    //*************************************************************
    private void refillDate()
    //*************************************************************
    {
        val addMedicationRequest = Laila.instance().getMAddMedicationRequest();

        if (addMedicationRequest == null || addMedicationRequest.getDispensedDate() == null || addMedicationRequest.getDispensedDate().length() == 0)
            return;

        val dispensedDate = addMedicationRequest.getDispensedDate();
        val dosage = stringToInteger(addMedicationRequest.getNumber_of_pills());
        val dispensedAmount = stringToInteger(addMedicationRequest.getDispensedAmount());
        val frequency = stringToInteger(addMedicationRequest.getFrequency());

        if (dosage == 0 || dispensedAmount == 0 || frequency == 0)
            return;
        val refillDays = dispensedAmount / (dosage * frequency);
        val date = addDaysToDate(dispensedDate, refillDays);
        mBinding.refillDate.setText(date);
        Log.d(TAG, "refillDays" + refillDays);
    }

    //*****************************************************
    @SneakyThrows
    private String addDaysToDate(@NonNull String date, @NonNull int days)
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

    //******************************************************************
    int stringToInteger(@NonNull String num)
    //******************************************************************
    {
        int myNum = 0;
        try {
            myNum = Integer.parseInt(num);
        } catch (NumberFormatException e) {
        }
        return myNum;
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
//        if (RXCare.instance().is_over_the_counter)
//        {
//            goToHome();
//            return;
//        }
        addMedicineEvents();
    }

    //*************************************************************
    private void addMedicineEvents()
    //*************************************************************
    {
        val medication = mMedicationResponses.getMedication();
        if (medication == null)
            return;

        val addMedicationRequest = Laila.instance()
                .getMAddMedicationRequest();
        val intakeTimeList = addMedicationRequest.getIntakeTimeList();

        val userPrivateCode = Laila.instance().getMUser().getProfile().getUserPrivateCode();
        val type = Constants.ALARM_TYPE;
        val eventTitle = medication.getMedicationName();
        val startDate = medication.getStartDate();
        val endDate = medication.getRefillDate();
        int frequency = Integer.parseInt(medication.getFrequency2());
        val medicationId = medication.getId();
        if (intakeTimeList == null || intakeTimeList.size() == 0)
            return;

        AddMedicineEventRequest eventRequest = new AddMedicineEventRequest();

        List<Events> eventsList = new ArrayList<>();

        Events events = new Events();


        for (int i = 0; i < frequency; i++) {
            events = new Events();
            events.setType(type);
            events.setEventTitle(eventTitle);
            events.setMedicationId(medicationId);
            events.setStartDate(startDate + " 8:00AM");
            events.setEndDate(endDate + " 11:59PM");
            events.setAlarmType("alarm1");
            switch (i) {
                case 0:
                    events.setFrequency("1");
                    events.setTimeSchedule(intakeTimeList.get(0));
                    eventsList.add(events);
                    break;
                case 1:
                    events.setFrequency("2");
                    events.setTimeSchedule(intakeTimeList.get(1));
                    eventsList.add(events);
                    break;
                case 2:
                    events.setFrequency("3");
                    events.setTimeSchedule(intakeTimeList.get(2));
                    eventsList.add(events);
                    break;
                case 3:
                    events.setFrequency("4");
                    events.setTimeSchedule(intakeTimeList.get(3));
                    eventsList.add(events);
                    break;
            }

        }

        eventRequest.setEvents(new AddEventRequest(userPrivateCode, eventsList));

        mMedicineEventViewModel.medicineEvent(eventRequest);

    }

    //*************************************************************
    @Override
    public void onFailed(@NonNull String errorMessage)
    //*************************************************************
    {
        hideLoadingDialog();
        Log.d("SessionToken", "onFailed: " + errorMessage);
        AndroidUtil.displayAlertDialog(errorMessage, "Error", getActivity());
    }

    //*************************************************************
    @Override
    public void onSuccessfullyAddEvent(@Nullable MedicineEventResponse response)
    //*************************************************************
    {
        if (response.getEvents() != null) {
            Laila.instance().addMedicineAlarm(response.getEvents(), 0);
            if (Laila.instance().getMUser().getEvents() == null)
                Laila.instance().getMUser().setEvents(new ArrayList<>());
            for (val event : response.getEvents())
                Laila.instance().getMUser().getEvents().add(event);
            SharedPreferencesUtils.setValue(Constants.USER_DATA, Laila.instance().getMUser());
        }
        goToHome();
    }

    //*************************************************************
    @Override
    public void onFailedToAddEvent(@NonNull String errorMessage)
    //*************************************************************
    {
        hideLoadingDialog();
        AndroidUtil.displayAlertDialog(errorMessage, AndroidUtil.getString(R.string.error), getActivity());
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
