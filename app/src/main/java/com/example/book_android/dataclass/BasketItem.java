package com.example.book_android.dataclass;

public class BasketItem {
    private int bookImg;
    private String bookTitle;
    private String writer;
    private String publisher;
    private String price;

    public BasketItem(int bookImg, String bookTitle, String writer, String publisher, String price) {
        this.bookImg = bookImg;
        this.bookTitle = bookTitle;
        this.writer = writer;
        this.publisher = publisher;
        this.price = price;
    }

    public int getBookImg() {
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
