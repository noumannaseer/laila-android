package com.aditum.models.updates.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActiveIngredient implements Parcelable {
    @SerializedName("dosage_unit")
    @Expose
    private Integer dosageUnit;
    @SerializedName("dosage_value")
    @Expose
    private String dosageValue;
    @SerializedName("drug_code")
    @Expose
    private Integer drugCode;
    @SerializedName("ingredient_name")
    @Expose
    private String ingredientName;
    @SerializedName("strength")
    @Expose
    private String strength;
    @SerializedName("strength_unit")
    @Expose
    private Integer strengthUnit;

    protected ActiveIngredient(Parcel in) {
        if (in.readByte() == 0) {
            dosageUnit = null;
        } else {
            dosageUnit = in.readInt();
        }
        dosageValue = in.readString();
        if (in.readByte() == 0) {
            drugCode = null;
        } else {
            drugCode = in.readInt();
        }
        ingredientName = in.readString();
        strength = in.readString();
        if (in.readByte() == 0) {
            strengthUnit = null;
        } else {
            strengthUnit = in.readInt();
        }
    }

    public static final Creator<ActiveIngredient> CREATOR = new Creator<ActiveIngredient>() {
        @Override
        public ActiveIngredient createFromParcel(Parcel in) {
            return new ActiveIngredient(in);
        }

        @Override
        public ActiveIngredient[] newArray(int size) {
            return new ActiveIngredient[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (dosageUnit == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(dosageUnit);
        }
        dest.writeString(dosageValue);
        if (drugCode == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(drugCode);
        }
        dest.writeString(ingredientName);
        dest.writeString(strength);
        if (strengthUnit == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(strengthUnit);
        }
    }
}
