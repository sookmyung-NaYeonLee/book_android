package com.example.book_android.retrofit.retrofitdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookshelfBidItem {
    @SerializedName("bid")
    @Expose
    private String bid;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("img_url")
    @Expose
    private String img_url;

    public BookshelfBidItem(String bid, String title, String img_url) {
        this.bid = bid;
        this.title = title;
        this.img_url = img_url;
    }

    public String getBid() {
        return bid;
    }

    public String getTitle() {
        return title;
    }

    public String getImg_url() {
        return img_url;
    }
}
