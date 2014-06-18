package be.simonraes.telemeter.model;

/**
 * Created by Simon Raes on 13/06/2014.
 */
public class StatusDescription {
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
}
