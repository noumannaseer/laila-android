package com.fantechlabs.lailaa.view_models;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.models.response_models.PharmacyResponse;
import com.fantechlabs.lailaa.network.ServiceGenerator;
import com.fantechlabs.lailaa.network.services.MedicationService;
import com.fantechlabs.lailaa.request_models.AddPharmacyRequest;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;

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
    public void addPharmacy(@NonNull AddPharmacyRequest addPharmacyRequest)
    //***********************************************************
    {
        val service = ServiceGenerator.createService(MedicationService.class, true,
                                                     Constants.BASE_URL);
        if (service == null)
        {
            mAddPharmacyListener.onPharmacyFailedToAdded(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        HashMap<String, Object> pharmacylist = new HashMap<String, Object>();
        pharmacylist.put("user_private_code", addPharmacyRequest.getUser_private_code());
        if (addPharmacyRequest.getId() != 0)
            pharmacylist.put("id", addPharmacyRequest.getId());
        pharmacylist.put("first_name", addPharmacyRequest.getFirst_name());
        pharmacylist.put("last_name", addPharmacyRequest.getLast_name());
        pharmacylist.put("contact_type", addPharmacyRequest.getContact_type());
        pharmacylist.put("email", addPharmacyRequest.getEmail());
        pharmacylist.put("phone", addPharmacyRequest.getPhone());
        pharmacylist.put("address_line1", addPharmacyRequest.getAddress_line1());
        pharmacylist.put("address_line2", addPharmacyRequest.getAddress_line2());
        pharmacylist.put("address_line3", addPharmacyRequest.getAddress_line3());
        pharmacylist.put("address_city", addPharmacyRequest.getAddress_city());
        pharmacylist.put("address_province", addPharmacyRequest.getAddress_province());
        pharmacylist.put("address_country", addPharmacyRequest.getAddress_country());
        pharmacylist.put("address_pobox", addPharmacyRequest.getAddress_pobox());
        pharmacylist.put("user_name", addPharmacyRequest.getUser_name());
        pharmacylist.put("is_preferred", addPharmacyRequest.getIs_preferred());

        HashMap<String, Object> main = new HashMap<String, Object>();
        main.put("contact", pharmacylist);

        val medicationService = service.addPharmacy(main);

        medicationService.enqueue(new Callback<PharmacyResponse>()
        {
            //***********************************************************
            @Override
            public void onResponse(Call<PharmacyResponse> call, Response<PharmacyResponse> response)
            //***********************************************************
            {
                if (response.isSuccessful())
                {
                    if (response.body().getError() != null)
                    {
                        mAddPharmacyListener.onPharmacyFailedToAdded((TextUtils.isEmpty(response.body().getError()) ?
                                AndroidUtil.getString(R.string.server_error) :
                                response.body().getError()));
                        return;
                    }
                        mAddPharmacyListener.onPharmacySuccessfullyAdded(response.body());
                    return;
                }
                mAddPharmacyListener.onPharmacyFailedToAdded(AndroidUtil.getString(R.string.server_error));
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
    }
}
