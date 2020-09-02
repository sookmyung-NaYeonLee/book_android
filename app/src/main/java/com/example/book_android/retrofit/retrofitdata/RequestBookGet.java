package com.example.book_android.retrofit.retrofitdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestBookGet {
    @SerializedName("bid")
    @Expose
    private String bid;

    @SerializedName("name")
    @Expose
    private String name;

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

    @SerializedName("list")
    @Expose
    private String list;

    @SerializedName("img_url")
    @Expose
    private String img_url;

    @SerializedName("pages")
    @Expose
    private String pages;

    public RequestBookGet(String bid, String name, String author, String publisher, String price, String description, String list, String img_url, String pages) {
        this.bid = bid;
        this.name = name;
        this.author = author;
        this.publisher = publisher;
        this.price = price;
        this.description = description;
        this.list = list;
        this.img_url = img_url;
        this.pages = pages;
    }

    public String getBid() {
        return bid;
    }

    public String getName() {
        return name;
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

    public String getList() {
        return list;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getPages() {
        return pages;
    }
}
