package com.example.theangkringan.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ReviewModel implements Parcelable {
    private int id;
    private int user_id;
    private int recipe_id;
    private String fullname;
    private String email;
    private float rating = 0;
    private String comment;
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
        dest.writeInt(this.user_id);
        dest.writeInt(this.recipe_id);
        dest.writeString(this.fullname);
        dest.writeString(this.email);
        dest.writeFloat(this.rating);
        dest.writeString(this.comment);
        dest.writeInt(this.status);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeString(this.deleted_at);
    }

    public ReviewModel() {
    }

    protected ReviewModel(Parcel in) {
        this.id = in.readInt();
        this.user_id = in.readInt();
        this.recipe_id = in.readInt();
        this.fullname = in.readString();
        this.email = in.readString();
        this.rating = in.readInt();
        this.comment = in.readString();
        this.status = in.readInt();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.deleted_at = in.readString();
    }

    public static final Parcelable.Creator<ReviewModel> CREATOR = new Parcelable.Creator<ReviewModel>() {
        @Override
        public ReviewModel createFromParcel(Parcel source) {
            return new ReviewModel(source);
        }

        @Override
        public ReviewModel[] newArray(int size) {
            return new ReviewModel[size];
        }
    };
}
