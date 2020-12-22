package com.fantechlabs.lailaa.request_models;


import com.fantechlabs.lailaa.models.Profile;
import com.fantechlabs.lailaa.models.ProfileImages;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

//******************************************
public class ProfileRequest
//******************************************
{

    @SerializedName("profile")
    @Expose
    private Profile profile;
    @SerializedName("images")
    @Expose
    private List<ProfileImages> images;

    public Profile getProfile() { return profile; }

    public void setProfile(Profile profile) { this.profile = profile; }

    public List<ProfileImages> getImages() { return images; }

    public void setImages(List<ProfileImages> images) { this.images = images; }
}