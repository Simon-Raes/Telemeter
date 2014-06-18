package be.simonraes.telemeter.model;

import java.util.Date;

/**
 * Created by Simon Raes on 13/06/2014.
 */
public class Period {
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
}
