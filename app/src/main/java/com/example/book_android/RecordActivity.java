package com.example.book_android;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.book_android.retrofit.RetrofitManager;
import com.example.book_android.retrofit.retrofitdata.RequestBookshelfPost;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecordActivity extends AppCompatActivity {

    private ImageView bookImg, backBtn;
    private TextView bookTitle, editBtn;
    private EditText recordText;
    private String uid, bid;
    public static int RECORD_ACTIVITY = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        init();
    }

    private void init(){
        bookImg = findViewById(R.id.record_book_img);
        bookTitle = findViewById(R.id.record_book_title);
        backBtn = findViewById(R.id.record_back_btn);
        editBtn = findViewById(R.id.record_edit_btn);
        recordText = findViewById(R.id.record_edittext);
        setData();
//        if(recordText.getText().toString().trim().length() == 0){
//            editBtn.setText("저장");
//        }
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("review", recordText.getText().toString());
                intent.putExtra("bid", bid);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String review = recordText.getText().toString();
                RequestBookshelfPost item = new RequestBookshelfPost(review, uid, bid);
                retrofitPutBookshelf(item);
                AlertDialog.Builder builder = new AlertDialog.Builder(RecordActivity.this);
                builder.setMessage("기록이 저장되었습니다.");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    private void retrofitPutBookshelf(RequestBookshelfPost item){
        Call<RequestBookshelfPost> call = RetrofitManager.createApi().putBookshelf(uid, bid, item);
        call.enqueue(new Callback<RequestBookshelfPost>() {
            @Override
            public void onResponse(Call<RequestBookshelfPost> call, Response<RequestBookshelfPost> response) {
                if(response.isSuccessful()){
                    Log.d("retrofitPutBookshelf", "review 저장 성공.");
                }else{
                    Log.d("retrofitPutBookshelf", "review 저장 실패.");
                }
            }

            @Override
            public void onFailure(Call<RequestBookshelfPost> call, Throwable t) {
                Log.d("retrofitPutBookshelf", "서버와 통신중 에러가 발생했습니다 : "+ t.toString());
            }
        });
    }

    private void setData(){
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        bid = intent.getStringExtra("bid");
        String imgUrl = intent.getStringExtra("imgUrl");
        Glide.with(this).load(imgUrl).into(bookImg);
        String title = intent.getStringExtra("title");
        String review = intent.getStringExtra("review");
        bookTitle.setText(title);
        recordText.setText(review);
    }
}