package com.aditum.bodyreading.repository.storge.responsemodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//********************************************
public class PaymentResponse
    implements Parcelable
//********************************************
{

    @SerializedName("clientSecret")
    @Expose
    private String clientSecret;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("paid")
    @Expose
    private int paid;

    protected PaymentResponse(Parcel in) {
        clientSecret = in.readString();
        error = in.readString();
        result = in.readString();
        paid = in.readInt();
    }

    public static final Creator<PaymentResponse> CREATOR = new Creator<PaymentResponse>() {
        @Override
        public PaymentResponse createFromParcel(Parcel in) {
            return new PaymentResponse(in);
        }

        @Override
        public PaymentResponse[] newArray(int size) {
            return new PaymentResponse[size];
        }
    };

    public String getClientSecret() { return clientSecret; }

    public void setClientSecret(String clientSecret) { this.clientSecret = clientSecret; }

    public String getError() { return error; }

    public void setError(String error) { this.error = error; }

    public String getResult() { return result; }

    public void setResult(String result) { this.result = result; }

    public int getPaid() { return paid; }

    public void setPaid(int paid) { this.paid = paid; }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(clientSecret);
        dest.writeString(error);
        dest.writeString(result);
        dest.writeInt(paid);
    }
}
