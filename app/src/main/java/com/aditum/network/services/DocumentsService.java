package com.aditum.network.services;


import com.aditum.models.updates.response_models.AvatarResponse;
import com.aditum.models.updates.response_models.DocumentResponse;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface DocumentsService {

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("documents/get_documents")
    Call<DocumentResponse>
    getDocuments(
            @Body HashMap<String, String> documentRequest
    );
    //***************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("documents/upload_document")
    @Multipart
    Call<DocumentResponse>
    addDocuments(
            @Part("user_id") RequestBody user_id,
            @Part("token") RequestBody token,
            @Part MultipartBody.Part imageFile
    );
    //**************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("users/upload_avatar")
    @Multipart
    Call<AvatarResponse>
    uploadAvatar(
            @Part("user_id") RequestBody user_id,
            @Part("token") RequestBody token,
            @Part MultipartBody.Part imageFile
//            @Body Map<String, String> imageFile
    );
    //**************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("documents/delete_document")
    Call<DocumentResponse>
    deleteDocuments(@Body HashMap<String, Object> deleteDocument);
    //**************************************************************
}
