package com.fantechlabs.lailaa.bodyreading.repository.storge.responsemodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.fantechlabs.lailaa.bodyreading.repository.storge.model.HealthReadSuccess;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//************************************************
public class HealthDataReadingResponse
    implements Parcelable
//************************************************
{
    @SerializedName("status_code")
    @Expose
    private int status_code;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("success")
    @Expose
    private HealthReadSuccess success;

    protected HealthDataReadingResponse(Parcel in) {
        status_code = in.readInt();
        error = in.readString();
        success = in.readParcelable(HealthReadSuccess.class.getClassLoader());
    }

    public static final Creator<HealthDataReadingResponse> CREATOR = new Creator<HealthDataReadingResponse>() {
        @Override
        public HealthDataReadingResponse createFromParcel(Parcel in) {
            return new HealthDataReadingResponse(in);
        }

        @Override
        public HealthDataReadingResponse[] newArray(int size) {
            return new HealthDataReadingResponse[size];
        }
    };

    public int getStatus_code() { return status_code; }

    public void setStatus_code(int status_code) { this.status_code = status_code; }

    public String getError() { return error; }

    public void setError(String error) { this.error = error; }

    public HealthReadSuccess getSuccess() { return success; }

    public void setSuccess(HealthReadSuccess success) { this.success = success; }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(status_code);
        dest.writeString(error);
        dest.writeParcelable(success, flags);
    }
}
