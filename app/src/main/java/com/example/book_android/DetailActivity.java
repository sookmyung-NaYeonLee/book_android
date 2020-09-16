package com.example.book_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.book_android.retrofit.RetrofitManager;
import com.example.book_android.retrofit.retrofitdata.RequestBookGet;
import com.example.book_android.retrofit.retrofitdata.RequestBookshelfGet;
import com.example.book_android.retrofit.retrofitdata.RequestBookshelfPost;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private ImageView backBtn, bookImg;
    private TextView bookTitle, bookWriter, bookPublisher, bookPages, bookPrice, bookDescription;
    private ProgressBar progressBar;
    private CheckBox basketCheck, bookshelfCheck;

    private String imgUrl, bid, uid;
    private int requestCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        init();
    }

    private void init(){
        findView();
        getData();
        setListeners();
    }

    private void findView(){
        backBtn = findViewById(R.id.back_btn);
        bookImg = findViewById(R.id.detail_book_img);
        bookTitle = findViewById(R.id.detail_book_title);
        bookWriter = findViewById(R.id.detail_book_writer);
        bookPublisher = findViewById(R.id.detail_book_publisher);
        bookPages = findViewById(R.id.detail_book_pages);
        bookPrice = findViewById(R.id.detail_book_price);
        bookDescription = findViewById(R.id.detail_description);
        progressBar = findViewById(R.id.detail_progress);
        basketCheck = findViewById(R.id.basket_checkbox);
        bookshelfCheck = findViewById(R.id.bookshelf_checkbox);
    }

    private void setListeners(){
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(requestCode == 200 || requestCode == 400){
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                }
                finish();
            }
        });
        basketCheck.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> basketList = getStringArrayPref("bidList");
                if(((CheckBox)view).isChecked()){
                    basketList.add(bid);
                    Log.d("basketCheckBtn", "추가 됨");
                }else{
                    basketList.remove(bid);
                    Log.d("basketCheckBtn", "삭제 됨");
                }
                setStringArrayPref("bidList", basketList);
            }
        }));
        bookshelfCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CheckBox)view).isChecked()){
                    RequestBookshelfPost item = new RequestBookshelfPost("", uid, bid);
                    retrofitPostBookshelf(item);
                }else{
                    retrofitDeleteBookshelf(uid, bid);
                }
            }
        });
    }

    //SharedPreferences 에 ArrayList<String> 형태로 찜 저장
    private void setStringArrayPref(String key, ArrayList<String> values) {
        SharedPreferences prefs = getSharedPreferences("basketPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray a = new JSONArray();
        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
        }
        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }
        editor.apply();
    }
    //SharedPreferences 에서 찜 리스트 가져오기
    private ArrayList<String> getStringArrayPref(String key) {
        SharedPreferences prefs = getSharedPreferences("basketPref", MODE_PRIVATE);
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

    private void retrofitPostBookshelf(RequestBookshelfPost item){
        Call<RequestBookshelfPost> call = RetrofitManager.createApi().postBookshelf(item);
        call.enqueue(new Callback<RequestBookshelfPost>() {
            @Override
            public void onResponse(Call<RequestBookshelfPost> call, Response<RequestBookshelfPost> response) {
                if(response.isSuccessful()){
                    Log.d("retrofitPostBookshelf", "Bookshelf 저장하기 성공.");
                }else{
                    Log.d("retrofitPostBookshelf", "Bookshelf 저장하기 실패.");
                }
            }

            @Override
            public void onFailure(Call<RequestBookshelfPost> call, Throwable t) {
                Log.d("retrofitPostBookshelf", "서버와 통신중 에러가 발생했습니다 : "+t.toString());
            }
        });
    }

    private void retrofitDeleteBookshelf(String uid, String bid){
        Call<ResponseBody> call = RetrofitManager.createApi().deleteBookshelf(uid, bid);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.d("retrofitDeleteBookshelf", "Bookshelf 삭제 성공.");
                }else{
                    Log.d("retrofitDeleteBookshelf", "Bookshelf 삭제 실패.");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("retrofitDeleteBookshelf", "서버와 통신중 에러가 발생했습니다 : "+t.toString());
            }
        });
    }

    private void getData(){
        Intent intent = getIntent();
        bid = intent.getStringExtra("bid");
        requestCode = intent.getIntExtra("requestCode", 0);
        retrofitGetBook(bid);
        SharedPreferences userPref = getSharedPreferences("userPref", MODE_PRIVATE);
        uid = userPref.getString("uid","");
        setBasketChecked(bid);
        setBookshelfChecked(uid, bid);
    }

    private void setBasketChecked(String bid){
        ArrayList<String> basketList = getStringArrayPref("bidList");
        if(basketList.contains(bid)) {
            basketCheck.setChecked(true);
        }else{
            basketCheck.setChecked(false);
        }
    }

    private void setBookshelfChecked(String uid, String bid){
        Call<RequestBookshelfGet> call = RetrofitManager.createApi().getBookshelfOne(uid, bid);
        call.enqueue(new Callback<RequestBookshelfGet>() {
            @Override
            public void onResponse(Call<RequestBookshelfGet> call, Response<RequestBookshelfGet> response) {
                if(response.isSuccessful()){
                    bookshelfCheck.setChecked(true);
                    //Log.d("setBookshelfChecked", "Bookshelf에 있음. "+response.body().getBid().getTitle());
                }else{
                    bookshelfCheck.setChecked(false);
                    //Log.d("setBookshelfChecked", "Bookshelf에 없음.");
                }
            }

            @Override
            public void onFailure(Call<RequestBookshelfGet> call, Throwable t) {
                Log.d("setBookshelfChecked", "서버와 통신중 에러가 발생했습니다 : "+t.toString());
            }
        });
    }

    private void retrofitGetBook(String bid){
        Call<RequestBookGet> call = RetrofitManager.createApi().getBook(bid);
        call.enqueue(new Callback<RequestBookGet>() {
            @Override
            public void onResponse(Call<RequestBookGet> call, Response<RequestBookGet> response) {
                if(response.isSuccessful()){
                    RequestBookGet bookData = response.body();
                    setBookData(bookData);
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

    private void setBookData(RequestBookGet bookData) {
        imgUrl = bookData.getImg_url();
        Glide.with(this).load(imgUrl).into(bookImg);
        bookTitle.setText(bookData.getTitle());
        bookWriter.setText(bookData.getAuthor());
        bookPublisher.setText(bookData.getPublisher());
        bookPages.setText(bookData.getPages());
        bookPrice.setText(bookData.getPrice());
        bookDescription.setText(bookData.getDescription());
    }
}