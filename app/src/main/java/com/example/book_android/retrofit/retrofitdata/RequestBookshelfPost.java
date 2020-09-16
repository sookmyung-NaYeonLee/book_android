package com.example.book_android.retrofit.retrofitdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestBookshelfPost {
    @SerializedName("review")
    @Expose
    private String review;

    @SerializedName("uid")
    @Expose
    private String uid;

    @SerializedName("bid")
    @Expose
    private String bid;

    public RequestBookshelfPost(String review, String uid, String bid) {
        this.review = review;
        this.uid = uid;
        this.bid = bid;
    }

    public String getReview() {
        return review;
    }

    public String getUid() {
        return uid;
    }

    public String getBid() {
        return bid;
    }
}
