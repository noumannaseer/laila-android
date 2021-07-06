package com.aditum.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
import retrofit2.http.GET;

@Setter
@Getter
public class Questions implements Parcelable {
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("question_1")
    @Expose
    private String question1;
    @SerializedName("question_2")
    @Expose
    private String question2;
    @SerializedName("question_3")
    @Expose
    private String question3;
    @SerializedName("question_4")
    @Expose
    private String question4;
    @SerializedName("question_5")
    @Expose
    private String question5;
    @SerializedName("question_6")
    @Expose
    private String question6;
    @SerializedName("question_7")
    @Expose
    private String question7;
    @SerializedName("question_8")
    @Expose
    private String question8;
    @SerializedName("created")
    @Expose
    private String created;

    protected Questions(Parcel in) {
        if (in.readByte() == 0) {
            userId = null;
        } else {
            userId = in.readInt();
        }
        question1 = in.readString();
        question2 = in.readString();
        question3 = in.readString();
        question4 = in.readString();
        question5 = in.readString();
        question6 = in.readString();
        question7 = in.readString();
        question8 = in.readString();
        created = in.readString();
    }

    public static final Creator<Questions> CREATOR = new Creator<Questions>() {
        @Override
        public Questions createFromParcel(Parcel in) {
            return new Questions(in);
        }

        @Override
        public Questions[] newArray(int size) {
            return new Questions[size];
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
        dest.writeString(question1);
        dest.writeString(question2);
        dest.writeString(question3);
        dest.writeString(question4);
        dest.writeString(question5);
        dest.writeString(question6);
        dest.writeString(question7);
        dest.writeString(question8);
        dest.writeString(created);
    }
}
