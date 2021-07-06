package com.aditum.models.response_models;

import android.os.Parcel;
import android.os.Parcelable;

import com.aditum.models.updates.models.Data;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QuestionsResponse implements Parcelable {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private Data data;

    protected QuestionsResponse(Parcel in) {
        if (in.readByte() == 0) {
            status = null;
        } else {
            status = in.readInt();
        }
        data = in.readParcelable(Data.class.getClassLoader());
    }

    public static final Creator<QuestionsResponse> CREATOR = new Creator<QuestionsResponse>() {
        @Override
        public QuestionsResponse createFromParcel(Parcel in) {
            return new QuestionsResponse(in);
        }

        @Override
        public QuestionsResponse[] newArray(int size) {
            return new QuestionsResponse[size];
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
