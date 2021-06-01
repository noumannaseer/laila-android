package com.fantechlabs.lailaa.models.updates.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
//******************************************************
public class Medication implements Parcelable
//******************************************************
{
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("medication_name")
    @Expose
    private String medicationName;
    @SerializedName("din_rx_number")
    @Expose
    private String dinRxNumber;
    @SerializedName("medecine_form")
    @Expose
    private String medecineForm;
    @SerializedName("strength")
    @Expose
    private Integer strength;
    @SerializedName("strength_uom")
    @Expose
    private String strengthUom;
    @SerializedName("dispensed_date")
    @Expose
    private long dispensedDate;
    @SerializedName("dispensed_amount")
    @Expose
    private String dispensedAmount;
    @SerializedName("dosage")
    @Expose
    private Integer dosage;
    @SerializedName("frequency")
    @Expose
    private String frequency;
    @SerializedName("num_refills")
    @Expose
    private Integer numRefills;
    @SerializedName("prescribed")
    @Expose
    private Integer prescribed;
    @SerializedName("pharmacy_id")
    @Expose
    private Integer pharmacyId;
    @SerializedName("refill_date")
    @Expose
    private long refillDate;
    @SerializedName("created_DateTime")
    @Expose
    private long createdDateTime;
    private List<String> intakeTimeList;


    protected Medication(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        medicationName = in.readString();
        dinRxNumber = in.readString();
        medecineForm = in.readString();
        if (in.readByte() == 0) {
            strength = null;
        } else {
            strength = in.readInt();
        }
        strengthUom = in.readString();
        dispensedDate = in.readLong();
        dispensedAmount = in.readString();
        if (in.readByte() == 0) {
            dosage = null;
        } else {
            dosage = in.readInt();
        }
        frequency = in.readString();
        if (in.readByte() == 0) {
            numRefills = null;
        } else {
            numRefills = in.readInt();
        }
        if (in.readByte() == 0) {
            prescribed = null;
        } else {
            prescribed = in.readInt();
        }
        if (in.readByte() == 0) {
            pharmacyId = null;
        } else {
            pharmacyId = in.readInt();
        }
        refillDate = in.readLong();
        createdDateTime = in.readLong();
        intakeTimeList = in.createStringArrayList();
    }

    public static final Creator<Medication> CREATOR = new Creator<Medication>() {
        @Override
        public Medication createFromParcel(Parcel in) {
            return new Medication(in);
        }

        @Override
        public Medication[] newArray(int size) {
            return new Medication[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(id);
        }
        parcel.writeString(medicationName);
        parcel.writeString(dinRxNumber);
        parcel.writeString(medecineForm);
        if (strength == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(strength);
        }
        parcel.writeString(strengthUom);
        parcel.writeLong(dispensedDate);
        parcel.writeString(dispensedAmount);
        if (dosage == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(dosage);
        }
        parcel.writeString(frequency);
        if (numRefills == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(numRefills);
        }
        if (prescribed == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(prescribed);
        }
        if (pharmacyId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(pharmacyId);
        }
        parcel.writeLong(refillDate);
        parcel.writeLong(createdDateTime);
        parcel.writeStringList(intakeTimeList);
    }
}
