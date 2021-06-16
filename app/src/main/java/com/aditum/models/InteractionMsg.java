package com.aditum.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//*******************************************
public class InteractionMsg
    implements Parcelable
//*******************************************
{

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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getSeverityId() {
        return severityId;
    }

    public void setSeverityId(Integer severityId) {
        this.severityId = severityId;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

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