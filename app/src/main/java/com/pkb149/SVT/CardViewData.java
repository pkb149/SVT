package com.pkb149.SVT;

/**
 * Created by CoderGuru on 19-08-2017.
 */

import android.os.Parcel;
import android.os.Parcelable;


public class CardViewData implements Parcelable {

    private int id=0;
    private String availableFrom=null;
    private String onwerId=null;
    private String numberPlate=null;

    CardViewData(Parcel in) {
        this.availableFrom=in.readString();
        this.onwerId=in.readString();
        this.numberPlate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(availableFrom);
        dest.writeString(onwerId);
        dest.writeString(numberPlate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<CardViewData> CREATOR = new Parcelable.Creator<CardViewData>() {

        @Override
        public CardViewData createFromParcel(Parcel source) {
            return new CardViewData(source);
        }

        @Override
        public CardViewData[] newArray(int size) {
            return new CardViewData[size];
        }
    };

    public String getAvailableFrom() {
        return availableFrom;
    }

    public void setAvailableFrom(String availableFrom) {
        this.availableFrom = availableFrom;
    }

    public String getOnwerId() {
        return onwerId;
    }

    public void setOnwerId(String onwerId) {
        this.onwerId = onwerId;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }
}
