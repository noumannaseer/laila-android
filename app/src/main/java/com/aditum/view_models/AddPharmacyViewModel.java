package com.aditum.view_models;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.aditum.Laila;
import com.aditum.R;
import com.aditum.models.updates.request_models.AddPharmacyRequest;
import com.aditum.models.updates.response_models.PharmacyResponse;
import com.aditum.network.NetworkUtils;
import com.aditum.network.ServiceGenerator;
import com.aditum.network.services.MedicationService;
import com.aditum.utils.AndroidUtil;
import com.aditum.utils.Constants;

import java.util.HashMap;

import lombok.val;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//***********************************************************
public class AddPharmacyViewModel
        extends ViewModel
//***********************************************************
{
    public AddPharmacyListener mAddPharmacyListener;

    //***********************************************************
    public AddPharmacyViewModel(@NonNull AddPharmacyListener mAddPharmacyListener)
    //***********************************************************
    {
        this.mAddPharmacyListener = mAddPharmacyListener;
    }

    //***********************************************************
    public void getPharmacies()
    //***********************************************************
    {
        val service = ServiceGenerator.createService(MedicationService.class, true,
                Constants.BASE_URL_U);
        if (service == null) {
            mAddPharmacyListener.onFailedGet(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }
        val token = Laila.instance().getMUser_U().getData().getUser().getToken();
        val userId = Laila.instance().getMUser_U().getData().getUser().getId().toString();

        HashMap<String, String> document = new HashMap<>();
        document.put(Constants.USER_ID, userId);
        document.put(Constants.USER_TOKEN, token);

        val getDocumentsServices = service.getPharmacies(document);


        getDocumentsServices.enqueue(new Callback<PharmacyResponse>() {
            @Override
            public void onResponse(Call<PharmacyResponse> call, Response<PharmacyResponse> response) {
                if (response.isSuccessful()) {

                    if (response.body().getStatus() == 200) {
                        mAddPharmacyListener.onSuccessfullyGet(response.body());
                        return;
                    }

                    mAddPharmacyListener.onFailedGet((TextUtils.isEmpty(response.body().getData().getMessage()) ?
                            AndroidUtil.getString(R.string.server_error) :
                            response.body().getData().getMessage()));
                    return;
                }
                val error = NetworkUtils.errorResponse(response.errorBody());
                mAddPharmacyListener.onFailedGet(error);

            }

            @Override
            public void onFailure(Call<PharmacyResponse> call, Throwable t) {
                mAddPharmacyListener.onFailedGet(t.getLocalizedMessage());
            }
        });

    }

    //***********************************************************
    public void addPharmacy(@NonNull AddPharmacyRequest addPharmacyRequest)
    //***********************************************************
    {
        val service = ServiceGenerator.createService(MedicationService.class, true,
                Constants.BASE_URL_U);
        if (service == null) {
            mAddPharmacyListener.onPharmacyFailedToAdded(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }
        val user_token = Laila.instance().getMUser_U().getData().getUser().getToken();
        val userId = Laila.instance().getMUser_U().getData().getUser().getId().toString();

        HashMap<String, Object> pharmacylist = new HashMap<String, Object>();
//         if (addPharmacyRequest.getId() != 0)
//            pharmacylist.put("id", addPharmacyRequest.getId());
        pharmacylist.put("name", addPharmacyRequest.getName());
        pharmacylist.put("email", addPharmacyRequest.getEmail());
        pharmacylist.put("contact_type", addPharmacyRequest.getContactType());
        pharmacylist.put("contact_no", addPharmacyRequest.getContactNo());
        pharmacylist.put("address", addPharmacyRequest.getAddress());
        pharmacylist.put("address2", addPharmacyRequest.getAddress2());
        pharmacylist.put("province", addPharmacyRequest.getProvince());
        pharmacylist.put("city", addPharmacyRequest.getCity());
        pharmacylist.put("country", addPharmacyRequest.getCountry());
        pharmacylist.put("zip_code", addPharmacyRequest.getZipCode());
        pharmacylist.put("is_preferred", addPharmacyRequest.getIsPreferred());
        pharmacylist.put(Constants.USER_ID, userId);
        pharmacylist.put("token", user_token);

        val pharmacyService = service.addPharmacy(pharmacylist);

        pharmacyService.enqueue(new Callback<PharmacyResponse>() {
            //***********************************************************
            @Override
            public void onResponse(Call<PharmacyResponse> call, Response<PharmacyResponse> response)
            //***********************************************************
            {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() != 200) {
                        mAddPharmacyListener.onPharmacyFailedToAdded((TextUtils.isEmpty(response.body().getData().getMessage()) ?
                                AndroidUtil.getString(R.string.server_error) :
                                response.body().getData().getMessage()));
                        return;
                    }
                    mAddPharmacyListener.onPharmacySuccessfullyAdded(response.body());
                    return;
                }
                val error = NetworkUtils.errorResponse(response.errorBody());
                mAddPharmacyListener.onPharmacyFailedToAdded(error);
            }

            //***********************************************************
            @Override
            public void onFailure(Call<PharmacyResponse> call, Throwable t)
            //***********************************************************
            {
                mAddPharmacyListener.onPharmacyFailedToAdded(t.getLocalizedMessage());
            }
        });

    }

    //***********************************************************
    public interface AddPharmacyListener
            //***********************************************************
    {
        void onPharmacySuccessfullyAdded(@Nullable PharmacyResponse Response);

        void onPharmacyFailedToAdded(@NonNull String errorMessage);

        void onSuccessfullyGet(@Nullable PharmacyResponse userResponse);

        void onFailedGet(@NonNull String errorMessage);


    }
}
