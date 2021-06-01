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
//************************************************
public class DocumentResponse implements Parcelable
//************************************************
{
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

    protected DocumentResponse(Parcel in) {
        if (in.readByte() == 0) {
            status = null;
        } else {
            status = in.readInt();
        }
        message = in.readString();
        data = in.readParcelable(Data.class.getClassLoader());
    }

    public static final Creator<DocumentResponse> CREATOR = new Creator<DocumentResponse>() {
        @Override
        public DocumentResponse createFromParcel(Parcel in) {
            return new DocumentResponse(in);
        }

        @Override
        public DocumentResponse[] newArray(int size) {
            return new DocumentResponse[size];
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
        parcel.writeString(message);
        parcel.writeParcelable(data, i);
    }
}
