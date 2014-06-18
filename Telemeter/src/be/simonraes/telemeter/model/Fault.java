package be.simonraes.telemeter.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Simon Raes on 13/06/2014.
 */
public class Fault implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(faultCode);
        dest.writeString(faultString);
        dest.writeParcelable(detail, 0);
    }

    public Fault(Parcel pc) {
        faultCode = pc.readString();
        faultString = pc.readString();
        detail = pc.readParcelable(Detail.class.getClassLoader());
    }

    public static final Creator<Fault> CREATOR = new
            Creator<Fault>() {
                public Fault createFromParcel(Parcel pc) {
                    return new Fault(pc);
                }

                public Fault[] newArray(int size) {
                    return new Fault[size];
                }
            };
}
