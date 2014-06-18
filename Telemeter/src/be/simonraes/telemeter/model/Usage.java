package be.simonraes.telemeter.model;

import java.util.Date;

/**
 * Created by Simon Raes on 13/06/2014.
 */
public class Usage {
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
}
