package com.aditum.models.updates.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//*********************************************
public class Avatar implements Parcelable
//*********************************************
{
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("avatar_url")
    @Expose
    private String avatarUrl;
    @SerializedName("created_at")
    @Expose
    private long createdAt;

    protected Avatar(Parcel in) {
        if (in.readByte() == 0) {
            userId = null;
        } else {
            userId = in.readInt();
        }
        avatarUrl = in.readString();
        createdAt = in.readLong();
    }

    public static final Creator<Avatar> CREATOR = new Creator<Avatar>() {
        @Override
        public Avatar createFromParcel(Parcel in) {
            return new Avatar(in);
        }

        @Override
        public Avatar[] newArray(int size) {
            return new Avatar[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (userId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(userId);
        }
        dest.writeString(avatarUrl);
        dest.writeLong(createdAt);
    }
}
