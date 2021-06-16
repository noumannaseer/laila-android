package com.aditum.bodyreading.repository.storge.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//*******************************************
public class Contact
implements Parcelable
//*******************************************
{

    @SerializedName("address_line2")
    @Expose
    private String addressLine2;
    @SerializedName("area_of_specialty")
    @Expose
    private String areaOfSpecialty;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("address_pobox")
    @Expose
    private String addressPobox;
    @SerializedName("user_private_code")
    @Expose
    private String userPrivateCode;
    @SerializedName("contact_type")
    @Expose
    private String contactType;
    @SerializedName("confirm_email")
    @Expose
    private String confirmEmail;
    @SerializedName("address_line3")
    @Expose
    private String addressLine3;
    @SerializedName("address_city")
    @Expose
    private String addressCity;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("confirm_phone")
    @Expose
    private String confirmPhone;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("address_line1")
    @Expose
    private String addressLine1;
    @SerializedName("address_country")
    @Expose
    private String addressCountry;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("address_province")
    @Expose
    private String addressProvince;
    @SerializedName("is_preferred")
    @Expose
    private String is_preferred;


    public Contact(){}


    protected Contact(Parcel in) {
        addressLine2 = in.readString();
        areaOfSpecialty = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        addressPobox = in.readString();
        userPrivateCode = in.readString();
        contactType = in.readString();
        confirmEmail = in.readString();
        addressLine3 = in.readString();
        addressCity = in.readString();
        email = in.readString();
        confirmPhone = in.readString();
        phone = in.readString();
        addressLine1 = in.readString();
        addressCountry = in.readString();
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        addressProvince = in.readString();
        is_preferred = in.readString();
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAreaOfSpecialty() {
        return areaOfSpecialty;
    }

    public void setAreaOfSpecialty(String areaOfSpecialty) { this.areaOfSpecialty = areaOfSpecialty; }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddressPobox() {
        return addressPobox;
    }

    public void setAddressPobox(String addressPobox) {
        this.addressPobox = addressPobox;
    }

    public String getUserPrivateCode() {
        return userPrivateCode;
    }

    public void setUserPrivateCode(String userPrivateCode) { this.userPrivateCode = userPrivateCode; }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public String getConfirmEmail() {
        return confirmEmail;
    }

    public void setConfirmEmail(String confirmEmail) {
        this.confirmEmail = confirmEmail;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirmPhone() {
        return confirmPhone;
    }

    public void setConfirmPhone(String confirmPhone) {
        this.confirmPhone = confirmPhone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressCountry() {
        return addressCountry;
    }

    public void setAddressCountry(String addressCountry) {
        this.addressCountry = addressCountry;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddressProvince() {
        return addressProvince;
    }

    public void setAddressProvince(String addressProvince) { this.addressProvince = addressProvince; }

    public String getIs_preferred() { return is_preferred; }

    public void setIs_preferred(String is_preferred) { this.is_preferred = is_preferred; }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(addressLine2);
        dest.writeString(areaOfSpecialty);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(addressPobox);
        dest.writeString(userPrivateCode);
        dest.writeString(contactType);
        dest.writeString(confirmEmail);
        dest.writeString(addressLine3);
        dest.writeString(addressCity);
        dest.writeString(email);
        dest.writeString(confirmPhone);
        dest.writeString(phone);
        dest.writeString(addressLine1);
        dest.writeString(addressCountry);
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(addressProvince);
        dest.writeString(is_preferred);
    }
}
