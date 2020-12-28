package com.fantechlabs.lailaa.models.response_models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fantechlabs.lailaa.models.SearchMedicine;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

//******************************************
public class UpcResponse
    implements Parcelable
//******************************************
{
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("offset")
    @Expose
    private Integer offset;
    @SerializedName("items")
    @Expose
    private List<SearchMedicine> items ;


    protected UpcResponse(Parcel in) {
        code = in.readString();
        message = in.readString();
        if (in.readByte() == 0) {
            total = null;
        } else {
            total = in.readInt();
        }
        if (in.readByte() == 0) {
            offset = null;
        } else {
            offset = in.readInt();
        }
    }

    public static final Creator<UpcResponse> CREATOR = new Creator<UpcResponse>() {
        @Override
        public UpcResponse createFromParcel(Parcel in) {
            return new UpcResponse(in);
        }

        @Override
        public UpcResponse[] newArray(int size) {
            return new UpcResponse[size];
        }
    };

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) { this.offset = offset; }

    public List<SearchMedicine> getItems() {
        return items;
    }

    public void setItems(List<SearchMedicine> items) {
        this.items = items;
    }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(message);
        if (total == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(total);
        }
        if (offset == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(offset);
        }
    }
}
