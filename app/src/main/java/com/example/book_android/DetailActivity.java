package com.example.book_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.book_android.retrofit.RetrofitManager;
import com.example.book_android.retrofit.retrofitdata.RequestBookGet;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private ImageView backBtn, bookImg;
    private TextView bookTitle, bookWriter, bookPublisher, bookPages, bookPrice, bookDescription, bookList;
    private ProgressBar progressBar;

    private Bitmap bitmap;
    private String imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        init();
    }

    private void init(){
        findView();
        setListeners();
        getData();
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
        bookList = findViewById(R.id.detail_list);
        progressBar = findViewById(R.id.detail_progress);
    }

    private void setListeners(){
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getData(){
        Intent intent = getIntent();
        String bid = intent.getStringExtra("bid");
        retrofitGetBook(bid);
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

    private void setBookData(RequestBookGet bookData){
        imgUrl = bookData.getImg_url();
        downloadProfile();
        bookImg.setImageBitmap(bitmap);
        bookTitle.setText(bookData.getName());
        bookWriter.setText(bookData.getAuthor());
        bookPublisher.setText(bookData.getPublisher());
        bookPages.setText(bookData.getPages());
        bookPrice.setText(bookData.getPrice());
        bookDescription.setText(bookData.getDescription());
        bookList.setText(bookData.getList());
    }

    private void downloadProfile(){
        Thread thread = new Thread(){
            @Override
            public void run() {
                try{
                    URL url = new URL(imgUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        };
        thread.start();
        try{
            thread.join();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}