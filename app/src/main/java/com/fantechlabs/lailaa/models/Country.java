package com.fantechlabs.lailaa.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

//*******************************************
public class Country
    implements Parcelable
//*******************************************
{

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone_code")
    @Expose
    private String phoneCode;
    @SerializedName("states")
    @Expose
    private List<State> states;

    protected Country(Parcel in) {
        name = in.readString();
        phoneCode = in.readString();
    }

    public static final Creator<Country> CREATOR = new Creator<Country>() {
        @Override
        public Country createFromParcel(Parcel in) {
            return new Country(in);
        }

        @Override
        public Country[] newArray(int size) {
            return new Country[size];
        }
    };

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getPhoneCode() { return phoneCode; }

    public void setPhoneCode(String phoneCode) { this.phoneCode = phoneCode; }

    public List<State> getStates() { return states; }

    public void setStates(List<State> states) { this.states = states; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phoneCode);
    }
}
