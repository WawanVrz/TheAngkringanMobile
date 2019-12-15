package com.example.theangkringan.models;

import android.os.Parcel;
import android.os.Parcelable;

public class DetailUser implements Parcelable {
    private int id;
    private int role_id;
    private String fullname;
    private String email;
    private String phone;
    private String address;
    private String gender;
    private String password;
    private String remember_token = null;
    private String avatar;
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

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRemember_token() {
        return remember_token;
    }

    public void setRemember_token(String remember_token) {
        this.remember_token = remember_token;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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
        dest.writeInt(this.role_id);
        dest.writeString(this.fullname);
        dest.writeString(this.email);
        dest.writeString(this.phone);
        dest.writeString(this.address);
        dest.writeString(this.gender);
        dest.writeString(this.password);
        dest.writeString(this.remember_token);
        dest.writeString(this.avatar);
        dest.writeInt(this.status);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeString(this.deleted_at);
    }

    public DetailUser() {
    }

    protected DetailUser(Parcel in) {
        this.id = in.readInt();
        this.role_id = in.readInt();
        this.fullname = in.readString();
        this.email = in.readString();
        this.phone = in.readString();
        this.address = in.readString();
        this.gender = in.readString();
        this.password = in.readString();
        this.remember_token = in.readString();
        this.avatar = in.readString();
        this.status = in.readInt();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.deleted_at = in.readString();
    }

    public static final Parcelable.Creator<DetailUser> CREATOR = new Parcelable.Creator<DetailUser>() {
        @Override
        public DetailUser createFromParcel(Parcel source) {
            return new DetailUser(source);
        }

        @Override
        public DetailUser[] newArray(int size) {
            return new DetailUser[size];
        }
    };
}
