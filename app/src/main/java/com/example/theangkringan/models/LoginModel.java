package com.example.theangkringan.models;

import android.os.Parcel;
import android.os.Parcelable;

public class LoginModel implements Parcelable {
    private int user_id;
    private String name;
    private String email;
    private String avaimg;
    private String token;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvaimg() {
        return avaimg;
    }

    public void setAvaimg(String avaimg) {
        this.avaimg = avaimg;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.user_id);
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeString(this.avaimg);
        dest.writeString(this.token);
    }

    public LoginModel() {
    }

    protected LoginModel(Parcel in) {
        this.user_id = in.readInt();
        this.name = in.readString();
        this.email = in.readString();
        this.avaimg = in.readString();
        this.token = in.readString();
    }

    public static final Parcelable.Creator<LoginModel> CREATOR = new Parcelable.Creator<LoginModel>() {
        @Override
        public LoginModel createFromParcel(Parcel source) {
            return new LoginModel(source);
        }

        @Override
        public LoginModel[] newArray(int size) {
            return new LoginModel[size];
        }
    };
}
