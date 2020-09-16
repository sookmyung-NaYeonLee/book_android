package com.example.book_android.dataclass;

public class BasketItem {
    private String bid;
    private String bookImg;
    private String bookTitle;
    private String writer;
    private String publisher;
    private String price;

    public BasketItem(String bid, String bookImg, String bookTitle, String writer, String publisher, String price) {
        this.bid = bid;
        this.bookImg = bookImg;
        this.bookTitle = bookTitle;
        this.writer = writer;
        this.publisher = publisher;
        this.price = price;
    }

    public String getBid() {
        return bid;
    }

    public String getBookImg() {
        return bookImg;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getWriter() {
        return writer;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getPrice() {
        return price;
    }
}
