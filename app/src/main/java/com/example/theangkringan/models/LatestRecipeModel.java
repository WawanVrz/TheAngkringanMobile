package com.example.theangkringan.models;

import android.os.Parcel;
import android.os.Parcelable;

public class LatestRecipeModel implements Parcelable {

    private int id;
    private String title;
    private String description;
    private String img_url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.img_url);
    }

    public LatestRecipeModel() {
    }

    protected LatestRecipeModel(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.description = in.readString();
        this.img_url = in.readString();
    }

    public static final Creator<LatestRecipeModel> CREATOR = new Creator<LatestRecipeModel>() {
        @Override
        public LatestRecipeModel createFromParcel(Parcel source) {
            return new LatestRecipeModel(source);
        }

        @Override
        public LatestRecipeModel[] newArray(int size) {
            return new LatestRecipeModel[size];
        }
    };
}
