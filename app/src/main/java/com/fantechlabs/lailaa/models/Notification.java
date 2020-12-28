package com.fantechlabs.lailaa.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//*************************************************
public class Notification
    implements Parcelable
//*************************************************
{

    @SerializedName("user_private_code")
    @Expose
    private String userPrivateCode;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("app_type")
    @Expose
    private String appType;

    protected Notification(Parcel in) {
        userPrivateCode = in.readString();
        token = in.readString();
        appType = in.readString();
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

    public String getUserPrivateCode() { return userPrivateCode; }

    public void setUserPrivateCode(String userPrivateCode) { this.userPrivateCode = userPrivateCode; }

    public String getToken() { return token; }

    public void setToken(String token) { this.token = token; }

    public String getAppType() { return appType; }

    public void setAppType(String appType) { this.appType = appType; }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userPrivateCode);
        dest.writeString(token);
        dest.writeString(appType);
    }
}