package com.example.book_android.retrofit;

import com.example.book_android.retrofit.retrofitdata.RequestUsersPost;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IRetrofitApi {
    //로그인 시 회원정보 저장
    @POST("users/")
    Call<RequestUsersPost> postUser(
            @Body RequestUsersPost user
    );
    //탈퇴 시 회원정보 삭제
    @DELETE("users/{uid}")
    Call<ResponseBody> deleteUser(
            @Path("uid") String uid
    );
}
