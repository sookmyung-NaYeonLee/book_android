package com.example.book_android;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.book_android.retrofit.RetrofitManager;
import com.example.book_android.retrofit.retrofitdata.RequestUsersPost;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.exception.KakaoException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Session session;
    private Button loginBtn;
    private String nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //getHashKey();
        loginBtn = findViewById(R.id.btn_kakao_login);
        init();
    }

    private void init(){
        initSession();
        setListeners();
    }

    private void setListeners(){
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session.open(AuthType.KAKAO_ACCOUNT, LoginActivity.this);
            }
        });
    }

    private void initSession(){
        // 세션 콜백 등록
        session = Session.getCurrentSession();
        session.addCallback(sessionCallback);
        // 토큰 만료시 갱신 시켜줌
        Session.getCurrentSession().checkAndImplicitOpen();
    }

    // 세션 콜백 구현
    private ISessionCallback sessionCallback = new ISessionCallback() {
        // 로그인 성공한 상태
        @Override
        public void onSessionOpened() {
            Log.i("KAKAO_SESSION","로그인 성공");
            requestMe();
        }

        // 로그인 실패한 상태
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.e("SessionCallback :: ", "onSessionOpenFailed : " + exception.getMessage());
        }

        // 사용자 정보 요청
        public void requestMe() {
            // 사용자 정보 요청 결과에 대한 Callback
            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                // 세션 오픈 실패. 세션이 삭제된 경우
                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Log.e("KAKAO_API", "세션이 닫혀 있음: " + errorResult);
                }

                // 사용자 정보 요청 실패
                @Override
                public void onFailure(ErrorResult errorResult) {
                    Log.e("KAKAO_API", "사용자 정보 요청 실패: " + errorResult);
                }

                // 사용자 정보 요청에 성공한 경우
                @Override
                public void onSuccess(MeV2Response result) {
                    String uid = Long.toString(result.getId());
                    Log.d("KAKAO_API", "사용자 아이디: "+uid);

                    UserAccount kakaoAccount = result.getKakaoAccount();
                    if(kakaoAccount != null) {
                        // 프로필 이름
                        Profile profile = kakaoAccount.getProfile();
                        if (profile != null) {
                            nickname = profile.getNickname();
                            Log.d("KAKAO_API", "nickname: "+nickname);
                            Log.d("KAKAO_API", "thumbnail image: " + profile.getThumbnailImageUrl());
                            SharedPreferences userPref = getSharedPreferences("userPref", MODE_PRIVATE);
                            SharedPreferences.Editor editor = userPref.edit();
                            editor.putString("uid", uid);
                            editor.putString("userName", nickname);
                            editor.putString("userImg", profile.getThumbnailImageUrl());
                            editor.commit();
                        }
                    }
                    retrofitPostUser(uid, nickname);
                    redirectMainActivity();
                }
            });
        }
    };

    private void redirectMainActivity(){
        final Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 세션 콜백 삭제
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // 카카오톡|스토리 간편로그인 실행 결과를 받아서 SDK로 전달
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //서버에 유저정보 저장
    private void retrofitPostUser(String uid, String name){
        RequestUsersPost user = new RequestUsersPost(uid, name);
        Call<RequestUsersPost> call = RetrofitManager.createApi().postUser(user);
        call.enqueue(new Callback<RequestUsersPost>(){
            @Override
            public void onResponse(Call<RequestUsersPost> call, Response<RequestUsersPost> response) {
                if(response.isSuccessful()){
                    Log.d("RetrofitPostUser","서버에 값을 전달했습니다.");
                }else{
                    Log.d("RetrofitPostUser","서버에 값 전달을 실패했습니다 : "+response.message());
                }
            }

            @Override
            public void onFailure(Call<RequestUsersPost> call, Throwable t) {
                Log.d("RetrofitPostUser","서버와 통신중 에러가 발생했습니다 : "+t.toString());
            }
        });
    }

    //해시키
    private void getHashKey(){
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }
}