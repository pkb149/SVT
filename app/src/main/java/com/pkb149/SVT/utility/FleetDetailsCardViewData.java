package com.pkb149.SVT.utility;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by CoderGuru on 17-09-2017.
 */


public class FleetDetailsCardViewData implements Parcelable {

    private int id=0;
    private String driverName=null;
    private String driverMobileNumber=null;
    private String numberPlate=null;

    public FleetDetailsCardViewData(Parcel in) {
        this.driverName=in.readString();
        this.driverMobileNumber=in.readString();
        this.numberPlate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(driverName);
        dest.writeString(driverMobileNumber);
        dest.writeString(numberPlate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<FleetDetailsCardViewData> CREATOR = new Parcelable.Creator<FleetDetailsCardViewData>() {

        @Override
        public FleetDetailsCardViewData createFromParcel(Parcel source) {
            return new FleetDetailsCardViewData(source);
        }

        @Override
        public FleetDetailsCardViewData[] newArray(int size) {
            return new FleetDetailsCardViewData[size];
        }
    };

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverMobileNumber() {
        return driverMobileNumber;
    }

    public void setDriverMobileNumber(String driverMobileNumber) {
        this.driverMobileNumber = driverMobileNumber;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }
}

