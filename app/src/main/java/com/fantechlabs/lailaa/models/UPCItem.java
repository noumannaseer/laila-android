package com.fantechlabs.lailaa.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//*****************************************
public class UPCItem
    implements Parcelable
//*****************************************
{
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("upc")
    @Expose
    private String upc;

    protected UPCItem(Parcel in) {
        title = in.readString();
        upc = in.readString();
    }

    public static final Creator<UPCItem> CREATOR = new Creator<UPCItem>() {
        @Override
        public UPCItem createFromParcel(Parcel in) {
            return new UPCItem(in);
        }

        @Override
        public UPCItem[] newArray(int size) {
            return new UPCItem[size];
        }
    };

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getUpc() { return upc; }

    public void setUpc(String upc) { this.upc = upc; }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(upc);
    }
}
