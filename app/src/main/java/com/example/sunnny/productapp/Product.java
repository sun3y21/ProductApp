package com.example.sunnny.productapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sunnny on 11/10/16.
 */

public class Product implements Parcelable {

    String id;
    String name;
    String description;
    String imageUrl;
    //for location longitude and latitude
    double longitude,latitude;
    double price;
    int quantity;
    double rating;



    public Product()
    {
        //this.imageUrl="http://10.0.2.2/";
        this.imageUrl="http://www.sun3y21.pe.hu/";
    }


    protected Product(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        imageUrl = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
        price = in.readDouble();
        quantity = in.readInt();
        rating = in.readDouble();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        for(int i=0;i<imageUrl.length();i++)
        {
            if(imageUrl.charAt(i)!='\\')
            this.imageUrl+=imageUrl.charAt(i);
        }
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString()
    {
        return name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(imageUrl);
        parcel.writeDouble(longitude);
        parcel.writeDouble(latitude);
        parcel.writeDouble(price);
        parcel.writeInt(quantity);
        parcel.writeDouble(rating);
    }
}
