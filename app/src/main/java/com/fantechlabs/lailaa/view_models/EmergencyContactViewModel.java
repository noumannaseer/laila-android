package com.fantechlabs.lailaa.view_models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.bodyreading.repository.network.ServiceGenerator;
import com.fantechlabs.lailaa.bodyreading.repository.network.utils.NetworkUtils;
import com.fantechlabs.lailaa.models.updates.request_models.EmergencyContactRequest;
import com.fantechlabs.lailaa.models.updates.response_models.EmergencyContactResponse;
import com.fantechlabs.lailaa.network.services.MedicationService;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;

import java.util.HashMap;

import lombok.val;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//***********************************************************
public class EmergencyContactViewModel
        extends ViewModel
//***********************************************************
{

    public EmergencyContactListener mEmergencyContactListener;

    //***********************************************************
    public EmergencyContactViewModel(@NonNull EmergencyContactListener mEmergencyContactListener)
    //***********************************************************
    {
        this.mEmergencyContactListener = mEmergencyContactListener;
    }

    //***********************************************************
    public void createEmergencyContact(@NonNull EmergencyContactRequest emergencyContactRequest)
    //***********************************************************
    {
        val service = ServiceGenerator.createService(MedicationService.class, true,
                Constants.BASE_URL_U);
        if (service == null) {
            mEmergencyContactListener.onFailed(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        HashMap<String, Object> contactList = new HashMap<String, Object>();

        contactList.put(Constants.USER_ID, emergencyContactRequest.getUserId());
        contactList.put(Constants.FIRST_NAME, emergencyContactRequest.getFirstName());
        contactList.put(Constants.LAST_NAME, emergencyContactRequest.getLastName());
        contactList.put(Constants.CONTACT_TYPE, emergencyContactRequest.getContactType());
        contactList.put(Constants.PHONE, emergencyContactRequest.getPhone());
        contactList.put(Constants.USER_TOKEN, emergencyContactRequest.getToken());

        val medicationIngredientsService = service.createEmergencyContact(contactList);

        medicationIngredientsService.enqueue(new Callback<EmergencyContactResponse>() {

            //***********************************************************
            @Override
            public void onResponse(Call<EmergencyContactResponse> call, Response<EmergencyContactResponse> response)
            //***********************************************************
            {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        mEmergencyContactListener.onSuccessfullyCreateContact(response.body());
                        return;
                    }
                    mEmergencyContactListener.onFailed(AndroidUtil.getString(R.string.server_error));
                    return;
                }
                val error = NetworkUtils.responseError(response);
                mEmergencyContactListener.onFailed(error);
            }

            //***********************************************************
            @Override
            public void onFailure(Call<EmergencyContactResponse> call, Throwable t)
            //***********************************************************
            {
                mEmergencyContactListener.onFailed(t.getLocalizedMessage());
            }
        });

    }

    //***********************************************************
    public void updateEmergencyContact(@NonNull EmergencyContactRequest emergencyContactRequest, int id)
    //***********************************************************
    {
        val service = ServiceGenerator.createService(MedicationService.class, true,
                Constants.BASE_URL_U);
        if (service == null) {
            mEmergencyContactListener.onFailed(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        HashMap<String, Object> contactList = new HashMap<String, Object>();
        contactList.put(Constants.ID, emergencyContactRequest.getContactId());
        contactList.put(Constants.USER_ID, emergencyContactRequest.getUserId());
        contactList.put(Constants.FIRST_NAME, emergencyContactRequest.getFirstName());
        contactList.put(Constants.LAST_NAME, emergencyContactRequest.getLastName());
        contactList.put(Constants.CONTACT_TYPE, emergencyContactRequest.getContactType());
        contactList.put(Constants.PHONE, emergencyContactRequest.getPhone());
        contactList.put(Constants.USER_TOKEN, emergencyContactRequest.getToken());

        val medicationIngredientsService = service.updateEmergencyContact(contactList);

        medicationIngredientsService.enqueue(new Callback<EmergencyContactResponse>() {

            //***********************************************************
            @Override
            public void onResponse(Call<EmergencyContactResponse> call, Response<EmergencyContactResponse> response)
            //***********************************************************
            {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        mEmergencyContactListener.onSuccessfullyCreateContact(response.body());
                        return;
                    }
                    mEmergencyContactListener.onFailed(AndroidUtil.getString(R.string.server_error));
                    return;
                }
                val error = NetworkUtils.responseError(response);
                mEmergencyContactListener.onFailed(error);
            }

            //***********************************************************
            @Override
            public void onFailure(Call<EmergencyContactResponse> call, Throwable t)
            //***********************************************************
            {
                mEmergencyContactListener.onFailed(t.getLocalizedMessage());
            }
        });

    }

    //***********************************************************
    public void getEmergencyContacts()
    //***********************************************************
    {
        val service = ServiceGenerator.createService(MedicationService.class, true,
                Constants.BASE_URL_U);
        if (service == null) {
            mEmergencyContactListener.onFailed(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        val userResponse = Laila.instance().getMUser_U();
        if (userResponse.getData() == null)
            return;
        val token = Laila.instance().getMUser_U().getData().getUser().getToken();
        val user_id = Laila.instance().getMUser_U().getData().getUser().getId().toString();
        HashMap<String, String> contactList = new HashMap<>();
        contactList.put(Constants.USER_TOKEN, token);
        contactList.put(Constants.USER_ID, user_id);
        val getEmergencyContacts = service.getEmergencyContacts(contactList);

        getEmergencyContacts.enqueue(new Callback<EmergencyContactResponse>() {

            //***********************************************************
            @Override
            public void onResponse(Call<EmergencyContactResponse> call, Response<EmergencyContactResponse> response)
            //***********************************************************
            {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        mEmergencyContactListener.onSuccessfullyGetContacts(response.body());
                        return;
                    }
                    mEmergencyContactListener.onFailed(AndroidUtil.getString(R.string.server_error));
                    return;
                }
                val error = NetworkUtils.responseError(response);
                mEmergencyContactListener.onFailed(error);
            }

            //***********************************************************
            @Override
            public void onFailure(Call<EmergencyContactResponse> call, Throwable t)
            //***********************************************************
            {
                mEmergencyContactListener.onFailed(t.getLocalizedMessage());
            }
        });

    }

    //***********************************************************
    public interface EmergencyContactListener
            //***********************************************************
    {
        void onSuccessfullyCreateContact(@Nullable EmergencyContactResponse response);

        void onSuccessfullyGetContacts(@Nullable EmergencyContactResponse response);

        void onFailed(@NonNull String errorMessage);
    }
}
