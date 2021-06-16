package com.aditum.fragments;

import android.content.DialogInterface;
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

import com.aditum.Laila;
import com.aditum.R;
import com.aditum.activities.AddMedicationActivity;
import com.aditum.activities.AddPharmacyActivity;
import com.aditum.activities.MedicationActivity;
import com.aditum.databinding.FragmentAddMedicationThreeBinding;
import com.aditum.models.updates.models.Event;
import com.aditum.models.updates.models.Medication;
import com.aditum.models.updates.models.ResponseEvent;
import com.aditum.models.updates.response_models.AddEventResponse;
import com.aditum.models.updates.response_models.GetEventsResponse;
import com.aditum.models.updates.response_models.MedicationResponse;
import com.aditum.models.updates.response_models.PharmacyResponse;
import com.aditum.utils.AndroidUtil;
import com.aditum.utils.Constants;
import com.aditum.utils.DateUtils;
import com.aditum.utils.SharedPreferencesUtils;
import com.aditum.view_models.AddMedicationViewModel;
import com.aditum.view_models.AddPharmacyViewModel;
import com.aditum.view_models.MedicineEventViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lombok.SneakyThrows;
import lombok.val;

import static com.aditum.utils.Constants.SELECT_PHARMACY;

//***********************************************************
public class AddMedicationThreeFragment
        extends BaseFragment
        implements AddMedicationViewModel.AddMedicationListener,
        MedicineEventViewModel.MedicineEventCompleteListener,
        View.OnClickListener,
        AddPharmacyViewModel.AddPharmacyListener
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
    private AddPharmacyViewModel mAddPharmacyViewModel;


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
        initViewModels();
        infoClickListener();
        saveMedications();
