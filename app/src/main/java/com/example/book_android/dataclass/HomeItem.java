package com.example.book_android.dataclass;

public class HomeItem {
    private int bookImg;
    private String bookTitle;
    private String writer;

    public HomeItem(int bookImg, String bookTitle, String writer) {
        this.bookImg = bookImg;
        this.bookTitle = bookTitle;
        this.writer = writer;
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
}
