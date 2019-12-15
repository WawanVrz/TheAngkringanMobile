package com.example.theangkringan.models;

import android.os.Parcel;
import android.os.Parcelable;

public class RatingModel implements Parcelable {
    private String recipe_id = "";
    private float totalrating = 0;
    private int totalcomment = 0;


    public String getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(String recipe_id) {
        this.recipe_id = recipe_id;
    }

    public float getTotalrating() {
        return totalrating;
    }

    public void setTotalrating(int totalrating) {
        this.totalrating = totalrating;
    }

    public int getTotalcomment() {
        return totalcomment;
    }

    public void setTotalcomment(int totalcomment) {
        this.totalcomment = totalcomment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.recipe_id);
        dest.writeFloat(this.totalrating);
        dest.writeInt(this.totalcomment);
    }

    public RatingModel() {
    }

    protected RatingModel(Parcel in) {
        this.recipe_id = in.readString();
        this.totalrating = in.readInt();
        this.totalcomment = in.readInt();
    }

    public static final Parcelable.Creator<RatingModel> CREATOR = new Parcelable.Creator<RatingModel>() {
        @Override
        public RatingModel createFromParcel(Parcel source) {
            return new RatingModel(source);
        }

        @Override
        public RatingModel[] newArray(int size) {
            return new RatingModel[size];
        }
    };
}
