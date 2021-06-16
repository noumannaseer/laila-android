package com.aditum.models.updates.response_models;

import android.os.Parcel;
import android.os.Parcelable;

import com.aditum.models.updates.models.Data;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//********************************************
public class ReadHealthDataResponse implements Parcelable
//********************************************
{
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private Data data;

    protected ReadHealthDataResponse(Parcel in) {
        if (in.readByte() == 0) {
            status = null;
        } else {
            status = in.readInt();
        }
        data = in.readParcelable(Data.class.getClassLoader());
    }

    public static final Creator<ReadHealthDataResponse> CREATOR = new Creator<ReadHealthDataResponse>() {
        @Override
        public ReadHealthDataResponse createFromParcel(Parcel in) {
            return new ReadHealthDataResponse(in);
        }

        @Override
        public ReadHealthDataResponse[] newArray(int size) {
            return new ReadHealthDataResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (status == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(status);
        }
        parcel.writeParcelable(data, i);
    }
}
