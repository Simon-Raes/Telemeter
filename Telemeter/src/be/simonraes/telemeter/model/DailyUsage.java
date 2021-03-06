package be.simonraes.telemeter.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Simon Raes on 15/06/2014.
 */
public class DailyUsage implements Parcelable{

    private Date day;
    private String usage;

    public DailyUsage(Date day, String usage) {
        this.day = day;
        this.usage = usage;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(day.getTime());
        dest.writeString(usage);

    }

    public DailyUsage(Parcel pc) {
        day = new Date(pc.readLong());
        usage = pc.readString();
    }

    public static final Creator<DailyUsage> CREATOR = new
            Creator<DailyUsage>() {
                public DailyUsage createFromParcel(Parcel pc) {
                    return new DailyUsage(pc);
                }

                public DailyUsage[] newArray(int size) {
                    return new DailyUsage[size];
                }
            };
}
