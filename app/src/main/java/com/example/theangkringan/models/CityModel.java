package com.example.theangkringan.models;

import android.os.Parcel;
import android.os.Parcelable;

public class CityModel implements Parcelable {
    private int id;
    private String name;
    private int master_province_id;
    private String created_at = null;
    private String updated_at = null;
    private String deleted_at = null;

    @Override
    public String toString() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaster_province_id() {
        return master_province_id;
    }

    public void setMaster_province_id(int master_province_id) {
        this.master_province_id = master_province_id;
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
        dest.writeString(this.name);
        dest.writeInt(this.master_province_id);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeString(this.deleted_at);
    }

    public CityModel() {
    }

    protected CityModel(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.master_province_id = in.readInt();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.deleted_at = in.readString();
    }

    public static final Parcelable.Creator<CityModel> CREATOR = new Parcelable.Creator<CityModel>() {
        @Override
        public CityModel createFromParcel(Parcel source) {
            return new CityModel(source);
        }

        @Override
        public CityModel[] newArray(int size) {
            return new CityModel[size];
        }
    };
}
