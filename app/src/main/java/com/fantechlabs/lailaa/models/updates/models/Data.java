package com.fantechlabs.lailaa.models.updates.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fantechlabs.lailaa.models.SpecialImages;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
//*****************************************************************
public class Data implements Parcelable
//*****************************************************************
{
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("profile")
    @Expose
    private Profile profile;
    @SerializedName("updated_profile")
    @Expose
    private Profile updatedProfile;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("medication")
    @Expose
    private Medication medication;
    @SerializedName("medications")
    @Expose
    private ArrayList<Medication> medicationList = null;
    @SerializedName("document")
    @Expose
    private Document document;
    @SerializedName("documents")
    @Expose
    private List<Document> documentsList = null;
    @SerializedName("pharmacy")
    @Expose
    private Pharmacy pharmacy;
    @SerializedName("pharmacies")
    @Expose
    private List<Pharmacy> pharmacyList = null;
    private List<String> stringPharmacyList;
    @SerializedName("body_vital")
    @Expose
    private BodyVital bodyVital;
    @SerializedName("averages")
    @Expose
    private HealthAverages averages;
    @SerializedName("readings")
    @Expose
    private ArrayList<BodyVital> bodyVitalReadings = null;
    @SerializedName("search_medications")
    @Expose
    private List<SearchMedication> searchMedications = null;
    @SerializedName("avatar")
    @Expose
    private Avatar avatar;
    @SerializedName("folowup")
    @Expose
    private Followup folowup;
    @SerializedName("medication_info")
    @Expose
    private List<MedicationInformation> medicationInfo = null;
    @SerializedName("active_ingredient")
    @Expose
    private List<ActiveIngredient> activeIngredient = null;
    @SerializedName("contact")
    @Expose
    private Contact contact;
    @SerializedName("contact_list")
    @Expose
    private List<Contact> contactList = null;
    @SerializedName("interaction_msgs")
    @Expose
    private List<InteractionMsg> interactionMsgs = null;
    @SerializedName("response_events")
    @Expose
    private List<ResponseEvent> responseEvents = null;
    @SerializedName("Events_list")
    @Expose
    private List<ResponseEvent> eventsList = null;
    @SerializedName("ids")
    @Expose
    private List<Integer> ids = null;
    @SerializedName("preferred_ids")
    @Expose
    private List<String> preferredIds = null;
    @SerializedName("images")
    @Expose
    private List<SpecialImages> specialImages = null;

    @SerializedName("clientSecret")
    @Expose
    private String clientSecret;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("paid")
    @Expose
    private Integer paid;

    protected Data(Parcel in) {
        user = in.readParcelable(User.class.getClassLoader());
        profile = in.readParcelable(Profile.class.getClassLoader());
        updatedProfile = in.readParcelable(Profile.class.getClassLoader());
        message = in.readString();
        medication = in.readParcelable(Medication.class.getClassLoader());
        medicationList = in.createTypedArrayList(Medication.CREATOR);
        document = in.readParcelable(Document.class.getClassLoader());
        documentsList = in.createTypedArrayList(Document.CREATOR);
        pharmacy = in.readParcelable(Pharmacy.class.getClassLoader());
        pharmacyList = in.createTypedArrayList(Pharmacy.CREATOR);
        stringPharmacyList = in.createStringArrayList();
        bodyVital = in.readParcelable(BodyVital.class.getClassLoader());
        averages = in.readParcelable(HealthAverages.class.getClassLoader());
        bodyVitalReadings = in.createTypedArrayList(BodyVital.CREATOR);
        searchMedications = in.createTypedArrayList(SearchMedication.CREATOR);
        avatar = in.readParcelable(Avatar.class.getClassLoader());
        medicationInfo = in.createTypedArrayList(MedicationInformation.CREATOR);
        activeIngredient = in.createTypedArrayList(ActiveIngredient.CREATOR);
        contact = in.readParcelable(Contact.class.getClassLoader());
        contactList = in.createTypedArrayList(Contact.CREATOR);
        interactionMsgs = in.createTypedArrayList(InteractionMsg.CREATOR);
        responseEvents = in.createTypedArrayList(ResponseEvent.CREATOR);
        eventsList = in.createTypedArrayList(ResponseEvent.CREATOR);
        preferredIds = in.createStringArrayList();
        clientSecret = in.readString();
        error = in.readString();
        if (in.readByte() == 0) {
            paid = null;
        } else {
            paid = in.readInt();
        }
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(user, flags);
        dest.writeParcelable(profile, flags);
        dest.writeParcelable(updatedProfile, flags);
        dest.writeString(message);
        dest.writeParcelable(medication, flags);
        dest.writeTypedList(medicationList);
        dest.writeParcelable(document, flags);
        dest.writeTypedList(documentsList);
        dest.writeParcelable(pharmacy, flags);
        dest.writeTypedList(pharmacyList);
        dest.writeStringList(stringPharmacyList);
        dest.writeParcelable(bodyVital, flags);
        dest.writeParcelable(averages, flags);
        dest.writeTypedList(bodyVitalReadings);
        dest.writeTypedList(searchMedications);
        dest.writeParcelable(avatar, flags);
        dest.writeTypedList(medicationInfo);
        dest.writeTypedList(activeIngredient);
        dest.writeParcelable(contact, flags);
        dest.writeTypedList(contactList);
        dest.writeTypedList(interactionMsgs);
        dest.writeTypedList(responseEvents);
        dest.writeTypedList(eventsList);
        dest.writeStringList(preferredIds);
        dest.writeString(clientSecret);
        dest.writeString(error);
        if (paid == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(paid);
        }
    }
}
