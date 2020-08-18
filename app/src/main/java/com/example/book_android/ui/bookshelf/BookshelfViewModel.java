package com.example.book_android.ui.bookshelf;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BookshelfViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BookshelfViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is bookshelf fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}