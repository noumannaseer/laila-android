package com.aditum.models.updates.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//*************************************************
public class Document implements Parcelable
//*************************************************
{
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("file_name")
    @Expose
    private String fileName;
    @SerializedName("file_type")
    @Expose
    private String fileType;
    @SerializedName("file_size")
    @Expose
    private String fileSize;
    @SerializedName("file_url")
    @Expose
    private String fileUrl;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("created_at")
    @Expose
    private long createdAt;

    protected Document(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        if (in.readByte() == 0) {
            userId = null;
        } else {
            userId = in.readInt();
        }
        fileName = in.readString();
        fileType = in.readString();
        fileSize = in.readString();
        fileUrl = in.readString();
        thumbnail = in.readString();
        createdAt = in.readLong();
    }

    public static final Creator<Document> CREATOR = new Creator<Document>() {
        @Override
        public Document createFromParcel(Parcel in) {
            return new Document(in);
        }

        @Override
        public Document[] newArray(int size) {
            return new Document[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(id);
        }
        if (userId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(userId);
        }
        parcel.writeString(fileName);
        parcel.writeString(fileType);
        parcel.writeString(fileSize);
        parcel.writeString(fileUrl);
        parcel.writeString(thumbnail);
        parcel.writeLong(createdAt);
    }
}
