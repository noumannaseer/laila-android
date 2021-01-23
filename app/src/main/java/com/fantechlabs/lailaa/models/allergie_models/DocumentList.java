package com.fantechlabs.lailaa.models.allergie_models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class DocumentList implements Parcelable {
    private String rank;
    private String url;
    private String term;
    private int count;
    private int num;
    private int start;
    private int per;
    private HashMap<String, String> documentList;

    protected DocumentList(Parcel in) {
        rank = in.readString();
        url = in.readString();
        term = in.readString();
        num = in.readInt();
        start = in.readInt();
        per = in.readInt();
    }

    public static final Creator<DocumentList> CREATOR = new Creator<DocumentList>() {
        @Override
        public DocumentList createFromParcel(Parcel in) {
            return new DocumentList(in);
        }

        @Override
        public DocumentList[] newArray(int size) {
            return new DocumentList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(rank);
        dest.writeString(url);
        dest.writeString(term);
        dest.writeInt(num);
        dest.writeInt(start);
        dest.writeInt(per);
    }
}

