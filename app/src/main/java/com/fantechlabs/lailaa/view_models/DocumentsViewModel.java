package com.fantechlabs.lailaa.view_models;

import android.app.Activity;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.models.Document;
import com.fantechlabs.lailaa.models.response_models.DocumentResponse;
import com.fantechlabs.lailaa.network.NetworkUtils;
import com.fantechlabs.lailaa.network.ServiceGenerator;
import com.fantechlabs.lailaa.network.services.DocumentsService;
import com.fantechlabs.lailaa.request_models.DocumentRequest;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;

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
                Constants.BASE_URL2);
        if (service == null) {
            mDocumentsListener.onFailed(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        val userPrivateCode = Laila.instance().getMUser().getProfile().getUserPrivateCode();

        HashMap<String, String> document = new HashMap<>();
        document.put(Constants.USER_PRIVATE_CODE, userPrivateCode);

        val getDocumentsServices = service.getDocuments(document);


        getDocumentsServices.enqueue(new Callback<DocumentResponse>() {
            @Override
            public void onResponse(Call<DocumentResponse> call, Response<DocumentResponse> response) {
                if (response.isSuccessful()) {

                    if (response.body().getCode() == 200) {
                        mDocumentsListener.onSuccessfully(response.body());
                        return;
                    }

                    mDocumentsListener.onFailed((TextUtils.isEmpty(response.body().getMsg()) ?
                            AndroidUtil.getString(R.string.server_error) :
                            response.body().getMsg()));
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
    public void addDocument(@NonNull DocumentRequest documentRequest)
    //***********************************************************
    {
        val service = ServiceGenerator.createService(DocumentsService.class, true,
                Constants.BASE_URL2);
        if (service == null) {
            mDocumentsListener.onFailed(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        Log.d("api_request", documentRequest.toString());
        val profile = documentRequest.getType().equals(Constants.DOCUMENT) ?
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

        val userPrivateCode = Laila.instance().getMUser().getProfile().getUserPrivateCode();

        val addDocumentsService = service.addDocuments(
                NetworkUtils.getMultiPartForm(String.valueOf(userPrivateCode)),
                profile
        );

        addDocumentsService.enqueue(new Callback<Document>() {
            @Override
            public void onResponse(Call<Document> call, Response<Document> response) {
                if (response.isSuccessful()) {

                    if (response.body().getCode() == 200) {
                        mDocumentsListener.onRecordAddedSuccessfully(response.body());
                        return;
                    }

                    mDocumentsListener.onFailed((TextUtils.isEmpty(response.body().getMsg()) ?
                            AndroidUtil.getString(R.string.server_error) :
                            response.body().getMsg()));
                    return;
                }
                mDocumentsListener.onFailed(AndroidUtil.getString(R.string.server_error));

            }

            @Override
            public void onFailure(Call<Document> call, Throwable t) {
                mDocumentsListener.onFailed(t.getLocalizedMessage());
            }
        });

    }

    //***********************************************************
    public void deleteDocument(@NonNull int documentID)
    //***********************************************************
    {
        val service = ServiceGenerator.createService(DocumentsService.class, true,
                Constants.BASE_URL2);
        if (service == null) {
            mDocumentsListener.onFailed(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        val userPrivateCode = Laila.instance().getMUser().getProfile().getUserPrivateCode();

        HashMap<String, Object> document = new HashMap<>();
        document.put("record_id", documentID);
        document.put(Constants.USER_PRIVATE_CODE, userPrivateCode);

        val deleteDocumentsServices = service.deleteDocuments(document);


        deleteDocumentsServices.enqueue(new Callback<DocumentResponse>() {
            @Override
            public void onResponse(Call<DocumentResponse> call, Response<DocumentResponse> response) {
                if (response.isSuccessful()) {

                    if (response.body().getCode() == 200) {
                        mDocumentsListener.onSuccessfullyDeleted(response.body().getMsg());
                        return;
                    }

                    mDocumentsListener.onFailed((TextUtils.isEmpty(response.body().getMsg()) ?
                            AndroidUtil.getString(R.string.server_error) :
                            response.body().getMsg()));
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

        void onRecordAddedSuccessfully(@Nullable Document response);

        void onSuccessfullyDeleted(@Nullable String msg);

        void onFailed(@NonNull String errorMessage);
    }

}
