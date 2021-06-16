package com.aditum.models.response_models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//**************************************
public class PasswordResponse
        implements Parcelable
//**************************************
{

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("error")
    @Expose
    private String error;

    protected PasswordResponse(Parcel in) {
        result = in.readString();
        error = in.readString();
    }

    public static final Creator<PasswordResponse> CREATOR = new Creator<PasswordResponse>() {
        @Override
        public PasswordResponse createFromParcel(Parcel in) {
            return new PasswordResponse(in);
        }

        @Override
        public PasswordResponse[] newArray(int size) {
            return new PasswordResponse[size];
        }
    };

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(result);
        dest.writeString(error);
    }
}
