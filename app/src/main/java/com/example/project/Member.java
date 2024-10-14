package com.example.project;
import android.os.Parcel;
import android.os.Parcelable;

public class Member implements Parcelable {

    private String name;
    private String status;
    private int profileImageResId; // For profile image resource
    private boolean isSelected;

    // Constructor
    public Member(String name, String status, int profileImageResId, boolean isSelected) {
        this.name = name;
        this.status = status;
        this.profileImageResId = profileImageResId;
        this.isSelected = isSelected;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public int getProfileImageResId() {
        return profileImageResId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setProfileImageResId(int profileImageResId) {
        this.profileImageResId = profileImageResId;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    // Parcelable methods implementation
    protected Member(Parcel in) {
        name = in.readString();
        status = in.readString();
        profileImageResId = in.readInt();
        isSelected = in.readByte() != 0;
    }

    public static final Creator<Member> CREATOR = new Creator<Member>() {
        @Override
        public Member createFromParcel(Parcel in) {
            return new Member(in);
        }

        @Override
        public Member[] newArray(int size) {
            return new Member[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(status);
        parcel.writeInt(profileImageResId);
        parcel.writeByte((byte) (isSelected ? 1 : 0));
    }
}



