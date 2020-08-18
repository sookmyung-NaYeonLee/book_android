package com.example.book_android.ui.bookshelf;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.book_android.MainActivity;
import com.example.book_android.R;

public class BookshelfFragment extends Fragment {

    private BookshelfViewModel BookshelfViewModel;
    MainActivity activity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //이 프래그먼트가 올라가있는 액티비티를 참조하는 메소드
        activity = (MainActivity)getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        BookshelfViewModel =
                ViewModelProviders.of(this).get(BookshelfViewModel.class);
        View root = inflater.inflate(R.layout.fragment_bookshelf, container, false);
        final TextView textView = root.findViewById(R.id.text_bookshelf);
        BookshelfViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        activity.setToolbarTitle("나만의 책장");
        return root;
    }
}