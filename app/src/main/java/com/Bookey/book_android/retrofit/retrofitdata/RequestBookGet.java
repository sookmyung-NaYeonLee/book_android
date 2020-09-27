package com.Bookey.book_android.retrofit.retrofitdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestBookGet {
    @SerializedName("bid")
    @Expose
    private String bid;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("author")
    @Expose
    private String author;

    @SerializedName("publisher")
    @Expose
    private String publisher;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("img_url")
    @Expose
    private String img_url;

    @SerializedName("pages")
    @Expose
    private String pages;

    public RequestBookGet(String bid, String title, String author, String publisher, String price, String description, String img_url, String pages) {
        this.bid = bid;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.price = price;
        this.description = description;
        this.img_url = img_url;
        this.pages = pages;
    }

    public String getBid() {
        return bid;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getPages() {
        return pages;
    }
}
