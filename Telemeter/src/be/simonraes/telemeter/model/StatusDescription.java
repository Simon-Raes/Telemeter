package be.simonraes.telemeter.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Simon Raes on 13/06/2014.
 */
public class StatusDescription implements Parcelable {
    private String nl, fr;

    public StatusDescription() {

    }

    public StatusDescription(String nl, String fr) {
        this.nl = nl;
        this.fr = fr;
    }

    public String getNl() {
        return nl;
    }

    public void setNl(String nl) {
        this.nl = nl;
    }

    public String getFr() {
        return fr;
    }

    public void setFr(String fr) {
        this.fr = fr;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(nl);
        dest.writeString(fr);
    }

    public StatusDescription(Parcel pc) {
        nl = pc.readString();
        fr = pc.readString();

    }

    public static final Creator<StatusDescription> CREATOR = new
            Creator<StatusDescription>() {
                public StatusDescription createFromParcel(Parcel pc) {
                    return new StatusDescription(pc);
                }

                public StatusDescription[] newArray(int size) {
                    return new StatusDescription[size];
                }
            };
}
