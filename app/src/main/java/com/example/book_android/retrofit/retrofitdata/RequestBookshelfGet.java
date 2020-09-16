package com.example.book_android.retrofit.retrofitdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestBookshelfGet {
    @SerializedName("uid")
    @Expose
    private String uid;

    @SerializedName("bid")
    @Expose
    private BookshelfBidItem bid;

    @SerializedName("review")
    @Expose
    private String review;

    public RequestBookshelfGet(String uid, BookshelfBidItem bid, String review) {
        this.uid = uid;
        this.bid = bid;
        this.review = review;
    }

    public String getUid() {
        return uid;
    }

    public BookshelfBidItem getBid() {
        return bid;
    }

    public String getReview() {
        return review;
    }
}
