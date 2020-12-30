package com.fantechlabs.lailaa.view_models;

import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.models.SearchMedicine;
import com.fantechlabs.lailaa.models.response_models.UpcResponse;
import com.fantechlabs.lailaa.network.ServiceGenerator;
import com.fantechlabs.lailaa.network.services.MedicationService;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;

import java.util.List;

import lombok.NonNull;
import lombok.val;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//**********************************************************
public class UPCViewModel
        extends ViewModel
//**********************************************************

{

    private UPCViewModelListener mUPCViewModelListener;

    public UPCViewModel(UPCViewModelListener viewModelListener) {
        this.mUPCViewModelListener = viewModelListener;
    }

    //**********************************************************
    public void checkUPC(String UPC)
    //**********************************************************
    {

        val service = ServiceGenerator.createService(MedicationService.class,
                true,
                Constants.UPC);
        if (service == null) {
            mUPCViewModelListener.onFailed(AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        val placeServices = service.getUpcLookUp(UPC);

        //**********************************************************
        placeServices.enqueue(new Callback<UpcResponse>()
                //**********************************************************
        {

            //**********************************************************
            @Override
            public void onResponse(Call<UpcResponse> call, Response<UpcResponse> response)
            //**********************************************************
            {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        mUPCViewModelListener.onSuccess(response.body().getItems());
                        return;
                    }
                    mUPCViewModelListener.onFailed((TextUtils.isEmpty(response.body().getMessage()) ? AndroidUtil.getString(R.string.server_error) : response.body().getMessage()));
                    return;
                }
                mUPCViewModelListener.onFailed(AndroidUtil.getString(R.string.server_error));
            }

            //**********************************************************
            @Override
            public void onFailure(Call<UpcResponse> call, Throwable t)
            //**********************************************************
            {
                if (mUPCViewModelListener != null)
                    mUPCViewModelListener.onFailed(t.getLocalizedMessage());
            }
        });

    }

    //**********************************************************
    public interface UPCViewModelListener
            //**********************************************************
    {
        void onSuccess(@Nullable List<SearchMedicine> response);

        void onFailed(@NonNull String message);
    }

}
