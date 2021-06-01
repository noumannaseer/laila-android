package com.fantechlabs.lailaa.models.updates.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//*******************************************************
public class BodyVital implements Parcelable
//*******************************************************
{
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("heart_rate")
    @Expose
    private Integer heartRate;
    @SerializedName("bp_systolic")
    @Expose
    private Integer bpSystolic;
    @SerializedName("bp_diastolic")
    @Expose
    private Integer bpDiastolic;
    @SerializedName("blood_sugar")
    @Expose
    private Integer bloodSugar;
    @SerializedName("created_at")
    @Expose
    private long createdAt;

    protected BodyVital(Parcel in) {
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
        if (in.readByte() == 0) {
            heartRate = null;
        } else {
            heartRate = in.readInt();
        }
        if (in.readByte() == 0) {
            bpSystolic = null;
        } else {
            bpSystolic = in.readInt();
        }
        if (in.readByte() == 0) {
            bpDiastolic = null;
        } else {
            bpDiastolic = in.readInt();
        }
        if (in.readByte() == 0) {
            bloodSugar = null;
        } else {
            bloodSugar = in.readInt();
        }
        createdAt = in.readLong();
    }

    public static final Creator<BodyVital> CREATOR = new Creator<BodyVital>() {
        @Override
        public BodyVital createFromParcel(Parcel in) {
            return new BodyVital(in);
        }

        @Override
        public BodyVital[] newArray(int size) {
            return new BodyVital[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        if (userId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(userId);
        }
        if (heartRate == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(heartRate);
        }
        if (bpSystolic == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(bpSystolic);
        }
        if (bpDiastolic == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(bpDiastolic);
        }
        if (bloodSugar == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(bloodSugar);
        }
        dest.writeLong(createdAt);
    }
}
