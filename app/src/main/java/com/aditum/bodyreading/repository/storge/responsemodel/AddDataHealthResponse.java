package com.aditum.bodyreading.repository.storge.responsemodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.aditum.bodyreading.repository.storge.model.Success;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//*******************************************************
public class AddDataHealthResponse
    implements Parcelable
//*******************************************************
{
    @SerializedName("status_code")
    @Expose
    private int status_code;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("success")
    @Expose
    private Success success;

    public AddDataHealthResponse(){}

    protected AddDataHealthResponse(Parcel in) {
        status_code = in.readInt();
        error = in.readString();
    }

    public static final Creator<AddDataHealthResponse> CREATOR = new Creator<AddDataHealthResponse>() {
        @Override
        public AddDataHealthResponse createFromParcel(Parcel in) {
            return new AddDataHealthResponse(in);
        }

        @Override
        public AddDataHealthResponse[] newArray(int size) {
            return new AddDataHealthResponse[size];
        }
    };

    public int getStatus_code() { return status_code; }

    public void setStatus_code(int status_code) { this.status_code = status_code; }

    public String getError() { return error; }

    public void setError(String error) { this.error = error; }

    public Success getSuccess() { return success; }

    public void setSuccess(Success success) { this.success = success; }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(status_code);
        dest.writeString(error);
    }
}
