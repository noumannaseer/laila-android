package com.fantechlabs.lailaa.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//*******************************************
public class Profile
        implements Parcelable
//*******************************************
{

    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("address_pobox")
    @Expose
    private String addressPobox;
    @SerializedName("pref_lang")
    @Expose
    private String prefLang;
    @SerializedName("twitter")
    @Expose
    private String twitter;
    @SerializedName("user_type")
    @Expose
    private Integer userType;
    @SerializedName("fav_music")
    @Expose
    private String favMusic;
    @SerializedName("address_country")
    @Expose
    private String addressCountry;
    @SerializedName("address_line2")
    @Expose
    private String addressLine2;
    @SerializedName("address_line3")
    @Expose
    private String addressLine3;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("address_line1")
    @Expose
    private String addressLine1;
    @SerializedName("user_private_code")
    @Expose
    private String userPrivateCode;
    @SerializedName("allergies")
    @Expose
    private String allergies;
    @SerializedName("is_assistant_active")
    @Expose
    private Integer isAssistantActive;
    @SerializedName("date_of_birth")
    @Expose
    private String dateOfBirth;
    @SerializedName("blood_type")
    @Expose
    private String bloodType;
    @SerializedName("is_notifications")
    @Expose
    private Integer isNotifications;
    @SerializedName("firebase_chat_token")
    @Expose
    private Object firebaseChatToken;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("health_card_number")
    @Expose
    private String healthCardNumber;
    @SerializedName("facebook")
    @Expose
    private String facebook;
    @SerializedName("organ_donor")
    @Expose
    private String organDonor;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("medical_Conditions")
    @Expose
    private String medicalConditions;
    @SerializedName("private_insurance")
    @Expose
    private String privateInsurance;
    @SerializedName("address_city")
    @Expose
    private String addressCity;
    @SerializedName("public_code")
    @Expose
    private String publicCode;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("private_insurance_number")
    @Expose
    private String privateInsuranceNumber;
    @SerializedName("is_audio")
    @Expose
    private Integer isAudio;
    @SerializedName("address_province")
    @Expose
    private String addressProvince;
    @SerializedName("ad_visible")
    @Expose
    private int ad_visible;
    @SerializedName("ad_amount")
    @Expose
    private double ad_amount;
    @SerializedName("ad_currency")
    @Expose
    private String ad_currency;
    @SerializedName("height")
    @Expose
    private double height;
    @SerializedName("weight")
    @Expose
    private double weight;
    @SerializedName("height_unit")
    @Expose
    private String height_unit;
    @SerializedName("weight_unit")
    @Expose
    private String weight_unit;

    public Profile(){}

    protected Profile(Parcel in) {
        lastName = in.readString();
        addressPobox = in.readString();
        prefLang = in.readString();
        twitter = in.readString();
        if (in.readByte() == 0) {
            userType = null;
        } else {
            userType = in.readInt();
        }
        favMusic = in.readString();
        addressCountry = in.readString();
        addressLine2 = in.readString();
        addressLine3 = in.readString();
        firstName = in.readString();
        addressLine1 = in.readString();
        userPrivateCode = in.readString();
        allergies = in.readString();
        if (in.readByte() == 0) {
            isAssistantActive = null;
        } else {
            isAssistantActive = in.readInt();
        }
        dateOfBirth = in.readString();
        bloodType = in.readString();
        if (in.readByte() == 0) {
            isNotifications = null;
        } else {
            isNotifications = in.readInt();
        }
        phone = in.readString();
        healthCardNumber = in.readString();
        facebook = in.readString();
        organDonor = in.readString();
        password = in.readString();
        medicalConditions = in.readString();
        privateInsurance = in.readString();
        addressCity = in.readString();
        publicCode = in.readString();
        gender = in.readString();
        email = in.readString();
        privateInsuranceNumber = in.readString();
        if (in.readByte() == 0) {
            isAudio = null;
        } else {
            isAudio = in.readInt();
        }
        addressProvince = in.readString();
        ad_visible = in.readInt();
        ad_amount = in.readDouble();
        ad_currency = in.readString();
        height = in.readDouble();
        weight = in.readDouble();
        height_unit = in.readString();
        weight_unit = in.readString();
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getAddressPobox() {
        return addressPobox;
    }

    public void setAddressPobox(String addressPobox) {
        this.addressPobox = addressPobox;
    }

    public String getPrefLang() {
        return prefLang;
    }

    public void setPrefLang(String prefLang) {
        this.prefLang = prefLang;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getFavMusic() {
        return favMusic;
    }

    public void setFavMusic(String favMusic) {
        this.favMusic = favMusic;
    }

    public String getAddressCountry() {
        return addressCountry;
    }

    public void setAddressCountry(String addressCountry) {
        this.addressCountry = addressCountry;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getUserPrivateCode() {
        return userPrivateCode;
    }

    public void setUserPrivateCode(String userPrivateCode) { this.userPrivateCode = userPrivateCode; }

    public Integer getIsAssistantActive() {
        return isAssistantActive;
    }

    public void setIsAssistantActive(Integer isAssistantActive) { this.isAssistantActive = isAssistantActive; }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public Integer getIsNotifications() {
        return isNotifications;
    }

    public void setIsNotifications(Integer isNotifications) { this.isNotifications = isNotifications; }

    public Object getFirebaseChatToken() {
        return firebaseChatToken;
    }

    public void setFirebaseChatToken(Object firebaseChatToken) { this.firebaseChatToken = firebaseChatToken; }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHealthCardNumber() {
        return healthCardNumber;
    }

    public void setHealthCardNumber(String healthCardNumber) { this.healthCardNumber = healthCardNumber; }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getOrganDonor() {
        return organDonor;
    }

    public void setOrganDonor(String organDonor) {
        this.organDonor = organDonor;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrivateInsurance() {
        return privateInsurance;
    }

    public void setPrivateInsurance(String privateInsurance) { this.privateInsurance = privateInsurance; }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getPublicCode() {
        return publicCode;
    }

    public void setPublicCode(String publicCode) {
        this.publicCode = publicCode;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPrivateInsuranceNumber() {
        return privateInsuranceNumber;
    }

    public void setPrivateInsuranceNumber(String privateInsuranceNumber) { this.privateInsuranceNumber = privateInsuranceNumber; }

    public Integer getIsAudio() {
        return isAudio;
    }

    public void setIsAudio(Integer isAudio) {
        this.isAudio = isAudio;
    }

    public String getAddressProvince() { return addressProvince; }

    public void setAddressProvince(String addressProvince) { this.addressProvince = addressProvince; }

    public String getAllergies() { return allergies; }

    public void setAllergies(String allergies) { this.allergies = allergies; }

    public String getMedicalConditions() { return medicalConditions; }

    public void setMedicalConditions(String medicalConditions) { this.medicalConditions = medicalConditions; }

    public int getAd_visible() { return ad_visible; }

    public void setAd_visible(int ad_visible) { this.ad_visible = ad_visible; }

    public double getAd_amount() { return ad_amount; }

    public void setAd_amount(double ad_amount) { this.ad_amount = ad_amount; }

    public String getAd_currency() { return ad_currency; }

    public void setAd_currency(String ad_currency) { this.ad_currency = ad_currency; }

    public double getHeight() { return height; }

    public void setHeight(double height) { this.height = height; }

    public double getWeight() { return weight; }

    public void setWeight(double weight) { this.weight = weight; }

    public String getHeight_unit() { return height_unit; }

    public void setHeight_unit(String height_unit) { this.height_unit = height_unit; }

    public String getWeight_unit() { return weight_unit; }

    public void setWeight_unit(String weight_unit) { this.weight_unit = weight_unit; }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(lastName);
        dest.writeString(addressPobox);
        dest.writeString(prefLang);
        dest.writeString(twitter);
        if (userType == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(userType);
        }
        dest.writeString(favMusic);
        dest.writeString(addressCountry);
        dest.writeString(addressLine2);
        dest.writeString(addressLine3);
        dest.writeString(firstName);
        dest.writeString(addressLine1);
        dest.writeString(userPrivateCode);
        dest.writeString(allergies);
        if (isAssistantActive == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(isAssistantActive);
        }
        dest.writeString(dateOfBirth);
        dest.writeString(bloodType);
        if (isNotifications == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(isNotifications);
        }
        dest.writeString(phone);
        dest.writeString(healthCardNumber);
        dest.writeString(facebook);
        dest.writeString(organDonor);
        dest.writeString(password);
        dest.writeString(medicalConditions);
        dest.writeString(privateInsurance);
        dest.writeString(addressCity);
        dest.writeString(publicCode);
        dest.writeString(gender);
        dest.writeString(email);
        dest.writeString(privateInsuranceNumber);
        if (isAudio == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(isAudio);
        }
        dest.writeString(addressProvince);
        dest.writeInt(ad_visible);
        dest.writeDouble(ad_amount);
        dest.writeString(ad_currency);
        dest.writeDouble(height);
        dest.writeDouble(weight);
        dest.writeString(height_unit);
        dest.writeString(weight_unit);
    }
}