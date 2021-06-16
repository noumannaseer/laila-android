package com.aditum.view_models;

import android.app.Activity;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.aditum.Laila;
import com.aditum.R;
import com.aditum.models.updates.request_models.DocumentRequest;
import com.aditum.models.updates.response_models.DocumentResponse;
import com.aditum.network.NetworkUtils;
import com.aditum.network.ServiceGenerator;
import com.aditum.network.services.DocumentsService;
import com.aditum.utils.AndroidUtil;
import com.aditum.utils.Constants;

import java.util.HashMap;

import lombok.val;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//****************************************************************
public class DocumentsViewModel
        extends ViewModel
//****************************************************************
{

    private DocumentsViewListener mDocumentsListener;
    private Activity mActivity;


    //****************************************************************
    public DocumentsViewModel(DocumentsViewListener documentListener, Activity mActivity)
    //****************************************************************
    {
        this.mDocumentsListener = documentListener;
        this.mActivity = mActivity;
    }

    //***********************************************************
    public void getDocument()
    //***********************************************************
    {
        val service = ServiceGenerator.createService(DocumentsService.class, true,
                Constants.BASE_URL_U);
        if (service == null) {
            mDocumentsListener.onFailed(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        val user_id = Laila.instance().getMUser_U().getData().getUser().getId().toString();
        val token = Laila.instance().getMUser_U().getData().getUser().getToken();

        HashMap<String, String> document = new HashMap<>();
        document.put(Constants.USER_ID, user_id);
        document.put(Constants.USER_TOKEN, token);

        val getDocumentsServices = service.getDocuments(document);


        getDocumentsServices.enqueue(new Callback<DocumentResponse>() {
            @Override
            public void onResponse(Call<DocumentResponse> call, Response<DocumentResponse> response) {
                if (response.isSuccessful()) {

                    if (response.body().getStatus() == 200) {
                        mDocumentsListener.onSuccessfully(response.body());
                        return;
                    }

                    mDocumentsListener.onFailed((TextUtils.isEmpty(response.body().getData().getMessage()) ?
                            AndroidUtil.getString(R.string.server_error) :
                            response.body().getData().getMessage()));
                    return;
                }
                val error = NetworkUtils.errorResponse(response.errorBody());
                mDocumentsListener.onFailed(error);

            }

            @Override
            public void onFailure(Call<DocumentResponse> call, Throwable t) {
                mDocumentsListener.onFailed(t.getLocalizedMessage());
            }
        });

    }

    //***********************************************************
    public void addDocument(@NonNull DocumentRequest documentRequest)
    //***********************************************************
    {
        val service = ServiceGenerator.createService(DocumentsService.class, true,
                Constants.BASE_URL_U);
        if (service == null) {
            mDocumentsListener.onFailed(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        Log.d("api_request", documentRequest.toString());

        val profile =
                documentRequest.getType().equals(Constants.DOCUMENT) ?
                        !TextUtils.isEmpty(documentRequest.getFile())
                                ? NetworkUtils.getDocumentFileMultiPartForm(
                                Uri.parse(documentRequest.getFile()),
                                "file", mActivity)
                                : null
                        :
                        !TextUtils.isEmpty(documentRequest.getFile())
                                ? NetworkUtils.getFileMultiPartForm(
                                Uri.parse(documentRequest.getFile()),
                                "file", mActivity)
                                : null;

        val user_id = Laila.instance().getMUser_U().getData().getUser().getId();
        val token = Laila.instance().getMUser_U().getData().getUser().getToken();

        val addDocumentsService = service.addDocuments
                (NetworkUtils.getMultiPartForm(String.valueOf(user_id)),
                        NetworkUtils.getMultiPartForm(String.valueOf(token)),
                        profile
                );

        addDocumentsService.enqueue(new Callback<DocumentResponse>() {
            @Override
            public void onResponse(Call<DocumentResponse> call, Response<DocumentResponse> response) {
                if (response.isSuccessful()) {

                    if (response.body().getStatus() == 200) {
                        mDocumentsListener.onRecordAddedSuccessfully(response.body());
                        return;
                    }

                    mDocumentsListener.onFailed((TextUtils.isEmpty(response.body().getData().getMessage()) ?
                            AndroidUtil.getString(R.string.server_error) :
                            response.body().getData().getMessage()));
                    return;
                }
                val error = NetworkUtils.errorResponse(response.errorBody());
                mDocumentsListener.onFailed(error);

            }

            @Override
            public void onFailure(Call<DocumentResponse> call, Throwable t) {
                mDocumentsListener.onFailed(t.getLocalizedMessage());
            }
        });

    }

    //***********************************************************
    public void deleteDocument(@NonNull int documentID)
    //***********************************************************
    {
        val service = ServiceGenerator.createService(DocumentsService.class, true,
                Constants.BASE_URL_U);
        if (service == null) {
            mDocumentsListener.onFailed(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        val token = Laila.instance().getMUser_U().getData().getUser().getToken();

        HashMap<String, Object> document = new HashMap<>();
        document.put("document_id", documentID);
        document.put(Constants.USER_TOKEN, token);

        val deleteDocumentsServices = service.deleteDocuments(document);


        deleteDocumentsServices.enqueue(new Callback<DocumentResponse>() {
            @Override
            public void onResponse(Call<DocumentResponse> call, Response<DocumentResponse> response) {
                if (response.isSuccessful()) {

                    if (response.body().getStatus() == 200) {
                        mDocumentsListener.onSuccessfullyDeleted(response.body().getMessage());
                        return;
                    }

                    mDocumentsListener.onFailed((TextUtils.isEmpty(response.body().getData().getMessage()) ?
                            AndroidUtil.getString(R.string.server_error) :
                            response.body().getData().getMessage()));
                    return;
                }
                mDocumentsListener.onFailed(AndroidUtil.getString(R.string.server_error));
            }

            @Override
            public void onFailure(Call<DocumentResponse> call, Throwable t) {
                mDocumentsListener.onFailed(t.getLocalizedMessage());
            }
        });

    }

    //***********************************************************
    public interface DocumentsViewListener
            //***********************************************************
    {
        void onSuccessfully(@Nullable DocumentResponse userResponse);

        void onRecordAddedSuccessfully(@Nullable DocumentResponse response);

        void onSuccessfullyDeleted(@Nullable String msg);

        void onFailed(@NonNull String errorMessage);
    }

}
