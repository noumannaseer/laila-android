package com.aditum.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

//*************************************
public class Special
//*************************************
{

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("images")
    @Expose
    private List<SpecialImages> images;

    public Integer getCode() { return code; }

    public void setCode(Integer code) { this.code = code; }

    public String getMsg() { return msg; }

    public void setMsg(String msg) { this.msg = msg; }

    public List<SpecialImages> getImages() { return images; }

    public void setImages(List<SpecialImages> images) { this.images = images; }
}