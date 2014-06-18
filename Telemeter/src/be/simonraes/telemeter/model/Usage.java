package be.simonraes.telemeter.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Simon Raes on 13/06/2014.
 */
public class Usage implements Parcelable {
    private double totalUsage, minUsageRemaining, maxUsageRemaining;
    private String unit;
    private Date lastUpdate;

    public Usage() {
        this.totalUsage = 0;
        this.minUsageRemaining = 0;
        this.maxUsageRemaining = 0;
        this.unit = null;
        this.lastUpdate = null;
    }

    public Usage(double totalUsage, double minUsageRemaining, double maxUsageRemaining, String unit, Date lastUpdate) {
        this.totalUsage = totalUsage;
        this.minUsageRemaining = minUsageRemaining;
        this.maxUsageRemaining = maxUsageRemaining;
        this.unit = unit;
        this.lastUpdate = lastUpdate;
    }

    public double getTotalUsage() {
        return totalUsage;
    }

    public void setTotalUsage(double totalUsage) {
        this.totalUsage = totalUsage;
    }

    public double getMinUsageRemaining() {
        return minUsageRemaining;
    }

    public void setMinUsageRemaining(double minUsageRemaining) {
        this.minUsageRemaining = minUsageRemaining;
    }

    public double getMaxUsageRemaining() {
        return maxUsageRemaining;
    }

    public void setMaxUsageRemaining(double maxUsageRemaining) {
        this.maxUsageRemaining = maxUsageRemaining;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    //parcelable code



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(totalUsage);
        dest.writeDouble(minUsageRemaining);
        dest.writeDouble(maxUsageRemaining);
        dest.writeString(unit);
        //convert dates to long so they are parcelable
        dest.writeLong(lastUpdate.getTime());
    }

    public Usage(Parcel pc) {
        totalUsage = pc.readDouble();
        minUsageRemaining = pc.readDouble();
        maxUsageRemaining = pc.readDouble();
        unit = pc.readString();
        lastUpdate = new Date(pc.readLong());
    }

    public static final Creator<Usage> CREATOR = new
            Creator<Usage>() {
                public Usage createFromParcel(Parcel pc) {
                    return new Usage(pc);
                }

                public Usage[] newArray(int size) {
                    return new Usage[size];
                }
            };
}
