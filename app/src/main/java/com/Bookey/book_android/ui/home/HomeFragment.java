package com.Bookey.book_android.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Bookey.book_android.DetailActivity;
import com.Bookey.book_android.R;
import com.Bookey.book_android.retrofit.retrofitdata.RequestBookGet;
import com.Bookey.book_android.MainActivity;
import com.Bookey.book_android.dataclass.HomeItem;
import com.Bookey.book_android.retrofit.RetrofitManager;
import com.Bookey.book_android.retrofit.retrofitdata.RequestBestsellerGet;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private MainActivity activity;
    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;
    private SearchAdapter searchAdapter;
    private TextView bestsellerTitle;
    private ConstraintLayout homeLayout;
    private ArrayList<HomeItem> list = new ArrayList<>();
    public static int HOME_FRAGMENT = 100;

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
        retrofitGetBestseller();
        bestsellerTitle = root.findViewById(R.id.bestseller_text);
        homeLayout = root.findViewById(R.id.home_layout);
        homeAdapter = new HomeAdapter(list, activity);
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
                intent.putExtra("requestCode", HOME_FRAGMENT);
                startActivity(intent);
            }
        });
    }

    public void changeRecyclerView(ArrayList<RequestBookGet> list){
        bestsellerTitle.setVisibility(View.GONE);
        changeRecyclerViewTopMargin();
        searchAdapter = new SearchAdapter(list, activity);
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
                intent.putExtra("requestCode", HOME_FRAGMENT);
                startActivity(intent);
            }
        });
    }

    private void changeRecyclerViewTopMargin(){
        ConstraintLayout.LayoutParams plControl = (ConstraintLayout.LayoutParams) recyclerView.getLayoutParams();
        plControl.topMargin = 0;
        recyclerView.setLayoutParams(plControl);
    }

    private void retrofitGetBestseller(){
        Call<ArrayList<RequestBestsellerGet>> call = RetrofitManager.createApi().getBestseller();
        call.enqueue(new Callback<ArrayList<RequestBestsellerGet>>() {
            @Override
            public void onResponse(Call<ArrayList<RequestBestsellerGet>> call, Response<ArrayList<RequestBestsellerGet>> response) {
                if(response.isSuccessful()){
                    ArrayList<RequestBestsellerGet> retrofitList = response.body();
                    makeHomeItemList(retrofitList);
                }else{
                    Log.d("retrofitGetBestseller", "bestseller 가져오기 실패.");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<RequestBestsellerGet>> call, Throwable t) {
                Log.d("retrofitGetBestseller", "서버와 통신중 에러가 발생했습니다 : "+t.toString());
            }
        });
    }

    private void makeHomeItemList(ArrayList<RequestBestsellerGet> retrofitList){
        for(RequestBestsellerGet item : retrofitList){
            HomeItem homeItem = new HomeItem(item.getBid().getBid(), item.getBid().getImg_url(), item.getBid().getTitle(), item.getBid().getAuthor());
            list.add(homeItem);
        }
        homeAdapter.setData(list);
    }
}