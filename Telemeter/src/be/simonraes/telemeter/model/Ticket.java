package be.simonraes.telemeter.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Simon Raes on 13/06/2014.
 */
public class Ticket implements Parcelable{
    private Date timestamp, expiryTimestamp;

    public Ticket() {
        this.timestamp = null;
        this.expiryTimestamp = null;
    }

    public Ticket(Date timestamp, Date expiryTimestamp) {
        this.timestamp = timestamp;
        this.expiryTimestamp = expiryTimestamp;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getExpiryTimestamp() {
        return expiryTimestamp;
    }

    public void setExpiryTimestamp(Date expiryTimestamp) {
        this.expiryTimestamp = expiryTimestamp;
    }

    //parcelable code

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //convert dates to long so they are parcelable
        dest.writeLong(timestamp.getTime());
        dest.writeLong(expiryTimestamp.getTime());
    }

    public Ticket(Parcel pc) {
        timestamp = new Date(pc.readLong());
        expiryTimestamp = new Date(pc.readLong());
    }

    public static final Creator<Ticket> CREATOR = new
            Creator<Ticket>() {
                public Ticket createFromParcel(Parcel pc) {
                    return new Ticket(pc);
                }

                public Ticket[] newArray(int size) {
                    return new Ticket[size];
                }
            };
}
