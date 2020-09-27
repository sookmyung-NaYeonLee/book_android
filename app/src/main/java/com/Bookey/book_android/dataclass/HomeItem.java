package com.Bookey.book_android.dataclass;

public class HomeItem {
    private String bid;
    private String bookImg;
    private String bookTitle;
    private String writer;

    public HomeItem(String bid, String bookImg, String bookTitle, String writer) {
        this.bid = bid;
        this.bookImg = bookImg;
        this.bookTitle = bookTitle;
        this.writer = writer;
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
}
