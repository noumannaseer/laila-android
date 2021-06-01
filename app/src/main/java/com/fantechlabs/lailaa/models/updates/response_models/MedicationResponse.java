package com.fantechlabs.lailaa.models.updates.response_models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fantechlabs.lailaa.models.updates.models.Data;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
//*********************************************************
public class MedicationResponse implements Parcelable
//*********************************************************
{
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("data")
    @Expose
    private Data data;

    protected MedicationResponse(Parcel in) {
        status = in.readInt();
        data = in.readParcelable(Data.class.getClassLoader());
    }

    public static final Creator<MedicationResponse> CREATOR = new Creator<MedicationResponse>() {
        @Override
        public MedicationResponse createFromParcel(Parcel in) {
            return new MedicationResponse(in);
        }

        @Override
        public MedicationResponse[] newArray(int size) {
            return new MedicationResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(status);
        parcel.writeParcelable(data, i);
    }
}
