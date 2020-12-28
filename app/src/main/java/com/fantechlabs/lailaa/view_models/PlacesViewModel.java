package com.fantechlabs.lailaa.view_models;

import androidx.lifecycle.ViewModel;

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.models.pharmact_places.NearByPlaces;
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

public class PlacesViewModel
        extends ViewModel {

    private PlaceViewModelListener mPlaceViewModelListener;

    public PlacesViewModel(PlaceViewModelListener mPlaceViewModelListener) {
        this.mPlaceViewModelListener = mPlaceViewModelListener;
    }

    public void getNearByPharmacy(String latlng, int radius, String types, String key) {

        val service = ServiceGenerator.createService(NearByService.class,
                true,
                Constants.GOOGLE_BASE_URL);
        if (service == null) {
            mPlaceViewModelListener.onFailed(AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        val placeServices = service.getPlacesApi(latlng, radius, types, key);

        placeServices.enqueue(new Callback<NearByPlaces>() {
            @Override
            public void onResponse(Call<NearByPlaces> call, Response<NearByPlaces> response) {
                if (response.isSuccessful()) {
                    mPlaceViewModelListener.onSuccessFullyCalled(response.body());

                } else {
                    String errorMessage = "API error";
                    try {
                        errorMessage = new String(response.errorBody()
                                .bytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (mPlaceViewModelListener != null)
                        mPlaceViewModelListener.onFailed(errorMessage);

                }
            }

            @Override
            public void onFailure(Call<NearByPlaces> call, Throwable t) {
                if (mPlaceViewModelListener != null)
                    mPlaceViewModelListener.onFailed(t.getLocalizedMessage());

            }
        });

    }

    public interface PlaceViewModelListener {
        void onSuccessFullyCalled(@NonNull NearByPlaces nearByPlaces);

        void onFailed(@NonNull String message);
    }

}
