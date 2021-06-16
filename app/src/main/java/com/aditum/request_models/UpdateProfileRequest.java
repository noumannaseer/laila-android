package com.aditum.request_models;

import android.media.Image;

import com.aditum.models.Profile;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UpdateProfileRequest
{

    @SerializedName("profile")
    @Expose
    private Profile profile;
    @SerializedName("images")
    @Expose
    private List<Image> images = null;

    public Profile getProfile() { return profile; }

    public void setProfile(Profile profile) { this.profile = profile; }

    public List<Image> getImages() { return images; }

    public void setImages(List<Image> images) { this.images = images; }

}