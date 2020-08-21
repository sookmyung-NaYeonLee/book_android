package com.example.book_android.ui.home;

import android.content.Context;
import android.content.Intent;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_android.DetailActivity;
import com.example.book_android.MainActivity;
import com.example.book_android.R;
import com.example.book_android.dataclass.HomeItem;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private MainActivity activity;
    private RecyclerView recyclerView;
    private HomeAdapter adapter;
    private ArrayList<HomeItem> list = new ArrayList<>();

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
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        setList();
        adapter = new HomeAdapter(list);
        setRecyclerItemListener();
        recyclerView = root.findViewById(R.id.bestseller_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(adapter);

        activity.setToolbarEditText();
        return root;
    }

    private void setRecyclerItemListener(){
        adapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                HomeItem item = list.get(position);
                int img = item.getBookImg();
                String title = item.getBookTitle();
                String writer = item.getWriter();
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra("img", img);
                intent.putExtra("title", title);
                intent.putExtra("writer", writer);
                startActivity(intent);
            }
        });
    }

    private void setList(){
        list.add(new HomeItem(R.drawable.bookimg_ex,"만남은 지겹고 이별은 지쳤다", "색과 체"));
        list.add(new HomeItem(R.drawable.bookimg_ex,"만남은 지겹고 이별은 지쳤다", "색과 체"));
        list.add(new HomeItem(R.drawable.bookimg_ex,"만남은 지겹고 이별은 지쳤다", "색과 체"));
        list.add(new HomeItem(R.drawable.bookimg_ex1,"돈의 속성", "김승호"));
        list.add(new HomeItem(R.drawable.bookimg_ex1,"돈의 속성", "김승호"));
        list.add(new HomeItem(R.drawable.bookimg_ex,"만남은 지겹고 이별은 지쳤다", "색과 체"));
        list.add(new HomeItem(R.drawable.bookimg_ex,"만남은 지겹고 이별은 지쳤다", "색과 체"));
        list.add(new HomeItem(R.drawable.bookimg_ex1,"돈의 속성", "김승호"));
        list.add(new HomeItem(R.drawable.bookimg_ex,"만남은 지겹고 이별은 지쳤다", "색과 체"));
        list.add(new HomeItem(R.drawable.bookimg_ex1,"돈의 속성", "김승호"));
        list.add(new HomeItem(R.drawable.bookimg_ex1,"돈의 속성", "김승호"));
        list.add(new HomeItem(R.drawable.bookimg_ex,"만남은 지겹고 이별은 지쳤다", "색과 체"));
    }
}