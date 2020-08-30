package com.example.book_android.dataclass;

public class BookshelfItem {
    int img;
    String title;

    public BookshelfItem(int img, String title) {
        this.img = img;
        this.title = title;
    }

    public int getImg() {
        return img;
    }

    public String getTitle() {
        return title;
    }
}
