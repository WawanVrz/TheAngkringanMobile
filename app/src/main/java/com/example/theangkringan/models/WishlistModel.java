package com.example.theangkringan.models;

import android.os.Parcel;
import android.os.Parcelable;

public class WishlistModel implements Parcelable {
    private int wishlist_id;

    public int getWishlist_id() {
        return wishlist_id;
    }

    public void setWishlist_id(int wishlist_id) {
        this.wishlist_id = wishlist_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.wishlist_id);
    }

    public WishlistModel() {
    }

    protected WishlistModel(Parcel in) {
        this.wishlist_id = in.readInt();
    }

    public static final Parcelable.Creator<WishlistModel> CREATOR = new Parcelable.Creator<WishlistModel>() {
        @Override
        public WishlistModel createFromParcel(Parcel source) {
            return new WishlistModel(source);
        }

        @Override
        public WishlistModel[] newArray(int size) {
            return new WishlistModel[size];
        }
    };
}
