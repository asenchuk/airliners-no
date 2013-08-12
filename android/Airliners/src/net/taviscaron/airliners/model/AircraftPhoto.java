package net.taviscaron.airliners.model;

import com.google.gson.annotations.SerializedName;

/**
 * Aircraft entity class
 * @author Andrei Senchuk
 */
public class AircraftPhoto extends Aircraft {
    protected String next;
    protected String prev;
    @SerializedName("image") protected String imageUrl;
    protected String takenAt;
    protected String takenOn;
    protected String remark;
    protected String author;
    protected long pos;
    protected long count;

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrev() {
        return prev;
    }

    public void setPrev(String prev) {
        this.prev = prev;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTakenAt() {
        return takenAt;
    }

    public void setTakenAt(String takenAt) {
        this.takenAt = takenAt;
    }

    public String getTakenOn() {
        return takenOn;
    }

    public void setTakenOn(String takenOn) {
        this.takenOn = takenOn;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getPos() {
        return pos;
    }

    public void setPos(long pos) {
        this.pos = pos;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
