package com.example.book_android.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_android.DetailActivity;
import com.example.book_android.MainActivity;
import com.example.book_android.R;
import com.example.book_android.dataclass.HomeItem;
import com.example.book_android.retrofit.retrofitdata.RequestBookGet;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private MainActivity activity;
    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;
    private SearchAdapter searchAdapter;
    private TextView bestsellerTitle;
    private ConstraintLayout homeLayout;
    private ArrayList<HomeItem> list = new ArrayList<>();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //이 프래그먼트가 올라가있는 액티비티를 참조하는 메소드
        activity = (MainActivity)getActivity();
        activity.setSearchEditClear();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        setBestsellerList();
        bestsellerTitle = root.findViewById(R.id.bestseller_text);
        homeLayout = root.findViewById(R.id.home_layout);
        homeAdapter = new HomeAdapter(list);
        setRecyclerItemListener();
        recyclerView = root.findViewById(R.id.home_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(homeAdapter);

        activity.setToolbarEditText();
        return root;
    }

    private void setRecyclerItemListener(){
        homeAdapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                HomeItem item = list.get(position);
                String bid = item.getBid();
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra("bid", bid);
                startActivity(intent);
            }
        });
    }

    public void changeRecyclerView(ArrayList<RequestBookGet> list){
        bestsellerTitle.setVisibility(View.GONE);
        changeRecyclerViewTopMargin();
        searchAdapter = new SearchAdapter(list);
        setSearchRecyclerItemListener(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(searchAdapter);
    }

    private void setSearchRecyclerItemListener(final ArrayList<RequestBookGet> list){
        searchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                RequestBookGet item = list.get(position);
                String bid = item.getBid();
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra("bid", bid);
                startActivity(intent);
            }
        });
    }

    private void changeRecyclerViewTopMargin(){
        ConstraintLayout.LayoutParams plControl = (ConstraintLayout.LayoutParams) recyclerView.getLayoutParams();
        plControl.topMargin = 0;
        recyclerView.setLayoutParams(plControl);
    }

    private void setBestsellerList(){
        list.add(new HomeItem("9788901243665", "http://image.kyobobook.co.kr/images/book/large/665/l9788901243665.jpg","김미경의 리부트", "김미경"));
        list.add(new HomeItem("9788901244600", "http://image.kyobobook.co.kr/images/book/large/600/l9788901244600.jpg","살고 싶다는 농담", "허지웅"));
        list.add(new HomeItem("9788932919126", "http://image.kyobobook.co.kr/images/book/large/126/l9788932919126.jpg","고양이. 1", "베르나르 베르베르"));
        list.add(new HomeItem("9788932919133", "http://image.kyobobook.co.kr/images/book/large/133/l9788932919133.jpg","고양이. 2", "베르나르 베르베르"));
        list.add(new HomeItem("9788932920405", "http://image.kyobobook.co.kr/images/book/large/405/l9788932920405.jpg","심판", "베르나르 베르베르"));
        list.add(new HomeItem("9788959130108", "http://image.kyobobook.co.kr/images/book/large/108/l9788959130108.jpg","재수의 연습장", "재수"));
        list.add(new HomeItem("9791165210144", "http://image.kyobobook.co.kr/images/book/large/144/l9791165210144.jpg","주식투자 무작정 따라하기(2020)", "윤재수"));
        list.add(new HomeItem("9791186757581", "http://image.kyobobook.co.kr/images/book/large/581/l9791186757581.jpg","이렇게 될 줄 몰랐습니다", "재수"));
        list.add(new HomeItem("9791188331796", "http://image.kyobobook.co.kr/images/book/large/796/l9791188331796.jpg","돈의 속성", "김승호"));
        list.add(new HomeItem("9791189320690", "http://image.kyobobook.co.kr/images/book/large/690/l9791189320690.jpg","나 혼자만 레벨업. 3(만화)", "장성락 (REDICE STUDIO), 추공 (원작) 지음"));
        list.add(new HomeItem("9791190382175", "http://image.kyobobook.co.kr/images/book/large/175/l9791190382175.jpg","더 해빙(The Having) 부와 행운을 끌어당기는 힘", "이서윤 , 홍주연"));
        list.add(new HomeItem("9791190413152", "http://image.kyobobook.co.kr/images/book/large/152/l9791190413152.jpg","한번도 경험해보지 못한 나라", "강양구 , 권경애 , 김경율 , 서민 , 진중권"));
    }
}