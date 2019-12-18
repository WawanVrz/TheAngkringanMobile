package com.example.theangkringan.models;

import android.os.Parcel;
import android.os.Parcelable;

public class UpdateProfile implements Parcelable {
    private String user_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.user_id);
    }

    public UpdateProfile() {
    }

    protected UpdateProfile(Parcel in) {
        this.user_id = in.readString();
    }

    public static final Parcelable.Creator<UpdateProfile> CREATOR = new Parcelable.Creator<UpdateProfile>() {
        @Override
        public UpdateProfile createFromParcel(Parcel source) {
            return new UpdateProfile(source);
        }

        @Override
        public UpdateProfile[] newArray(int size) {
            return new UpdateProfile[size];
        }
    };
}
