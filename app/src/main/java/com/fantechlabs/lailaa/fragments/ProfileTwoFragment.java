package com.fantechlabs.lailaa.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.databinding.FragmentProfileTwoBinding;
import com.fantechlabs.lailaa.models.Country;
import com.fantechlabs.lailaa.models.State;
import com.fantechlabs.lailaa.models.updates.models.Profile;
import com.fantechlabs.lailaa.models.updates.request_models.ProfileRequest;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.UIUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import lombok.val;


//***********************************************************
public class ProfileTwoFragment extends BaseFragment
//***********************************************************
{
    private FragmentProfileTwoBinding mBinding;
    private View mRootView;
    private ProfileRequest mProfileRequest;
    private ArrayList<String> mCountries = new ArrayList<String>();
    private List<Country> mCountryList;
    public static int mCounter = 0;
    private String mCurrentCountry;
    private int mCurrentSelection;
    private Profile mProfile;

    //***********************************************************
    public ProfileTwoFragment()
    //***********************************************************
    {
        // Required empty public constructor
    }

    //*********************************************************************************************
    @NonNull
    @Override
    public View onCreateViewBaseFragment(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    //*********************************************************************************************
    {
        if (mRootView == null) {
            mBinding = FragmentProfileTwoBinding.inflate(inflater, parent, false);
            mRootView = mBinding.getRoot();
        }
        initControls();

        return mRootView;
    }

    //*********************************************************
    private void initControls()
    //*********************************************************
    {
        edit();
        initStates();
        getCountryList();
        editTextWatcher();
        setCountryAndStateToDropdownList();
    }

    //******************************************************
    private void initStates()
    //******************************************************
    {
        mCurrentCountry = AndroidUtil.getDeviceCountryCode(getContext());
        if (mCurrentCountry.equals("us"))
            mBinding.stateTitle.setText(R.string.state);
    }

    //******************************************************
    private void setCountryAndStateToDropdownList()
    //******************************************************
    {
        if (Laila.instance().getMUser_U().getData() == null || Laila.instance().getMUser_U().getData().getProfile() == null)
            return;
        val userDetail = Laila.instance().getMUser_U().getData().getProfile();
        val countryName = userDetail.getAddressCountry();
        val stateName = userDetail.getAddressProvince();

        ArrayList<String> countries = new ArrayList<>();
        ArrayList<String> states = new ArrayList<>();

        val countriesList = UIUtils.readFileFroRawFolder(R.raw.countries_states);
        mCountryList = getReceiverObj(countriesList);

        for (Country country : mCountryList) {
            countries.add(country.getName());
            for (State state : country.getStates())
                if (country.getName().equals(countryName))
                    states.add(state.getName());
        }

        setDropDownItems(mBinding.country, countries, TextUtils.isEmpty(countryName) ? AndroidUtil.getCountryFullName(mCurrentCountry) : countryName);
        if (!TextUtils.isEmpty(stateName))
            setDropDownItems(mBinding.state, states, stateName);
    }

    //******************************************************
    private void edit()
    //******************************************************
    {
        mBinding.address1.requestFocus();
        mBinding.zipCode.setEnabled(true);
        mBinding.country.setEnabled(true);
        mBinding.city.setEnabled(true);
        mBinding.address1.setEnabled(true);
        mBinding.address2.setEnabled(true);
        mBinding.phone.setEnabled(true);
        mBinding.state.setEnabled(true);
    }

    //******************************************************
    private void editTextWatcher()
    //******************************************************
    {
        mProfileRequest = Laila.instance().getMProfileRequest();
        if (mProfileRequest == null)
            mProfileRequest = new ProfileRequest();

        mCurrentSelection = mBinding.country.getSelectedItemPosition();
        mBinding.country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                mProfileRequest.setAddressCountry(parent.getSelectedItem().toString());
                Laila.instance().setMProfileRequest(mProfileRequest);

                val itemPosition = parent.getSelectedItemPosition();

                ArrayList<String> statesList = new ArrayList<>();

                if (mCountryList == null || mCountryList.size() == 0)
                    return;

                val states = mCountryList.get(itemPosition).getStates();
                for (State state : states) {
                    statesList.add(state.getName());
                }

                val stateName = mProfile.getAddressProvince();
                if (!stateName.isEmpty())
                    setDropDownItems(mBinding.state, statesList, stateName);
                else
                    setCountryAndStateList(mBinding.state, statesList);
                mCurrentSelection = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mBinding.state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mProfileRequest.setAddressProvince(parent.getSelectedItem().toString());
                Laila.instance().setMProfileRequest(mProfileRequest);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mBinding.city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mProfileRequest.setAddressCity(s.toString());
                Laila.instance().setMProfileRequest(mProfileRequest);
            }
        });
        mBinding.zipCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mProfileRequest.setAddressPobox(s.toString());
                Laila.instance().setMProfileRequest(mProfileRequest);
            }
        });
        mBinding.address1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mProfileRequest.setAddressLine1(s.toString());
                Laila.instance().setMProfileRequest(mProfileRequest);
            }
        });
        mBinding.address2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mProfileRequest.setAddressLine2(s.toString());
                Laila.instance().setMProfileRequest(mProfileRequest);
            }
        });
        mBinding.phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mProfileRequest.setPhone(s.toString());
                Laila.instance().setMProfileRequest(mProfileRequest);
            }
        });
    }

    //************************************************
    private void getCountryList()
    //************************************************
    {
        ArrayList<String> countries = new ArrayList<>();

        val countriesList = UIUtils.readFileFroRawFolder(R.raw.countries_states);
        mCountryList = getReceiverObj(countriesList);
        for (Country country : mCountryList) {
            countries.add(country.getName());
        }
        setCountryAndStateList(mBinding.country, countries);

    }

    //*********************************************************************************************
    private void setCountryAndStateList(Spinner spinner, ArrayList<String> countries)
    //*********************************************************************************************
    {
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, countries);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(countryAdapter);
    }

    //*******************************************************************
    @Override
    public void onResume()
    //*******************************************************************
    {
        super.onResume();
        setData();
    }

    //*************************************************************
    private void setData()
    //*************************************************************
    {
        mProfile = Laila.instance().getMUser_U().getData().getProfile();
        if (mProfile == null)
            mProfile = new Profile();

        mProfileRequest = Laila.instance().getMProfileRequest();
        if (mProfileRequest.getHeight() != null || mProfileRequest.getWeight() != null) {
            requestProfile();
            return;
        }
        getProfileDate();
    }

    //*************************************************************
    private void requestProfile()
    //*************************************************************
    {
        mBinding.zipCode.setText(mProfileRequest.getAddressPobox());
        mBinding.city.setText(mProfileRequest.getAddressCity());
        mBinding.phone.setText(mProfileRequest.getPhone());
        mBinding.address1.setText(mProfileRequest.getAddressLine1());
        mBinding.address2.setText(mProfileRequest.getAddressLine2());
    }

    //*************************************************************
    private void getProfileDate()
    //*************************************************************
    {
        mBinding.zipCode.setText(mProfile.getAddressPobox());
        mBinding.city.setText(mProfile.getAddressCity());
        mBinding.phone.setText(mProfile.getPhone());
        mBinding.address1.setText(mProfile.getAddressLine1());
        mBinding.address2.setText(mProfile.getAddressLine2());
        setCountryAndStateToDropdownList();

    }

    //*************************************************************
    public List<Country> getReceiverObj(String countriesList)
    //*************************************************************
    {
        Type listType = new TypeToken<List<Country>>() {
        }.getType();
        List<Country> country = new Gson().fromJson(countriesList, listType);
        return country;
    }

    //*************************************************************
    private void setDropDownItems(Spinner spinner, ArrayList<String> itemList, String name)
    //*************************************************************
    {
        ArrayAdapter<String> countryListAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, itemList);
        countryListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(countryListAdapter);

        int spinnerPosition = countryListAdapter.getPosition(name);
        spinner.setSelection(spinnerPosition);
    }
}
