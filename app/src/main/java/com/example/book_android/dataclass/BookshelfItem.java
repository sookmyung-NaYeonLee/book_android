package com.example.book_android.dataclass;

public class BookshelfItem {
    private String bid;
    private String title;
    private String img_url;
    private String review;

    public BookshelfItem(String bid, String title, String img_url, String review) {
        this.bid = bid;
        this.title = title;
        this.img_url = img_url;
        this.review = review;
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

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
