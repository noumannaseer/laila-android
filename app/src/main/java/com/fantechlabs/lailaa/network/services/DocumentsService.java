package com.fantechlabs.lailaa.network.services;


import com.fantechlabs.lailaa.models.Document;
import com.fantechlabs.lailaa.models.response_models.DocumentResponse;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface DocumentsService
{

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("documents/")
    Call<DocumentResponse>
    getDocuments(
            @Body HashMap<String, String> documentRequest
            );
    //***************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("document/add/")
    @Multipart
    Call<Document>
    addDocuments(
            @Part("user_private_code") RequestBody user_private_code,
            @Part MultipartBody.Part imageFile
    );
    //**************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("document/delete/")
    Call<DocumentResponse>
    deleteDocuments(  @Body HashMap<String, Object> deleteDocument );
    //**************************************************************
}
