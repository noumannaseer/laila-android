package com.fantechlabs.lailaa.models.response_models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fantechlabs.lailaa.models.Document;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DocumentResponse implements Parcelable
{

    @SerializedName("record_urls")
    @Expose
    private List<Document> documentList;
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("msg")
    @Expose
    private String msg;


    protected DocumentResponse(Parcel in) {
        documentList = in.createTypedArrayList(Document.CREATOR);
        if (in.readByte() == 0) {
            code = null;
        } else {
            code = in.readInt();
        }
        msg = in.readString();
    }

    public static final Creator<DocumentResponse> CREATOR = new Creator<DocumentResponse>() {
        @Override
        public DocumentResponse createFromParcel(Parcel in) {
            return new DocumentResponse(in);
        }

        @Override
        public DocumentResponse[] newArray(int size) {
            return new DocumentResponse[size];
        }
    };

    public List<Document> getDocumentList() { return documentList; }

    public void setDocumentList(List<Document> documentList) { this.documentList = documentList; }

    public Integer getCode() { return code; }

    public void setCode(Integer code) { this.code = code; }

    public String getMsg() { return msg; }

    public void setMsg(String msg) { this.msg = msg; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(documentList);
        if (code == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(code);
        }
        dest.writeString(msg);
    }
}
