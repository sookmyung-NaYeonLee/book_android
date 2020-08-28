package com.example.book_android.ui.bookshelf;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_android.MainActivity;
import com.example.book_android.R;
import com.example.book_android.dataclass.BookshelfItem;

import java.util.ArrayList;

public class BookshelfFragment extends Fragment {

    private BookshelfViewModel BookshelfViewModel;
    private RecyclerView recyclerView;
    private ArrayList<ArrayList<BookshelfItem>> allList = new ArrayList<>();
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

        activity.setToolbarTitle("나만의 책장");
        recyclerView = root.findViewById(R.id.bookshelf_recycler);
        initData();
        BookshelfVerticalAdapter adapter = new BookshelfVerticalAdapter(getContext(), allList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        if(adapter.getItemCount() < 3){
            for(int i = adapter.getItemCount(); i < 3; i++) {
                allList.add(new ArrayList<BookshelfItem>());
                adapter.setmDataList(allList);
            }
        }
        return root;
    }

    private void initData(){
        ArrayList<BookshelfItem> list1 = new ArrayList<>();
        list1.add(new BookshelfItem(R.drawable.bookimg_ex));
        list1.add(new BookshelfItem(R.drawable.bookimg_ex1));
        list1.add(new BookshelfItem(R.drawable.bookimg_ex));
        allList.add(list1);

        ArrayList<BookshelfItem> list2 = new ArrayList<>();
        list2.add(new BookshelfItem(R.drawable.bookimg_ex));
        list2.add(new BookshelfItem(R.drawable.bookimg_ex1));
        allList.add(list2);
    }
}