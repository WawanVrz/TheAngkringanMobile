package com.example.theangkringan.models;

import android.os.Parcel;
import android.os.Parcelable;

public class AddRecipeModel implements Parcelable {
    private int recipe_id;
    private String recipe_name;

    public int getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.recipe_id);
        dest.writeString(this.recipe_name);
    }

    public AddRecipeModel() {
    }

    protected AddRecipeModel(Parcel in) {
        this.recipe_id = in.readInt();
        this.recipe_name = in.readString();
    }

    public static final Parcelable.Creator<AddRecipeModel> CREATOR = new Parcelable.Creator<AddRecipeModel>() {
        @Override
        public AddRecipeModel createFromParcel(Parcel source) {
            return new AddRecipeModel(source);
        }

        @Override
        public AddRecipeModel[] newArray(int size) {
            return new AddRecipeModel[size];
        }
    };
}
