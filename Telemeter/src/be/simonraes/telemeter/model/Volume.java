package be.simonraes.telemeter.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Simon Raes on 15/06/2014.
 */
public class Volume implements Parcelable {

    private String unit, limit, totalUsage;
    private ArrayList<DailyUsage> dailyUsageList;

    public Volume() {
        this.unit = null;
        this.limit = null;
        this.totalUsage = null;
        dailyUsageList = new ArrayList<DailyUsage>();
    }

    public Volume(String unit, String limit, String totalUsage, ArrayList<DailyUsage> dailyUsageList) {
        this.unit = unit;
        this.limit = limit;
        this.totalUsage = totalUsage;
        this.dailyUsageList = dailyUsageList;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getTotalUsage() {
        return totalUsage;
    }

    public void setTotalUsage(String totalUsage) {
        this.totalUsage = totalUsage;
    }

    public ArrayList<DailyUsage> getDailyUsageList() {
        return dailyUsageList;
    }

    public void setDailyUsageList(ArrayList<DailyUsage> dailyUsageList) {
        this.dailyUsageList = dailyUsageList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(unit);
        dest.writeString(limit);
        dest.writeString(totalUsage);
        dest.writeTypedList(dailyUsageList);
    }

    public Volume(Parcel pc) {
        unit = pc.readString();
        limit = pc.readString();
        totalUsage = pc.readString();
        dailyUsageList = pc.readArrayList(DailyUsage.class.getClassLoader());
    }

    public static final Creator<Volume> CREATOR = new
            Creator<Volume>() {
                public Volume createFromParcel(Parcel pc) {
                    return new Volume(pc);
                }

                public Volume[] newArray(int size) {
                    return new Volume[size];
                }
            };
}
