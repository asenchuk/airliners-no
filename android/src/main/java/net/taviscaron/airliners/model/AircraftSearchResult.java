package net.taviscaron.airliners.model;

import com.google.gson.annotations.SerializedName;

/**
 * Search result entity item
 * @author Andrei Senchuk
 */
public class AircraftSearchResult extends Aircraft {
    @SerializedName("thumb") protected String thumbUrl;
    protected String place;
    protected String country;
    protected String date;
    protected String author;

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
