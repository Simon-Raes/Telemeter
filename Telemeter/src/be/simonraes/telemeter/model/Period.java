package be.simonraes.telemeter.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Simon Raes on 13/06/2014.
 */
public class Period implements Parcelable{
    private Date from, till;
    private String currentDay;

    public Period() {
        this.from = null;
        this.till = null;
        this.currentDay = null;
    }

    public Period(Date from, Date till, String currentDay) {
        this.from = from;
        this.till = till;
        this.currentDay = currentDay;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTill() {
        return till;
    }

    public void setTill(Date till) {
        this.till = till;
    }

    public String getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(String currentDay) {
        this.currentDay = currentDay;
    }

    //parcelable code

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //convert dates to long so they are parcelable
        dest.writeLong(from.getTime());
        dest.writeLong(till.getTime());
        dest.writeString(currentDay);
    }

    public Period(Parcel pc) {
        from = new Date(pc.readLong());
        till = new Date(pc.readLong());
        currentDay = pc.readString();
    }

    public static final Creator<Period> CREATOR = new
            Creator<Period>() {
                public Period createFromParcel(Parcel pc) {
                    return new Period(pc);
                }

                public Period[] newArray(int size) {
                    return new Period[size];
                }
            };
}
