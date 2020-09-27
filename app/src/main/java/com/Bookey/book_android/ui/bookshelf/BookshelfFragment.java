package com.Bookey.book_android.ui.bookshelf;

import android.content.Context;
import android.content.Intent;
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
import com.Bookey.book_android.retrofit.retrofitdata.BookshelfBidItem;
import com.Bookey.book_android.retrofit.retrofitdata.RequestBookshelfGet;
import com.Bookey.book_android.MainActivity;
import com.Bookey.book_android.RecordActivity;
import com.Bookey.book_android.dataclass.BookshelfItem;
import com.Bookey.book_android.retrofit.RetrofitManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BookshelfFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<ArrayList<BookshelfItem>> allList = new ArrayList<>();
    private BookshelfVerticalAdapter adapter;
    private String uid;
    MainActivity activity;
    public static int BOOKSHELF_FRAGMENT = 400;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //이 프래그먼트가 올라가있는 액티비티를 참조하는 메소드
        activity = (MainActivity)getActivity();
        uid = activity.getUid();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bookshelf, container, false);

        activity.setToolbarTitle("나만의 책장");
        recyclerView = root.findViewById(R.id.bookshelf_recycler);

        adapter = new BookshelfVerticalAdapter(getContext(), activity, allList);
        setData();
        setAdapterListeners();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        return root;
    }

    private void setData(){
        retrofitGetBookshelfTotal(uid);
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable()  {
            public void run() {
                // 시간 지난 후 실행할 코딩
                if(adapter.getItemCount() < 3){
                    for(int i = adapter.getItemCount(); i < 3; i++) {
                        allList.add(new ArrayList<BookshelfItem>());
                    }
                }
                adapter.setmDataList(allList);
            }
        }, 500);
    }

    private void setAdapterListeners(){
        adapter.setOnBookImgClickListener2(new BookshelfVerticalAdapter.OnBookImgClickListener2() {
            @Override
            public void onBookImgClick2(BookshelfItem item) {
                String bid = item.getBid();
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra("bid", bid);
                intent.putExtra("requestCode", BOOKSHELF_FRAGMENT);
                startActivityForResult(intent, BOOKSHELF_FRAGMENT);
            }
        });
        adapter.setOnRecordBtnClickListener2(new BookshelfVerticalAdapter.OnRecordBtnClickListener2() {
            @Override
            public void onRecordBtnClick2(BookshelfItem item) {
                String bid = item.getBid();
                String imgUrl = item.getImg_url();
                String title = item.getTitle();
                String review = item.getReview();
                Intent intent = new Intent(getContext(), RecordActivity.class);
                intent.putExtra("uid", uid);
                intent.putExtra("bid", bid);
                intent.putExtra("imgUrl", imgUrl);
                intent.putExtra("title", title);
                intent.putExtra("review", review);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        allList.clear();
        setData();
    }

    private void retrofitGetBookshelfTotal(final String uid){
        Call<ArrayList<RequestBookshelfGet>> call = RetrofitManager.createApi().getBookshelfTotal(uid);
        call.enqueue(new Callback<ArrayList<RequestBookshelfGet>>() {
            @Override
            public void onResponse(Call<ArrayList<RequestBookshelfGet>> call, Response<ArrayList<RequestBookshelfGet>> response) {
                if(response.isSuccessful()){
                    ArrayList<RequestBookshelfGet> retrofitBookshelfList = response.body();
                    makeBookshelfItemList(retrofitBookshelfList);
                }else{
                    Log.d("retrofitGetBookshelfTot", "Bookshelf table에서 데이터 가져오기 실패.");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<RequestBookshelfGet>> call, Throwable t) {
                Log.d("retrofitGetBookshelfTot","서버와 통신중 에러가 발생했습니다 : "+t.toString());
            }
        });
    }

    private void makeBookshelfItemList(ArrayList<RequestBookshelfGet> retrofitBookshelfList){
        for(int i = 0; i < retrofitBookshelfList.size(); i++){
            ArrayList<BookshelfItem> list = new ArrayList<>();
            for(int j = 0; j < 3; j++){
                if(retrofitBookshelfList.size() < i+1){
                    i++;
                    break;
                }
                RequestBookshelfGet item = retrofitBookshelfList.get(i++);
                BookshelfBidItem bidItem = item.getBid();
                String bid = bidItem.getBid();
                String title = bidItem.getTitle();
                String imgUrl = bidItem.getImg_url();
                String review = item.getReview();
                BookshelfItem bookshelfItem = new BookshelfItem(bid, title, imgUrl, review);
                list.add(bookshelfItem);
            }
            i--;
            allList.add(list);
        }
    }
}

