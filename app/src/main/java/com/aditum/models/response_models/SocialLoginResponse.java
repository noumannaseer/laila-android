package com.aditum.models.response_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SocialLoginResponse
{

    @SerializedName("signup")
    @Expose
    private SignUpResponse signUpResponse;
    @SerializedName("login")
    @Expose
    private UserResponse userResponse;

}
