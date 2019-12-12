package com.example.theangkringan.models;

import android.os.Parcel;
import android.os.Parcelable;

public class CategoryRecipeModel implements Parcelable {
    private int id;
    private String category_name;
    private String description;
    private String image = null;
    private int status;
    private String created_at = null;
    private String updated_at = null;
    private String deleted_at = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.category_name);
        dest.writeString(this.description);
        dest.writeString(this.image);
        dest.writeInt(this.status);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeString(this.deleted_at);
    }

    public CategoryRecipeModel() {
    }

    protected CategoryRecipeModel(Parcel in) {
        this.id = in.readInt();
        this.category_name = in.readString();
        this.description = in.readString();
        this.image = in.readString();
        this.status = in.readInt();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.deleted_at = in.readString();
    }

    public static final Parcelable.Creator<CategoryRecipeModel> CREATOR = new Parcelable.Creator<CategoryRecipeModel>() {
        @Override
        public CategoryRecipeModel createFromParcel(Parcel source) {
            return new CategoryRecipeModel(source);
        }

        @Override
        public CategoryRecipeModel[] newArray(int size) {
            return new CategoryRecipeModel[size];
        }
    };
}
