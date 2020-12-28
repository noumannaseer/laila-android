package com.fantechlabs.lailaa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//*********************************************************
public class ProfileImages
//*********************************************************
{
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("user_private_code")
    @Expose
    private String userPrivateCode;
    @SerializedName("image_title")
    @Expose
    private String imageTitle;
    @SerializedName("image_description")
    @Expose
    private String imageDescription;
    @SerializedName("image_data")
    @Expose
    private String imageData;

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public String getUserPrivateCode() { return userPrivateCode; }

    public void setUserPrivateCode(String userPrivateCode) { this.userPrivateCode = userPrivateCode; }

    public String getImageTitle() { return imageTitle; }

    public void setImageTitle(String imageTitle) { this.imageTitle = imageTitle; }

    public String getImageDescription() { return imageDescription; }

    public void setImageDescription(String imageDescription) { this.imageDescription = imageDescription; }

    public String getImageData() { return imageData; }

    public void setImageData(String imageData) { this.imageData = imageData; }
}
