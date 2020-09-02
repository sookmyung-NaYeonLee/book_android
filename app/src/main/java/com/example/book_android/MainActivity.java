package com.example.book_android;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.example.book_android.retrofit.RetrofitManager;
import com.google.android.material.navigation.NavigationView;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private View headerView;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ActionBar ab;
    private EditText searchEdit;
    private TextView toolbarTitle;
    private MenuItem searchBtn;
    private String uid;

    private int isHomeFirst = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        // 여기서 id들은 mobile_navigation에서 정의한 id들.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_bookshelf, R.id.nav_basket)
                .setDrawerLayout(drawer)
                .build();
        ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        headerView = navigationView.getHeaderView(0);

        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        searchBtn = menu.findItem(R.id.action_search);
        /*
        // 검색 버튼 클릭 시 searchview 길이 꽉차게 늘려줌
        SearchView searchView = (SearchView)menu.findItem(R.id.action_search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        // 검색 버튼 클릭 시 searchview 에 힌트 추가
        searchView.setQueryHint("도서명으로 검색합니다.");
        */
        return true;
    }

    //뒤로가기 버튼 위해서
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void init(){
        searchEdit = findViewById(R.id.search_edittext);
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setVisibility(View.GONE);
        setUserData();
        setNavHeaderListener();
    }

    //User 이름, 프로필사진 setting
    private void setUserData(){
        SharedPreferences userPref = getSharedPreferences("userPref", MODE_PRIVATE);
        uid = userPref.getString("uid","");
        String userName = userPref.getString("userName", "User");
        String userImg = userPref.getString("userImg", null);
        TextView nameTxt = headerView.findViewById(R.id.userName_textView);
        ImageView profileImg = headerView.findViewById(R.id.profile_imageView);
        nameTxt.setText(userName);
        if(userImg != null){
            Glide.with(this).load(userImg).into(profileImg);
        }
    }

    //로그아웃, 회원탈퇴 리스너
    private void setNavHeaderListener(){
        TextView logout = headerView.findViewById(R.id.logout_btn);
        TextView withdrawal = headerView.findViewById(R.id.withdrawal_btn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        withdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("회원 탈퇴").setMessage("회원 탈퇴를 하게 되면 기존의 데이터가 사라집니다.");
                builder.setPositiveButton("탈퇴", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        withdrawal();
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.show();
            }
        });
    }

    //LoginActivity로 돌아가기
    private void redirectLoginActivity(){
        final Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    //로그아웃
    private void logout(){
        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                Log.i("KAKAO_API", "로그아웃 완료");
                redirectLoginActivity();
            }
        });
    }

    //회원 탈퇴
    private void withdrawal(){
        UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.e("KAKAO_API", "세션이 닫혀 있음: " + errorResult);
            }

            @Override
            public void onFailure(ErrorResult errorResult) {
                Log.e("KAKAO_API", "연결 끊기 실패: " + errorResult);

            }
            @Override
            public void onSuccess(Long result) {
                Log.i("KAKAO_API", "연결 끊기 성공. id: " + result);
                retrofitDeleteUser(uid);
                redirectLoginActivity();
            }
        });
    }

    //탈퇴 시 서버에서 유저정보 삭제
    private void retrofitDeleteUser(String uid){
        Call<ResponseBody> call = RetrofitManager.createApi().deleteUser(uid);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.d("retrofitDeleteUser", "정상적으로 삭제되었습니다.");
                }else{
                    Log.d("retrofitDeleteUser", "삭제 실패하였습니다: "+response.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("retrofitDeleteUser", t.toString());
            }
        });
    }

    public void setToolbarTitle(String title){
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText(title);
        searchEdit.setVisibility(View.GONE);
        searchBtn.setVisible(false);
    }

    public void setToolbarEditText(){
        if(isHomeFirst == 0) {
            isHomeFirst = 1;
        }
        else {
            toolbarTitle.setVisibility(View.GONE);
            searchEdit.setVisibility(View.VISIBLE);
            searchBtn.setVisible(true);
        }
    }
}