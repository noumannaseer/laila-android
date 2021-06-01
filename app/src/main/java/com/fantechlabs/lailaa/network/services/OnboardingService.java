package com.fantechlabs.lailaa.network.services;


import com.fantechlabs.lailaa.models.Profile;
import com.fantechlabs.lailaa.models.onboarding.Meta;
import com.fantechlabs.lailaa.models.response_models.PasswordResponse;
import com.fantechlabs.lailaa.models.response_models.UpdateProfileResponse;
import com.fantechlabs.lailaa.models.updates.request_models.ProfileRequest;
import com.fantechlabs.lailaa.models.updates.response_models.ProfileResponse;
import com.fantechlabs.lailaa.models.updates.response_models.SignUpResponse;
import com.fantechlabs.lailaa.models.updates.response_models.UserResponse;

import java.util.HashMap;
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
    @POST("users/login")
    Call<UserResponse>
    login(
            @Body Map<String, String> login
    );
    //**************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("users/signup")
    Call<SignUpResponse>
    signUp(
            @Body Map<String, String> signup
    );
    //**************************************************************

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
    @POST("users/update_profile")
    Call<ProfileResponse>
    updateProfile(@Body ProfileRequest profileRequest);
    //**************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("users/Get_profile")
    Call<ProfileResponse>
    getProfile(
            @Body HashMap<String, String> profileRequest
    );
    //**************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("users/social_login")
    Call<UserResponse>
    socialLogin(
            @Body Map<String, String> socialLogin
    );
    //**************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("users/forgot_password")
    Call<PasswordResponse>
    resetPassword(
            @Body Map<String, String> resetPassword
    );
    //**************************************************************

    //**************************************************************
    @Headers("Accept: application/json")
    @POST("users/reset_password")
    Call<UserResponse>
    updatePassword(
            @Body Map<String, String> updatePassword
    );
    //**************************************************************

}
