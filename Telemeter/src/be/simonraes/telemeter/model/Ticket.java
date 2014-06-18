package be.simonraes.telemeter.model;

import java.util.Date;

/**
 * Created by Simon Raes on 13/06/2014.
 */
public class Ticket {
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
}
