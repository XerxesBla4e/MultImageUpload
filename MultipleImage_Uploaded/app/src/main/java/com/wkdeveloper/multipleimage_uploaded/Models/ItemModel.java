package com.wkdeveloper.multipleimage_uploaded.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class ItemModel implements Parcelable {
    private String ItemName, ItemDesc, ItemId;
    private ArrayList<String> ImageUrls;

    // we need empty constructor
    public ItemModel() {
    }

    public ItemModel(String itemName, String itemDesc, String itemId, ArrayList<String> imageUrls) {
        this.ItemName = itemName;
        this.ItemDesc = itemDesc;
        this.ItemId = itemId;
        this.ImageUrls = imageUrls;
    }

    protected ItemModel(Parcel in) {
        ItemName = in.readString();
        ItemDesc = in.readString();
        ItemId = in.readString();
        ImageUrls = in.createStringArrayList();
    }

    public static final Creator<ItemModel> CREATOR = new Creator<ItemModel>() {
        @Override
        public ItemModel createFromParcel(Parcel in) {
            return new ItemModel(in);
        }

        @Override
        public ItemModel[] newArray(int size) {
            return new ItemModel[size];
        }
    };

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemDesc() {
        return ItemDesc;
    }

    public void setItemDesc(String itemDesc) {
        ItemDesc = itemDesc;
    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public ArrayList<String> getImageUrls() {
        return ImageUrls;
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        ImageUrls = imageUrls;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ItemName);
        parcel.writeString(ItemDesc);
        parcel.writeString(ItemId);
        parcel.writeStringList(ImageUrls);
    }
}
