package com.fantechlabs.lailaa.models.updates.response_models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fantechlabs.lailaa.models.updates.models.Data;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//*****************************************************************
public class AddHealthDataResponse implements Parcelable
//*****************************************************************
{
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private Data data;

    protected AddHealthDataResponse(Parcel in) {
        if (in.readByte() == 0) {
            status = null;
        } else {
            status = in.readInt();
        }
        data = in.readParcelable(Data.class.getClassLoader());
    }

    public static final Creator<AddHealthDataResponse> CREATOR = new Creator<AddHealthDataResponse>() {
        @Override
        public AddHealthDataResponse createFromParcel(Parcel in) {
            return new AddHealthDataResponse(in);
        }

        @Override
        public AddHealthDataResponse[] newArray(int size) {
            return new AddHealthDataResponse[size];
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
