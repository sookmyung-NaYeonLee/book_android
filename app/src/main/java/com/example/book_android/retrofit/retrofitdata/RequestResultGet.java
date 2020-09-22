package com.example.book_android.retrofit.retrofitdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestResultGet {
    @SerializedName("bid")
    @Expose
    private String bid;

    @SerializedName("good")
    @Expose
    private float good;

    public RequestResultGet(String bid, float good) {
        this.bid = bid;
        this.good = good;
    }

    public String getBid() {
        return bid;
    }

    public float getGood() {
        return good;
    }
}
