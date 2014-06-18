package be.simonraes.telemeter.model;

/**
 * Created by Simon Raes on 13/06/2014.
 */
public class Fault {

    private String faultCode, faultString;
    private Detail detail;

    public Fault() {
        this.faultCode = null;
        this.faultString = null;
        this.detail = null;
    }

    public Fault(String faultCode, String faultString, Detail detail) {
        this.faultCode = faultCode;
        this.faultString = faultString;
        this.detail = detail;
    }

    public String getFaultCode() {
        return faultCode;
    }

    public void setFaultCode(String faultCode) {
        this.faultCode = faultCode;
    }

    public String getFaultString() {
        return faultString;
    }

    public void setFaultString(String faultString) {
        this.faultString = faultString;
    }

    public Detail getDetail() {
        if (detail != null) {
            return detail;
        } else {
            return new Detail();
        }

    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }
}
