package com.Bookey.book_android.ui.basket;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Bookey.book_android.DetailActivity;
import com.Bookey.book_android.R;
import com.Bookey.book_android.dataclass.BasketItem;
import com.Bookey.book_android.retrofit.retrofitdata.RequestBookGet;
import com.Bookey.book_android.MainActivity;
import com.Bookey.book_android.retrofit.RetrofitManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class BasketFragment extends Fragment {

    private RecyclerView recyclerView;
    private BasketAdapter adapter;
    private ArrayList<BasketItem> list = new ArrayList<>();
    MainActivity activity;

    public static int BASKET_FRAGMENT = 200;

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
        View root = inflater.inflate(R.layout.fragment_basket, container, false);
        activity.setToolbarTitle("찜");
        adapter = new BasketAdapter(list, activity);
        setList();
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
                String bid = item.getBid();
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra("bid", bid);
                intent.putExtra("requestCode", BASKET_FRAGMENT);
                startActivityForResult(intent, BASKET_FRAGMENT);
            }
        });
    }

    private void setList(){
        ArrayList<String> basketList = getStringArrayPref("bidList");
        for(String bid : basketList){
            retrofitGetBook(bid);
        }
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable()  {
            public void run() {
                // 시간 지난 후 실행할 코딩
                adapter.setData(list);
            }
        }, 500);
    }

    //SharedPreferences 에서 찜 리스트 가져오기
    private ArrayList<String> getStringArrayPref(String key) {
        SharedPreferences prefs = activity.getSharedPreferences("basketPref", MODE_PRIVATE);
        String json = prefs.getString(key, null);
        ArrayList<String> urls = new ArrayList<>();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }

    private void retrofitGetBook(String bid){
        Call<RequestBookGet> call = RetrofitManager.createApi().getBook(bid);
        call.enqueue(new Callback<RequestBookGet>() {
            @Override
            public void onResponse(Call<RequestBookGet> call, Response<RequestBookGet> response) {
                if(response.isSuccessful()){
                    RequestBookGet bookData = response.body();
                    makeAddBasketItem(bookData);
                }else{
                    Log.d("retrofitGetBook", "Book 가져오기 실패.");
                }
            }

            @Override
            public void onFailure(Call<RequestBookGet> call, Throwable t) {
                Log.d("retrofitGetBook", "서버와 통신중 에러가 발생했습니다 : "+t.toString());
            }
        });
    }

    private void makeAddBasketItem(RequestBookGet bookData){
        String bid = bookData.getBid();
        String imgUrl = bookData.getImg_url();
        String title = bookData.getTitle();
        String writer = bookData.getAuthor();
        String publisher = bookData.getPublisher();
        String price = bookData.getPrice();
        BasketItem item = new BasketItem(bid, imgUrl, title, writer, publisher, price);
        list.add(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == BASKET_FRAGMENT){
            if(resultCode == RESULT_OK){
                list.clear();
                setList();
            }
        }
    }
}