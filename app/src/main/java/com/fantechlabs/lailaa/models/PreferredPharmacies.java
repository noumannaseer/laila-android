package com.fantechlabs.lailaa.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

//***************************************************
public class PreferredPharmacies
    implements Parcelable
//***************************************************
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("preferred_ids")
    @Expose
    private List<String> preferredIds = null;


    public PreferredPharmacies(){}
    
    protected PreferredPharmacies(Parcel in) {
        if (in.readByte() == 0) {
            status = null;
        } else {
            status = in.readInt();
        }
        msg = in.readString();
        if (in.readByte() == 0) {
            count = null;
        } else {
            count = in.readInt();
        }
        preferredIds = in.createStringArrayList();
    }

    public static final Creator<PreferredPharmacies> CREATOR = new Creator<PreferredPharmacies>() {
        @Override
        public PreferredPharmacies createFromParcel(Parcel in) {
            return new PreferredPharmacies(in);
        }

        @Override
        public PreferredPharmacies[] newArray(int size) {
            return new PreferredPharmacies[size];
        }
    };

    public Integer getStatus() { return status; }

    public void setStatus(Integer status) { this.status = status; }

    public String getMsg() { return msg; }

    public void setMsg(String msg) { this.msg = msg; }

    public Integer getCount() { return count; }

    public void setCount(Integer count) { this.count = count; }

    public List<String> getPreferredIds() { return preferredIds; }

    public void setPreferredIds(List<String> preferredIds) { this.preferredIds = preferredIds; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (status == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(status);
        }
        dest.writeString(msg);
        if (count == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(count);
        }
        dest.writeStringList(preferredIds);
    }
}