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
public class EmergencyContactResponse implements Parcelable {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private Data data;

    protected EmergencyContactResponse(Parcel in) {
        if (in.readByte() == 0) {
            status = null;
        } else {
            status = in.readInt();
        }
        data = in.readParcelable(Data.class.getClassLoader());
    }

    public static final Creator<EmergencyContactResponse> CREATOR = new Creator<EmergencyContactResponse>() {
        @Override
        public EmergencyContactResponse createFromParcel(Parcel in) {
            return new EmergencyContactResponse(in);
        }

        @Override
        public EmergencyContactResponse[] newArray(int size) {
            return new EmergencyContactResponse[size];
        }
    };

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
        dest.writeParcelable(data, flags);
    }
}