//        getPharmacies();
        addPharmacy();
        loadPharmacies();
        backToPreviousScreen();
    }

    //***********************************************************
    private void loadPharmacies()
    //***********************************************************
    {
        val pharmacies = Laila.instance().getMUser_U().getData().getStringPharmacyList();
        if (pharmacies != null)
            pharmacyDropDown(pharmacies);

    }

    //*************************************************************
    private void pharmacyDropDown(List<String> pharmacyNameList)
    //*************************************************************
    {
        ArrayAdapter<String> pharmacyListAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, pharmacyNameList);
        pharmacyListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.pharmacy.setAdapter(pharmacyListAdapter);
    }

    //***********************************************************
    private void initViewModels()
    //***********************************************************
    {
        mAddMedicationViewModel = new AddMedicationViewModel(this);
        mMedicineEventViewModel = new MedicineEventViewModel(this);
        mAddPharmacyViewModel = new AddPharmacyViewModel(this);
    }

    //***********************************************************
    private void backToPreviousScreen()
    //***********************************************************
    {
        mBinding.navigateBack.setOnClickListener(v ->
        {
            if (AndroidUtil.mTooltip != null)
                AndroidUtil.mTooltip.dismiss();
            Laila.instance().from_third_screen = true;

            ((AddMedicationActivity) getActivity()).navigateToScreen(1);
        });
    }

    //***********************************************************
    private void saveMedications()
    //***********************************************************
    {
        mBinding.saveBtn.setOnClickListener(v -> validation());
    }

    //***********************************************************
    private void addPharmacy()
    //***********************************************************
    {
        mBinding.addPharmacy.setOnClickListener(v ->
        {
            if (AndroidUtil.mTooltip != null)
                AndroidUtil.mTooltip.dismiss();

            Intent pharmacyIntent = new Intent(
                    getContext(),
                    AddPharmacyActivity.class);
            startActivity(pharmacyIntent);
        });
    }

    //***********************************************************
    private void getPharmacies()
    //***********************************************************
    {
        mPharmacyList = new ArrayList<>();
        mPharmacyList = (ArrayList<String>) Laila.instance().getMUser_U().getData().getStringPharmacyList();

        if (mPharmacyList == null) {
            showLoadingDialog();
            mAddPharmacyViewModel.getPharmacies();
        }

    }

    //***********************************************************
    private void infoClickListener()
    //***********************************************************
    {
        mBinding.refillDateInfo.setOnClickListener(this);
        mBinding.addMedicationThreeMainView.setOnClickListener(this);
    }

    //***************************************************************
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    //***************************************************************
    {
        if (isVisibleToUser) {
//            getPharmacies();
//
//            getActivity().getWindow()
//                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//
//            refillDate();
//            if (Laila.instance().is_pharmacy_added)
//                Laila.instance().is_pharmacy_added = false;
//
//            if (Laila.instance().from_pharmacy_added) {
//                setUpdatedPharmacy();
//                Laila.instance().from_pharmacy_added = false;
//            }
//            val onUpdateMedicine = Laila.instance().on_update_medicine;
//            if (onUpdateMedicine)
//                onUpdate();


        }

        super.setUserVisibleHint(isVisibleToUser);
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
            case R.id.add_medication_three_container:
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
                val user = Laila.instance().getMUser_U();
                if (user == null)
                    return;
                val pharmacyList = user.getData().getPharmacyList();
                if (pharmacyList == null)
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
        val medicationList = Laila.instance().getMUser_U().getData().getMedicationList();
        if (medicationList == null || medicationList.size() == 0)
            return;

        val medication = Laila.instance().getMUpdateMedication();

        if (mUpdateMedicine) {
            mBinding.saveBtn.setText(R.string.update);
            mBinding.saveBtn.setBackgroundTintList(
                    ContextCompat.getColorStateList(getActivity(), R.color.button_background));
            if (medication == null)
                return;

            val pharmacy = medication.getPharmacyId();
            val deliverType = medication.getPrescribed();

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

        if (Laila.instance().getMUser_U() == null || Laila.instance().getMUser_U().getData().getPharmacyList() == null)
            return;
        val pharmacyList = Laila.instance().getMUser_U().getData().getPharmacyList();
        pharmacyNameList.add(SELECT_PHARMACY);
        for (val pharmacy : pharmacyList) {
            if (pharmacy.getId().equals(id))
                name = pharmacy.getName();
            pharmacyNameList.add(pharmacy.getName());
        }
//        if (TextUtils.isEmpty(name) && id == 0) {
//            setPharmacySpinner();
//            return;
//        }
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

//        val deliveryType = mBinding.pickup.isChecked();

        val refillDate = mBinding.refillDate.getText().toString();
        String pharmacyId = "";

        val position = mBinding.pharmacy.getSelectedItemPosition();
        val pharmacyText = mBinding.pharmacy.getSelectedItem().toString();
        if (prescribed == 1)
            if (TextUtils.isEmpty(pharmacyText) || pharmacyText.equals(SELECT_PHARMACY)) {
                AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.pharmacy_required), AndroidUtil.getString(R.string.alert), getActivity());
                return;
            }

        val user = Laila.instance()
                .getMUser_U();
        if (user == null)
            return;
        val pharmacyList = user.getData().getPharmacyList();
        if (pharmacyList == null)
            return;

        if (position > 0) {
            pharmacyId = pharmacyList
                    .get(position - 1)
                    .getId()
                    .toString();
        }
        if (TextUtils.isEmpty(pharmacyId))
            pharmacyId = "0";
        addMedicationRequest.setPharmacyId(Integer.parseInt(pharmacyId));
        addMedicationRequest.setRefillDate(refillDate);

        val onUpdateMedicine = Laila.instance().on_update_medicine;
        if (onUpdateMedicine) {

            val medication = Laila.instance().getMUpdateMedication();
            val longRefillDate = DateUtils.getDateFromStringFormat(refillDate, Constants.DATE_FORMAT).getTime();

            if (!TextUtils.isEmpty(pharmacyId))
                medication.setPharmacyId(Integer.valueOf(pharmacyId));
            medication.setRefillDate(longRefillDate);
        }

        if (mUpdateMedicine) {
            if (Laila.instance()
                    .getMUser_U()
                    .getData()
                    .getMedicationList() == null || Laila.instance()
                    .getMUser_U()
                    .getData()
                    .getMedicationList()
                    .size() == 0)
                return;
            val medication = Laila.instance()
                    .getMUser_U()
                    .getData()
                    .getMedicationList()
                    .get(mItemPosition);
            mUpdateMedicineId = medication.getId();
            addMedicationRequest.setId(mUpdateMedicineId);
            Laila.instance()
                    .setMAddMedicationRequest(addMedicationRequest);
            showLoadingDialog();
            mAddMedicationViewModel.updateMedication(addMedicationRequest);
            return;
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
//        getActivity().getWindow()
//                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//
//        refillDate();
//        if (Laila.instance().is_pharmacy_added)
//            Laila.instance().is_pharmacy_added = false;
//
//        if (Laila.instance().from_pharmacy_added) {
//            setUpdatedPharmacy();
//            Laila.instance().from_pharmacy_added = false;
//        }
//        val onUpdateMedicine = Laila.instance().on_update_medicine;
//        if (onUpdateMedicine)
//            onUpdate();


        getPharmacies();

        getActivity().getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        refillDate();
        if (Laila.instance().is_pharmacy_added)
            Laila.instance().is_pharmacy_added = false;

        if (Laila.instance().from_pharmacy_added) {
            setUpdatedPharmacy();
            Laila.instance().from_pharmacy_added = false;
        }
        val onUpdateMedicine = Laila.instance().on_update_medicine;
        if (onUpdateMedicine)
            onUpdate();

    }

    //*************************************************************
    private void setUpdatedPharmacy()
    //*************************************************************
    {
        mPharmacyList = new ArrayList<>();
        mPharmacyList.add(SELECT_PHARMACY);
        val pharmacyList = Laila.instance().getMUser_U().getData().getPharmacyList();

        if (pharmacyList != null)
            for (val i : pharmacyList) {
                if (!TextUtils.isEmpty(i.getName()))
                    mPharmacyList.add(i.getName());
            }
        Laila.instance()
                .getMUser_U()
                .getData()
                .setStringPharmacyList(mPharmacyList);

        ArrayAdapter<String> pharmacyAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                mPharmacyList);
        mBinding.pharmacy.setAdapter(pharmacyAdapter);
    }

    //*************************************************************
    private void setPharmacySpinner()
    //*************************************************************
    {
        mPharmacyList = new ArrayList<>();
        mPharmacyList.add(SELECT_PHARMACY);

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
        val dosage = addMedicationRequest.getDosage();
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
//        Laila.instance().setMAddMedicationRequest(null);
        if (response.getData() == null)
            return;
        mMedicationResponses = response;
        if (Laila.instance()
                .getMUser_U()
                .getData().getMedicationList() == null)
            Laila.instance()
                    .getMUser_U()
                    .getData()
                    .setMedicationList(new ArrayList<>());
        if (mUpdateMedicine) {
            Laila.instance().from_update_events = true;
            addMedicineEvents();
            return;
        }
        Laila.instance()
                .getMUser_U()
                .getData()
                .getMedicationList()
                .add(mMedicationResponses.getData().getMedication());
        Laila.instance()
                .setMMedicationId(response.getData().getMedication()
                        .getId());

        SharedPreferencesUtils.setValue(Constants.USER_DATA, Laila.instance().getMUser_U());
        Laila.instance().is_medicine_added = true;
        addMedicineEvents();
    }

    //*************************************************************
    private void addMedicineEvents()
    //*************************************************************
    {
        Medication medication = null;
        if (mUpdateMedicine) {
            Laila.instance().on_update_medicine = false;
            Laila.instance().from_update_medication = true;
            medication = Laila.instance().getMUpdateMedication();

            val medicationId = medication.getId();

            val events = Laila.instance().getMUser_U().getData().getEventsList();
            List<ResponseEvent> responseEventList = new ArrayList<>();
            for (val event : events) {
                if (event.getMedicationId() != null)
                    if (!event.getMedicationId().equals(Integer.toString(medicationId)))
                        responseEventList.add(event);
            }
            Laila.instance().getMUser_U().getData().setEventsList(responseEventList);
            SharedPreferencesUtils.setValue(Constants.USER_DATA, Laila.instance().getMUser_U());

        } else
            medication = mMedicationResponses.getData().getMedication();

        val addMedicationRequest = Laila.instance()
                .getMAddMedicationRequest();
        if (addMedicationRequest == null)
            return;
        val intakeTimeList = addMedicationRequest.getIntakeTimeList();
        val userId = Laila.instance().getMUser_U().getData().getUser().getId().toString();
        val token = Laila.instance().getMUser_U().getData().getUser().getToken();
        val type = Constants.MEDICINE_REMINDER;
        val eventTitle = medication.getMedicationName();

        String startDate = "";
        String endDate = "";
        if (Laila.instance().from_update_events) {
            startDate = DateUtils.getDate(medication.getDispensedDate(), "yyyy-MMM-dd");
            endDate = DateUtils.getDate(medication.getRefillDate(), "yyyy-MMM-dd");
        } else {
            startDate = DateUtils.getDateFromTimeStamp(medication.getDispensedDate(), "yyyy-MMM-dd");
            endDate = DateUtils.getDateFromTimeStamp(medication.getRefillDate(), "yyyy-MMM-dd");
        }
        int frequency = Integer.parseInt(medication.getFrequency());
        val medicationId = medication.getId();
        if (intakeTimeList == null || intakeTimeList.size() == 0)
            return;

        val eventRequest = Laila.instance().getMAddEventRequest().Builder();

        List<Event> eventsList = new ArrayList<>();
        Event event = new Event();
        for (int i = 0; i < frequency; i++) {
            event = new Event();
            event.setType(type);
            event.setEventTitle(eventTitle);
            event.setMedicationId(Integer.toString(medicationId));
            event.setContactId("1");
            event.setDeliveryType("1");
            event.setStartDate(startDate + " 8:00AM");
            event.setEndDate(endDate + " 11:59PM");
            switch (i) {
                case 0:
                    event.setFrequency(1);
                    event.setTimeSchedule(intakeTimeList.get(0));
                    eventsList.add(event);
                    break;
                case 1:
                    event.setFrequency(2);
                    event.setTimeSchedule(intakeTimeList.get(1));
                    eventsList.add(event);
                    break;
                case 2:
                    event.setFrequency(3);
                    event.setTimeSchedule(intakeTimeList.get(2));
                    eventsList.add(event);
                    break;
                case 3:
                    event.setFrequency(4);
                    event.setTimeSchedule(intakeTimeList.get(3));
                    eventsList.add(event);
                    break;
            }

        }
        eventRequest.setUserId(Integer.parseInt(userId));
        eventRequest.setToken(token);
        eventRequest.setEvents(eventsList);

        Laila.instance().setMUpdateMedication(null);
        Laila.instance().setMAddMedicationRequest(null);

        mMedicineEventViewModel.medicineEvent(eventRequest);

    }

    //*************************************************************
    @Override
    public void onFailed(@NonNull String errorMessage)
    //*************************************************************
    {
        hideLoadingDialog();
        AndroidUtil.displayAlertDialog(errorMessage, "Error", getActivity(), "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == -1)
                    goToHome();
            }
        });

    }

    @Override
    public void onSuccessfullyGetMedications(@Nullable MedicationResponse medicationResponse) {

    }

    @Override
    public void onFailedGetMedications(@NonNull String errorMessage) {

    }

    //*************************************************************
    @Override
    public void onSuccessfullyAddEvent(@Nullable AddEventResponse response)
    //*************************************************************
    {
        if (response.getData().getResponseEvents() != null) {
            Laila.instance().addMedicineAlarm(response.getData().getResponseEvents(), 0);

            if (Laila.instance().getMUser_U().getData().getResponseEvents() == null)
                Laila.instance().getMUser_U().getData().setResponseEvents(new ArrayList<>());
            if (Laila.instance().getMUser_U().getData().getEventsList() == null)
                Laila.instance().getMUser_U().getData().setEventsList(new ArrayList<>());

            val responseEvents = response.getData().getResponseEvents();
            for (ResponseEvent event : responseEvents) {
                Laila.instance().getMUser_U().getData().getResponseEvents().add(event);
                Laila.instance().getMUser_U().getData().getEventsList().add(event);
            }
//            Laila.instance().getMUser_U().getData().setEventsList(response.getData().getResponseEvents());
            SharedPreferencesUtils.setValue(Constants.USER_DATA, Laila.instance().getMUser_U());
        }
        goToHome();
    }

    @Override
    public void onSuccessfullyGetEvents(@Nullable GetEventsResponse response) {
        hideLoadingDialog();
    }

    //*************************************************************
    @Override
    public void onFailedEvents(@NonNull String errorMessage)
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

    //*************************************************************
    @Override
    public void onPharmacySuccessfullyAdded(@Nullable PharmacyResponse Response)
    //*************************************************************
    {

    }

    //*************************************************************
    @Override
    public void onPharmacyFailedToAdded(@NonNull String errorMessage)
    //*************************************************************
    {

    }

    //*************************************************************
    @Override
    public void onSuccessfullyGet(@Nullable PharmacyResponse response)
    //*************************************************************
    {
        hideLoadingDialog();
        mPharmacyList = new ArrayList<>();
        mPharmacyList.add(SELECT_PHARMACY);
        if (response.getData() == null || response.getData().getPharmacyList() == null)
            return;
        if (response.getData().getPharmacyList() != null)
            for (val i : response.getData().getPharmacyList()) {
                if (!TextUtils.isEmpty(i.getName()))
                    mPharmacyList.add(i.getName());
            }
        Laila.instance()
                .getMUser_U()
                .getData()
                .setStringPharmacyList(mPharmacyList);
        Laila.instance()
                .getMUser_U()
                .getData()
                .setPharmacyList(response.getData().getPharmacyList());
        SharedPreferencesUtils.setValue(Constants.USER_DATA, Laila.instance().getMUser_U());

        ArrayAdapter<String> pharmacyAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                mPharmacyList);
        mBinding.pharmacy.setAdapter(pharmacyAdapter);
    }

    //*************************************************************
    @Override
    public void onFailedGet(@NonNull String errorMessage)
    //*************************************************************
    {
        hideLoadingDialog();
    }
}
