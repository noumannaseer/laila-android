package com.aditum.models.response_models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

//**************************************
public class RefillRemindersResponse
    implements Parcelable
//**************************************
{

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("ids")
    @Expose
    private List<Integer> ids = null;

    protected RefillRemindersResponse(Parcel in) {
        code = in.readString();
        msg = in.readString();
    }

    public static final Creator<RefillRemindersResponse> CREATOR = new Creator<RefillRemindersResponse>() {
        @Override
        public RefillRemindersResponse createFromParcel(Parcel in) {
            return new RefillRemindersResponse(in);
        }

        @Override
        public RefillRemindersResponse[] newArray(int size) {
            return new RefillRemindersResponse[size];
        }
    };

    public String getCode() { return code; }

    public void setCode(String code) { this.code = code; }

    public String getMsg() { return msg; }

    public void setMsg(String msg) { this.msg = msg; }

    public List<Integer> getIds() { return ids; }

    public void setIds(List<Integer> ids) { this.ids = ids; }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(msg);
    }
}
