package com.fantechlabs.lailaa.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Medication
    implements Parcelable
{

    @SerializedName("medication_name")
    @Expose
    private String medicationName;
    @SerializedName("strength")
    @Expose
    private String strength;
    @SerializedName("user_private_code")
    @Expose
    private String userPrivateCode;
    @SerializedName("strength_uom")
    @Expose
    private String strengthUom;
    @SerializedName("din_rx_number")
    @Expose
    private String dinRxNumber;
    @SerializedName("medecine_form")
    @Expose
    private String medecineForm;
    @SerializedName("comments")
    @Expose
    private String comments;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("number_of_pills")
    @Expose
    private Integer numberOfPills;
    @SerializedName("num_refills")
    @Expose
    private Integer numRefills;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("refill_date")
    @Expose
    private String refillDate;
    @SerializedName("frequency1")
    @Expose
    private String frequency1;
    @SerializedName("frequency2")
    @Expose
    private String frequency2;
    @SerializedName("when_needed")
    @Expose
    private String whenNeeded;
    @SerializedName("pharmacy")
    @Expose
    private Integer pharmacy;
    @SerializedName("medscape_id")
    @Expose
    private String medscapeId;
    @SerializedName("add_refill_date")
    @Expose
    private String addRefillDate;
    @SerializedName("dosage")
    @Expose
    private String dosage;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("prescribed")
    @Expose
    private int prescribed;
    @SerializedName("delivery_type")
    @Expose
    private int delivery_type;

    private List<String> intakeTime;


    protected Medication(Parcel in) {
        medicationName = in.readString();
        strength = in.readString();
        userPrivateCode = in.readString();
        strengthUom = in.readString();
        dinRxNumber = in.readString();
        medecineForm = in.readString();
        comments = in.readString();
        startDate = in.readString();
        if (in.readByte() == 0) {
            numberOfPills = null;
        } else {
            numberOfPills = in.readInt();
        }
        if (in.readByte() == 0) {
            numRefills = null;
        } else {
            numRefills = in.readInt();
        }
        amount = in.readString();
        refillDate = in.readString();
        frequency1 = in.readString();
        frequency2 = in.readString();
        whenNeeded = in.readString();
        if (in.readByte() == 0) {
            pharmacy = null;
        } else {
            pharmacy = in.readInt();
        }
        medscapeId = in.readString();
        addRefillDate = in.readString();
        dosage = in.readString();
        id = in.readInt();
        error = in.readString();
        prescribed = in.readInt();
        delivery_type = in.readInt();
        intakeTime = in.createStringArrayList();
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

    public String getError()
    {
        return error;
    }

    public void setError(String error)
    {
        this.error = error;
    }

    public String getMedicationName()
    {
        return medicationName;
    }

    public void setMedicationName(String medicationName)
    {
        this.medicationName = medicationName;
    }

    public String getStrength()
    {
        return strength;
    }

    public void setStrength(String strength)
    {
        this.strength = strength;
    }

    public String getUserPrivateCode()
    {
        return userPrivateCode;
    }

    public void setUserPrivateCode(String userPrivateCode) { this.userPrivateCode = userPrivateCode; }

    public int getDelivery_type() { return delivery_type; }

    public void setDelivery_type(int delivery_type) { this.delivery_type = delivery_type; }

    public String getStrengthUom()
    {
        return strengthUom;
    }

    public void setStrengthUom(String strengthUom)
    {
        this.strengthUom = strengthUom;
    }

    public String getDinRxNumber()
    {
        return dinRxNumber;
    }

    public void setDinRxNumber(String dinRxNumber)
    {
        this.dinRxNumber = dinRxNumber;
    }

    public String getMedecineForm()
    {
        return medecineForm;
    }

    public void setMedecineForm(String medecineForm)
    {
        this.medecineForm = medecineForm;
    }

    public String getComments()
    {
        return comments;
    }

    public void setComments(String comments)
    {
        this.comments = comments;
    }

    public String getStartDate()
    {
        return startDate;
    }

    public void setStartDate(String startDate)
    {
        this.startDate = startDate;
    }

    public Integer getNumberOfPills()
    {
        return numberOfPills;
    }

    public void setNumberOfPills(Integer numberOfPills)
    {
        this.numberOfPills = numberOfPills;
    }

    public Integer getNumRefills()
    {
        return numRefills;
    }

    public void setNumRefills(Integer numRefills)
    {
        this.numRefills = numRefills;
    }

    public String getAmount()
    {
        return amount;
    }

    public void setAmount(String amount)
    {
        this.amount = amount;
    }

    public String getRefillDate()
    {
        return refillDate;
    }

    public void setRefillDate(String refillDate)
    {
        this.refillDate = refillDate;
    }

    public String getFrequency1()
    {
        return frequency1;
    }

    public void setFrequency1(String frequency1)
    {
        this.frequency1 = frequency1;
    }

    public String getFrequency2()
    {
        return frequency2;
    }

    public void setFrequency2(String frequency2)
    {
        this.frequency2 = frequency2;
    }

    public String getWhenNeeded()
    {
        return whenNeeded;
    }

    public void setWhenNeeded(String whenNeeded)
    {
        this.whenNeeded = whenNeeded;
    }

    public Integer getPharmacy()
    {
        return pharmacy;
    }

    public void setPharmacy(Integer pharmacy)
    {
        this.pharmacy = pharmacy;
    }

    public String getMedscapeId()
    {
        return medscapeId;
    }

    public void setMedscapeId(String medscapeId)
    {
        this.medscapeId = medscapeId;
    }

    public String getAddRefillDate()
    {
        return addRefillDate;
    }

    public void setAddRefillDate(String addRefillDate)
    {
        this.addRefillDate = addRefillDate;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getDosage()
    {
        return dosage;
    }

    public void setDosage(String dosage)
    {
        this.dosage = dosage;
    }

    public int getPrescribed() { return prescribed; }

    public void setPrescribed(int prescribed) { this.prescribed = prescribed; }

    public List<String> getIntakeTime() { return intakeTime; }

    public void setIntakeTime(List<String> intakeTime) { this.intakeTime = intakeTime; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(medicationName);
        parcel.writeString(strength);
        parcel.writeString(userPrivateCode);
        parcel.writeString(strengthUom);
        parcel.writeString(dinRxNumber);
        parcel.writeString(medecineForm);
        parcel.writeString(comments);
        parcel.writeString(startDate);
        if (numberOfPills == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(numberOfPills);
        }
        if (numRefills == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(numRefills);
        }
        parcel.writeString(amount);
        parcel.writeString(refillDate);
        parcel.writeString(frequency1);
        parcel.writeString(frequency2);
        parcel.writeString(whenNeeded);
        if (pharmacy == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(pharmacy);
        }
        parcel.writeString(medscapeId);
        parcel.writeString(addRefillDate);
        parcel.writeString(dosage);
        parcel.writeInt(id);
        parcel.writeString(error);
        parcel.writeInt(prescribed);
        parcel.writeInt(delivery_type);
        parcel.writeStringList(intakeTime);
    }
}