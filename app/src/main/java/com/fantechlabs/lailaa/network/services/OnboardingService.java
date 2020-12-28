package com.fantechlabs.lailaa.network.services;


import com.fantechlabs.lailaa.models.Profile;
import com.fantechlabs.lailaa.models.onboarding.Meta;
import com.fantechlabs.lailaa.models.response_models.PasswordResponse;
import com.fantechlabs.lailaa.models.response_models.SignUpResponse;
import com.fantechlabs.lailaa.models.response_models.UpdateProfileResponse;
import com.fantechlabs.lailaa.models.response_models.UserResponse;
import com.fantechlabs.lailaa.request_models.ProfileRequest;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface OnboardingService {

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("login/1")
    Call<UserResponse>
    login(
            @Body Map<String, String> login
    );
    //**************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("signup/1")
    Call<SignUpResponse>
    signUp(
            @Body Map<String, String> signup
    );


    //**************************************************************
    @Headers("Accept: application/json")
    @POST("signup/1")
    Call<SignUpResponse>
    signUpBody(
            @Body Profile profile
    );

    //**************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("reset_password")
    @Multipart
    Call<Meta>
    forgotPassWord(
            @Part("email") RequestBody deviceType,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude
    );
    //**************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("updateprofile/1")
    Call<UpdateProfileResponse>
    updateProfile(@Body ProfileRequest profileRequest);
    //**************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("social_login/1")
    Call<UserResponse>
    socialLogin(
            @Body Map<String, String> socialLogin
    );
    //**************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("resetpassword/1")
    Call<PasswordResponse>
    resetPassword(
            @Body Map<String, String> resetPassword
    );
    //**************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("changepassword/1")
    Call<PasswordResponse>
    updatePassword(
            @Body Map<String, String> updatePassword
    );
    //**************************************************************

}
