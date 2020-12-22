package com.fantechlabs.lailaa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//***************************************
public class SpecialImages
//***************************************
{

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("redirect_url")
    @Expose
    private String redirectUrl;
    @SerializedName("url")
    @Expose
    private String url;

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getRedirectUrl() { return redirectUrl; }

    public void setRedirectUrl(String redirectUrl) { this.redirectUrl = redirectUrl; }

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }

}