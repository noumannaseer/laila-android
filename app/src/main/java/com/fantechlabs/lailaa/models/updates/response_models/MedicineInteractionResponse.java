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
public class MedicineInteractionResponse implements Parcelable {
    @SerializedName("Status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private Data data;

    protected MedicineInteractionResponse(Parcel in) {
        if (in.readByte() == 0) {
            status = null;
        } else {
            status = in.readInt();
        }
        data = in.readParcelable(Data.class.getClassLoader());
    }

    public static final Creator<MedicineInteractionResponse> CREATOR = new Creator<MedicineInteractionResponse>() {
        @Override
        public MedicineInteractionResponse createFromParcel(Parcel in) {
            return new MedicineInteractionResponse(in);
        }

        @Override
        public MedicineInteractionResponse[] newArray(int size) {
            return new MedicineInteractionResponse[size];
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
