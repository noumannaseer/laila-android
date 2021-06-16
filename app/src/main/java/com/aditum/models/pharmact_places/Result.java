package com.aditum.models.pharmact_places;

import android.os.Parcel;
import android.os.Parcelable;

import com.aditum.models.PlusCode;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result
        implements Parcelable
{

    @SerializedName("geometry")
    @Expose
    private Geometry geometry;
    @SerializedName("plus_code")
    @Expose
    private PlusCode plusCode;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("vicinity")
    @Expose
    private String vicinity;
    @SerializedName("place_id")
    @Expose
    private String place_id;
    @SerializedName("address_components")
    @Expose
    private List<AddressComponent> addressComponents ;
    @SerializedName("formatted_address")
    @Expose
    private String formattedAddress;
    @SerializedName("formatted_phone_number")
    @Expose
    private String formattedPhoneNumber;

    public Result()
    {
    }

    protected Result(Parcel in) {
        geometry = in.readParcelable(Geometry.class.getClassLoader());
        name = in.readString();
        vicinity = in.readString();
        place_id = in.readString();
        addressComponents = in.createTypedArrayList(AddressComponent.CREATOR);
        formattedAddress = in.readString();
        formattedPhoneNumber = in.readString();
    }

    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

    public Geometry getGeometry()
    {
        return geometry;
    }

    public void setGeometry(Geometry geometry)
    {
        this.geometry = geometry;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getVicinity()
    {
        return vicinity;
    }

    public void setVicinity(String vicinity)
    {
        this.vicinity = vicinity;
    }

    public PlusCode getPlusCode() { return plusCode; }

    public void setPlusCode(PlusCode plusCode) { this.plusCode = plusCode; }

    public String getPlace_id() { return place_id; }

    public void setPlace_id(String place_id) { this.place_id = place_id; }

    public List<AddressComponent> getAddressComponents() { return addressComponents; }

    public void setAddressComponents(List<AddressComponent> addressComponents) { this.addressComponents = addressComponents; }

    public String getFormattedAddress() { return formattedAddress; }

    public void setFormattedAddress(String formattedAddress) { this.formattedAddress = formattedAddress; }

    public String getFormattedPhoneNumber() { return formattedPhoneNumber; }

    public void setFormattedPhoneNumber(String formattedPhoneNumber) { this.formattedPhoneNumber = formattedPhoneNumber; }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(geometry, flags);
        dest.writeString(name);
        dest.writeString(vicinity);
        dest.writeString(place_id);
        dest.writeTypedList(addressComponents);
        dest.writeString(formattedAddress);
        dest.writeString(formattedPhoneNumber);
    }
}