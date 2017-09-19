package com.pkb149.SVT.utility;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by CoderGuru on 17-09-2017.
 */

public class BookingDetailsCardViewData implements Parcelable {
    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getMerchantMobile() {
        return merchantMobile;
    }

    public void setMerchantMobile(String merchantMobile) {
        this.merchantMobile = merchantMobile;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    private int id=0;
    private String bookingId=null;
    private String merchant=null;
    private String merchantMobile=null;
    private String fromLocation=null;
    private String toLocation=null;
    private String fromTime=null;
    private String toTime=null;


    public BookingDetailsCardViewData(Parcel in) {
        this.bookingId=in.readString();
        this.merchant=in.readString();
        this.merchantMobile=in.readString();
        this.fromLocation=in.readString();
        this.toLocation = in.readString();
        this.fromTime=in.readString();
        this.toTime=in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bookingId);
        dest.writeString(merchant);
        dest.writeString(merchantMobile);
        dest.writeString(fromLocation);
        dest.writeString(toLocation);
        dest.writeString(fromTime);
        dest.writeString(toTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<BookingDetailsCardViewData> CREATOR = new Parcelable.Creator<BookingDetailsCardViewData>() {

        @Override
        public BookingDetailsCardViewData createFromParcel(Parcel source) {
            return new BookingDetailsCardViewData(source);
        }

        @Override
        public BookingDetailsCardViewData[] newArray(int size) {
            return new BookingDetailsCardViewData[size];
        }
    };


}

