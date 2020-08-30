package com.example.book_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class RecordActivity extends AppCompatActivity {

    private ImageView bookImg, backBtn;
    private TextView bookTitle, editBtn;
    private EditText recordText;

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
        if(recordText.getText().toString().trim().length() == 0){
            editBtn.setText("저장");
        }
        setData();
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo 수정버튼 리스너 구현
            }
        });
    }

    private void setData(){
        Intent intent = getIntent();
        int img = intent.getIntExtra("bookImg", 0);
        String title = intent.getStringExtra("bookTitle");
        bookImg.setImageResource(img);
        bookTitle.setText(title);
    }
}