package com.fantechlabs.lailaa.models.updates.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class InteractionMsg implements Parcelable {
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("object")
    @Expose
    private String object;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("severityId")
    @Expose
    private Integer severityId;
    @SerializedName("severity")
    @Expose
    private String severity;

    protected InteractionMsg(Parcel in) {
        subject = in.readString();
        object = in.readString();
        text = in.readString();
        if (in.readByte() == 0) {
            severityId = null;
        } else {
            severityId = in.readInt();
        }
        severity = in.readString();
    }

    public static final Creator<InteractionMsg> CREATOR = new Creator<InteractionMsg>() {
        @Override
        public InteractionMsg createFromParcel(Parcel in) {
            return new InteractionMsg(in);
        }

        @Override
        public InteractionMsg[] newArray(int size) {
            return new InteractionMsg[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(subject);
        dest.writeString(object);
        dest.writeString(text);
        if (severityId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(severityId);
        }
        dest.writeString(severity);
    }
}
