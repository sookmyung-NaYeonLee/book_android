package com.example.book_android.ui.basket;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_android.DetailActivity;
import com.example.book_android.MainActivity;
import com.example.book_android.R;
import com.example.book_android.dataclass.BasketItem;
import com.example.book_android.dataclass.HomeItem;
import com.example.book_android.ui.home.HomeAdapter;

import java.util.ArrayList;

public class BasketFragment extends Fragment {

    private BasketViewModel basketViewModel;
    private RecyclerView recyclerView;
    private BasketAdapter adapter;
    private ArrayList<BasketItem> list = new ArrayList<>();
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
        basketViewModel =
                ViewModelProviders.of(this).get(BasketViewModel.class);
        View root = inflater.inflate(R.layout.fragment_basket, container, false);
        activity.setToolbarTitle("찜");
        setList();
        adapter = new BasketAdapter(list);
        setRecyclerItemListener();
        recyclerView = root.findViewById(R.id.basket_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        return root;
    }

    private void setRecyclerItemListener(){
        adapter.setOnItemClickListener(new BasketAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                BasketItem item = list.get(position);
                int img = item.getBookImg();
                String title = item.getBookTitle();
                String writer = item.getWriter();
                String publisher = item.getPublisher();
                String price = item.getPrice();
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra("img", img);
                intent.putExtra("title", title);
                intent.putExtra("writer", writer);
                intent.putExtra("publisher", publisher);
                intent.putExtra("price",price);
                startActivity(intent);
            }
        });
    }

    private void setList(){
        list.add(new BasketItem(R.drawable.bookimg_ex, "만남은 지겹고 이별은 지쳤다", "색과 체", "떠오름", "12420원"));
        list.add(new BasketItem(R.drawable.bookimg_ex, "만남은 지겹고 이별은 지쳤다", "색과 체", "떠오름", "12420원"));
        list.add(new BasketItem(R.drawable.bookimg_ex, "만남은 지겹고 이별은 지쳤다", "색과 체", "떠오름", "12420원"));
        list.add(new BasketItem(R.drawable.bookimg_ex, "만남은 지겹고 이별은 지쳤다", "색과 체", "떠오름", "12420원"));
        list.add(new BasketItem(R.drawable.bookimg_ex, "만남은 지겹고 이별은 지쳤다", "색과 체", "떠오름", "12420원"));
        list.add(new BasketItem(R.drawable.bookimg_ex, "만남은 지겹고 이별은 지쳤다", "색과 체", "떠오름", "12420원"));
        list.add(new BasketItem(R.drawable.bookimg_ex, "만남은 지겹고 이별은 지쳤다", "색과 체", "떠오름", "12420원"));
        list.add(new BasketItem(R.drawable.bookimg_ex, "만남은 지겹고 이별은 지쳤다", "색과 체", "떠오름", "12420원"));
        list.add(new BasketItem(R.drawable.bookimg_ex, "만남은 지겹고 이별은 지쳤다", "색과 체", "떠오름", "12420원"));

    }
}