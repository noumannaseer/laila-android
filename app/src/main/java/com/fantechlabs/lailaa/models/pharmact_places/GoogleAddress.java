package com.fantechlabs.lailaa.models.pharmact_places;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GoogleAddress
    implements Parcelable
{

    @SerializedName("result")
    @Expose
    private Result result;
    @SerializedName("status")
    @Expose
    private String status;

    public GoogleAddress(){}

    protected GoogleAddress(Parcel in) {
        result = in.readParcelable(Result.class.getClassLoader());
        status = in.readString();
    }

    public static final Creator<GoogleAddress> CREATOR = new Creator<GoogleAddress>() {
        @Override
        public GoogleAddress createFromParcel(Parcel in) {
            return new GoogleAddress(in);
        }

        @Override
        public GoogleAddress[] newArray(int size) {
            return new GoogleAddress[size];
        }
    };

    public Result getResult() { return result; }

    public void setResult(Result result) { this.result = result; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeParcelable(result, flags);
        dest.writeString(status);
    }
}
