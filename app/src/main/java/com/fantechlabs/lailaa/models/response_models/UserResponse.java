package com.fantechlabs.lailaa.models.response_models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fantechlabs.lailaa.models.Contact;
import com.fantechlabs.lailaa.models.Medication;
import com.fantechlabs.lailaa.models.Profile;
import com.fantechlabs.lailaa.models.Events;
import com.fantechlabs.lailaa.models.ProfileImages;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class UserResponse
    implements Parcelable
{
    @SerializedName("profile")
    @Expose
    private Profile profile;
    @SerializedName("medication")
    @Expose
    private ArrayList<Medication> medication;
    @SerializedName("contacts")
    @Expose
    private ArrayList<Contact> contacts;
    @SerializedName("contact_type")
    @Expose
    private String contactType;
    @SerializedName("mandatory_fields")
    @Expose
    private String mandatoryFields;
    @SerializedName("images")
    @Expose
    private List<ProfileImages> images;
    @SerializedName("events")
    @Expose
    private ArrayList<Events> events;
    @SerializedName("error")
    @Expose
    private String error;

    protected UserResponse(Parcel in) {
        profile = in.readParcelable(Profile.class.getClassLoader());
        medication = in.createTypedArrayList(Medication.CREATOR);
        contactType = in.readString();
        mandatoryFields = in.readString();
        events = in.createTypedArrayList(Events.CREATOR);
        error = in.readString();
    }

    public static final Creator<UserResponse> CREATOR = new Creator<UserResponse>() {
        @Override
        public UserResponse createFromParcel(Parcel in) {
            return new UserResponse(in);
        }

        @Override
        public UserResponse[] newArray(int size) {
            return new UserResponse[size];
        }
    };

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public ArrayList<Medication> getMedication() { return medication; }

    public void setMedication(ArrayList<Medication> medication) { this.medication = medication; }

    public ArrayList<Contact> getContacts() { return contacts; }

    public void setContacts(ArrayList<Contact> contacts) { this.contacts = contacts; }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public String getMandatoryFields() {
        return mandatoryFields;
    }

    public void setMandatoryFields(String mandatoryFields) { this.mandatoryFields = mandatoryFields; }

    public List<ProfileImages> getImages() { return images; }

    public void setImages(List<ProfileImages> images) { this.images = images; }

    public ArrayList<Events> getEvents() { return events; }

    public void setEvents(ArrayList<Events> events) { this.events = events; }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(profile, flags);
        dest.writeTypedList(medication);
        dest.writeString(contactType);
        dest.writeString(mandatoryFields);
        dest.writeTypedList(events);
        dest.writeString(error);
    }
}