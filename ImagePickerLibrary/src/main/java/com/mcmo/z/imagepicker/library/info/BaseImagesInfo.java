package com.mcmo.z.imagepicker.library.info;

/**
 * Created by weizhang210142 on 2016/5/5.
 */
public class BaseImagesInfo implements SelectAble {
    private int _id;
    private String bucketId;
    private String bucketDisplayName;
    private String data;
    private int dateAdded;
    private String mine;
    private String displayName;
    private boolean queryOver=false;
    private String thumbnail;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getBucketId() {
        return bucketId;
    }

    public void setBucketId(String bucketId) {
        this.bucketId = bucketId;
    }

    public String getBucketDisplayName() {
        return bucketDisplayName;
    }

    public void setBucketDisplayName(String bucketDisplayName) {
        this.bucketDisplayName = bucketDisplayName;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(int dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getMine() {
        return mine;
    }

    public void setMine(String mine) {
        this.mine = mine;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isQueryOver() {
        return queryOver;
    }

    public void setQueryOver(boolean queryOver) {
        this.queryOver = queryOver;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        queryOver=true;
        this.thumbnail = thumbnail;
    }

    @Override
    public String toString() {
        return "BaseImagesInfo{" +
                "_id=" + _id +
                ", bucketId='" + bucketId + '\'' +
                ", bucketDisplayName='" + bucketDisplayName + '\'' +
                ", data='" + data + '\'' +
                ", dateAdded=" + dateAdded +
                ", mine='" + mine + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }

    private boolean mSelected;

    @Override
    public boolean isSelected() {
        return mSelected;
    }

    @Override
    public void setSelect(boolean selected) {
        mSelected = selected;
    }

    @Override
    public void toggleSelect() {
        mSelected = !mSelected;
    }
}
