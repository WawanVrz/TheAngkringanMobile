package com.example.theangkringan.models;

import android.os.Parcel;
import android.os.Parcelable;

public class AddReviewModel implements Parcelable {
    private int review_id;

    public int getReview_id() {
        return review_id;
    }

    public void setReview_id(int review_id) {
        this.review_id = review_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.review_id);
    }

    public AddReviewModel() {
    }

    protected AddReviewModel(Parcel in) {
        this.review_id = in.readInt();
    }

    public static final Parcelable.Creator<AddReviewModel> CREATOR = new Parcelable.Creator<AddReviewModel>() {
        @Override
        public AddReviewModel createFromParcel(Parcel source) {
            return new AddReviewModel(source);
        }

        @Override
        public AddReviewModel[] newArray(int size) {
            return new AddReviewModel[size];
        }
    };
}
