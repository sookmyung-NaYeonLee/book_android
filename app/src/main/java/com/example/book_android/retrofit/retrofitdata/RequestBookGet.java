package com.example.book_android.retrofit.retrofitdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestBookGet {
    @SerializedName("Bid")
    @Expose
    private String Bid;

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

    @SerializedName("chapter")
    @Expose
    private String chapter;

    @SerializedName("img_url")
    @Expose
    private String img_url;

    @SerializedName("page")
    @Expose
    private String page;

    public RequestBookGet(String bid, String name, String author, String publisher, String price, String description, String chapter, String img_url, String page) {
        Bid = bid;
        this.name = name;
        this.author = author;
        this.publisher = publisher;
        this.price = price;
        this.description = description;
        this.chapter = chapter;
        this.img_url = img_url;
        this.page = page;
    }

    public String getBid() {
        return Bid;
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

    public String getChapter() {
        return chapter;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getPage() {
        return page;
    }
}
