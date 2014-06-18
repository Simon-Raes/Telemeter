package be.simonraes.telemeter.model;

import java.util.Date;

/**
 * Created by Simon Raes on 15/06/2014.
 */
public class DailyUsage {

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
}
