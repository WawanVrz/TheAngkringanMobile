package com.example.theangkringan.models;

import android.os.Parcel;
import android.os.Parcelable;

public class RecipeModel implements Parcelable {

    private int id;
    private int user_id;
    private String recipe_name;
    private int recipe_category;
    private String description;
    private String image = null;
    private String gallery = null;
    private String video = null;
    private String inggridients;
    private String cookmethd;
    private String notes = null;
    private int province;
    private int city;
    private int status;
    private String views = null;
    private String whislist = null;
    private String share = null;
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

    public String getRecipe_name() {
        return recipe_name;
    }

    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    public int getRecipe_category() {
        return recipe_category;
    }

    public void setRecipe_category(int recipe_category) {
        this.recipe_category = recipe_category;
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

    public String getGallery() {
        return gallery;
    }

    public void setGallery(String gallery) {
        this.gallery = gallery;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getInggridients() {
        return inggridients;
    }

    public void setInggridients(String inggridients) {
        this.inggridients = inggridients;
    }

    public String getCookmethd() {
        return cookmethd;
    }

    public void setCookmethd(String cookmethd) {
        this.cookmethd = cookmethd;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getProvince() {
        return province;
    }

    public void setProvince(int province) {
        this.province = province;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getWhislist() {
        return whislist;
    }

    public void setWhislist(String whislist) {
        this.whislist = whislist;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
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
        dest.writeString(this.recipe_name);
        dest.writeInt(this.recipe_category);
        dest.writeString(this.description);
        dest.writeString(this.image);
        dest.writeString(this.gallery);
        dest.writeString(this.video);
        dest.writeString(this.inggridients);
        dest.writeString(this.cookmethd);
        dest.writeString(this.notes);
        dest.writeInt(this.province);
        dest.writeInt(this.city);
        dest.writeInt(this.status);
        dest.writeString(this.views);
        dest.writeString(this.whislist);
        dest.writeString(this.share);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeString(this.deleted_at);
    }

    public RecipeModel() {
    }

    protected RecipeModel(Parcel in) {
        this.id = in.readInt();
        this.user_id = in.readInt();
        this.recipe_name = in.readString();
        this.recipe_category = in.readInt();
        this.description = in.readString();
        this.image = in.readString();
        this.gallery = in.readString();
        this.video = in.readString();
        this.inggridients = in.readString();
        this.cookmethd = in.readString();
        this.notes = in.readString();
        this.province = in.readInt();
        this.city = in.readInt();
        this.status = in.readInt();
        this.views = in.readString();
        this.whislist = in.readString();
        this.share = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.deleted_at = in.readString();
    }

    public static final Parcelable.Creator<RecipeModel> CREATOR = new Parcelable.Creator<RecipeModel>() {
        @Override
        public RecipeModel createFromParcel(Parcel source) {
            return new RecipeModel(source);
        }

        @Override
        public RecipeModel[] newArray(int size) {
            return new RecipeModel[size];
        }
    };
}
