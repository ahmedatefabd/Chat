package com.example.ahmed.chat.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AllUsers implements Parcelable {
    private String user_name;
    private String user_image;
    private String user_status;
    private String user_ID;

    public String getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(String user_ID) {
        this.user_ID = user_ID;
    }

    public AllUsers() {
    }

    protected AllUsers(Parcel in) {
        user_name = in.readString();
        user_image = in.readString();
        user_status = in.readString();
        user_ID = in.readString();
    }

    public static final Creator<AllUsers> CREATOR = new Creator<AllUsers>() {
        @Override
        public AllUsers createFromParcel(Parcel in) {
            return new AllUsers(in);
        }

        @Override
        public AllUsers[] newArray(int size) {
            return new AllUsers[size];
        }
    };

    public String getUser_name() {
        return user_name;
    }
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    public String getUser_image() {
        return user_image;
    }
    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }
    public String getUser_status() {
        return user_status;
    }
    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_name);
        dest.writeString(user_image);
        dest.writeString(user_status);
        dest.writeString(user_ID);
    }
}