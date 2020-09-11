package com.example.book_android;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.example.book_android.retrofit.RetrofitManager;
import com.example.book_android.retrofit.retrofitdata.RequestBookGet;
import com.example.book_android.ui.home.HomeFragment;
import com.google.android.material.navigation.NavigationView;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

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
    private TextView toolbarTitle, noDataText;
    private MenuItem searchBtn;
    private String uid;
    
    private int isHomeFirst = 0;

    private ArrayList<RequestBookGet> searchList = new ArrayList<>();

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
        searchBtn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                noDataText = findViewById(R.id.no_data_text);
                retrofitGetBookSearch();
                Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable()  {
                    public void run() {
                        // 시간 지난 후 실행할 코딩
                        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                        ((HomeFragment)fragment.getChildFragmentManager().getFragments().get(0)).changeRecyclerView(searchList);
                        if(searchList.size() != 0){
                            noDataText.setVisibility(View.GONE);
                        }else{
                            noDataText.setVisibility(View.VISIBLE);
                        }
                    }
                }, 500);
                hideKeyboard();
                return true;
            }
        });
        return true;
    }

    private void retrofitGetBookSearch(){
        Call<ArrayList<RequestBookGet>> call = RetrofitManager.createApi().getBookSearch(searchEdit.getText().toString());
        call.enqueue(new Callback<ArrayList<RequestBookGet>>() {
            @Override
            public void onResponse(Call<ArrayList<RequestBookGet>> call, Response<ArrayList<RequestBookGet>> response) {
                if(response.isSuccessful()){
                    searchList = response.body();
                }else{
                    Log.d("retrofitGetBookSearch", "검색 실패.");
                    searchList = new ArrayList<>();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<RequestBookGet>> call, Throwable t) {
                Log.d("retrofitGetBookSearch", "서버와 통신 중 에러가 발생했습니다. "+t.toString());
            }
        });
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

    public void setSearchEditClear(){
        searchEdit.setText("");
    }

    public void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchEdit.getWindowToken(), 0);
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