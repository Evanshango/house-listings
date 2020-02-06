package com.evans.multiimageupload.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class House implements Parcelable {

    private String houseId, category, location, desc, likesCount;
    private List<ImageUrl> imageUrls;

    public House() {
    }

    public House(String houseId, String category, String location, String desc, String likesCount,
                 List<ImageUrl> imageUrls) {
        this.houseId = houseId;
        this.category = category;
        this.location = location;
        this.desc = desc;
        this.likesCount = likesCount;
        this.imageUrls = imageUrls;
    }

    protected House(Parcel in) {
        houseId = in.readString();
        category = in.readString();
        location = in.readString();
        desc = in.readString();
        likesCount = in.readString();
        imageUrls = in.createTypedArrayList(ImageUrl.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(houseId);
        dest.writeString(category);
        dest.writeString(location);
        dest.writeString(desc);
        dest.writeString(likesCount);
        dest.writeTypedList(imageUrls);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<House> CREATOR = new Creator<House>() {
        @Override
        public House createFromParcel(Parcel in) {
            return new House(in);
        }

        @Override
        public House[] newArray(int size) {
            return new House[size];
        }
    };

    public String getHouseId() {
        return houseId;
    }

    public String getCategory() {
        return category;
    }

    public String getLocation() {
        return location;
    }

    public String getDesc() {
        return desc;
    }

    public String getLikesCount() {
        return likesCount;
    }

    public List<ImageUrl> getImageUrls() {
        return imageUrls;
    }
}
