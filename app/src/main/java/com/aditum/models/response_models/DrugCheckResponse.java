package com.aditum.models.response_models;

import android.os.Parcel;
import android.os.Parcelable;

import com.aditum.models.InteractionMsg;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

//*******************************************
public class DrugCheckResponse
    implements Parcelable
//*******************************************
{

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("interaction_msgs")
    @Expose
    private List<InteractionMsg> interactionMsgs = null;

    protected DrugCheckResponse(Parcel in) {
        code = in.readString();
        msg = in.readString();
        interactionMsgs = in.createTypedArrayList(InteractionMsg.CREATOR);
    }

    public static final Creator<DrugCheckResponse> CREATOR = new Creator<DrugCheckResponse>() {
        @Override
        public DrugCheckResponse createFromParcel(Parcel in) {
            return new DrugCheckResponse(in);
        }

        @Override
        public DrugCheckResponse[] newArray(int size) {
            return new DrugCheckResponse[size];
        }
    };

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<InteractionMsg> getInteractionMsgs() {
        return interactionMsgs;
    }

    public void setInteractionMsgs(List<InteractionMsg> interactionMsgs) {
        this.interactionMsgs = interactionMsgs;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(msg);
        dest.writeTypedList(interactionMsgs);
    }
}
