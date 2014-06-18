package be.simonraes.telemeter.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Simon Raes on 13/06/2014.
 */
public class Detail implements Parcelable {

    private String code, description;

    public Detail() {

    }

    public Detail(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(code);
        dest.writeString(description);
    }

    public Detail(Parcel pc) {
        code = pc.readString();
        description = pc.readString();
    }

    public static final Creator<Detail> CREATOR = new
            Creator<Detail>() {
                public Detail createFromParcel(Parcel pc) {
                    return new Detail(pc);
                }

                public Detail[] newArray(int size) {
                    return new Detail[size];
                }
            };
}
