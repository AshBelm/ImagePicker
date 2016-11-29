package com.mcmo.z.imagepicker.library.info;

import android.provider.MediaStore;

/**
 * Created by weizhang210142 on 2016/5/4.
 */
public class ImagesInfo extends BaseImagesInfo{
    private String description;
    private String picasaId;
    private int isPrivate;
    private double latitude;
    private double longitude;
    private int dateTaken;
    private int orientation;
    private int miniThumbMagic;
    private String title;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicasaId() {
        return picasaId;
    }

    public void setPicasaId(String picasaId) {
        this.picasaId = picasaId;
    }

    public int getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(int isPrivate) {
        this.isPrivate = isPrivate;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(int dateTaken) {
        this.dateTaken = dateTaken;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getMiniThumbMagic() {
        return miniThumbMagic;
    }

    public void setMiniThumbMagic(int miniThumbMagic) {
        this.miniThumbMagic = miniThumbMagic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "ImagesInfo{" +
                "description='" + description + '\'' +
                ", picasaId='" + picasaId + '\'' +
                ", isPrivate=" + isPrivate +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", dateTaken=" + dateTaken +
                ", orientation=" + orientation +
                ", miniThumbMagic=" + miniThumbMagic +
                ", title='" + title + '\'' + super.toString()+
                '}';
    }
}
