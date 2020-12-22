package com.fantechlabs.lailaa.view_models;

import androidx.lifecycle.ViewModel;

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.models.pharmact_places.GoogleAddress;
import com.fantechlabs.lailaa.network.ServiceGenerator;
import com.fantechlabs.lailaa.network.services.NearByService;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;

import java.io.IOException;

import lombok.NonNull;
import lombok.val;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//***********************************************
public class GoogleAddressViewModel
        extends ViewModel
//***********************************************
{

    private GoogleAddressViewModelListener mGoogleAddressViewModelListener;

    public GoogleAddressViewModel(GoogleAddressViewModelListener mViewModelListener)
    {
        this.mGoogleAddressViewModelListener = mViewModelListener;
    }

    //*******************************************************************
    public void getGoogleAddress(String fields, String placeId, String key)
    //*******************************************************************
    {

        val service = ServiceGenerator.createService(NearByService.class,
                                                     true,
                                                     Constants.GOOGLE_BASE_URL);
        if (service == null)
        {
            mGoogleAddressViewModelListener.onFailed(AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        val placeServices = service.getGoogleAddress(fields, placeId, key);

        //*******************************************************************
        placeServices.enqueue(new Callback<GoogleAddress>()
        //*******************************************************************
        {

            //*******************************************************************
            @Override
            public void onResponse(Call<GoogleAddress> call, Response<GoogleAddress> response)
            //*******************************************************************
            {
                if (response.isSuccessful())
                {
                    mGoogleAddressViewModelListener.onSuccess(response.body());

                }
                else
                {
                    String errorMessage = "API error";
                    try
                    {
                        errorMessage = new String(response.errorBody()
                                                          .bytes());
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    if (mGoogleAddressViewModelListener != null)
                        mGoogleAddressViewModelListener.onFailed(errorMessage);

                }
            }

            //*******************************************************************
            @Override
            public void onFailure(Call<GoogleAddress> call, Throwable t)
            //*******************************************************************
            {
                if (mGoogleAddressViewModelListener != null)
                    mGoogleAddressViewModelListener.onFailed(t.getLocalizedMessage());

            }
        });

    }

    //******************************************************
    public interface GoogleAddressViewModelListener
    //******************************************************
    {
        void onSuccess(@NonNull GoogleAddress response);

        void onFailed(@NonNull String message);
    }

}
