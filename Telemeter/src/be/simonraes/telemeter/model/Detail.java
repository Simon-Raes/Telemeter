package be.simonraes.telemeter.model;

/**
 * Created by Simon Raes on 13/06/2014.
 */
public class Detail {

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
}
