package com.example.book_android.retrofit;

import com.example.book_android.retrofit.retrofitdata.RequestBestsellerGet;
import com.example.book_android.retrofit.retrofitdata.RequestBookGet;
import com.example.book_android.retrofit.retrofitdata.RequestBookshelfPost;
import com.example.book_android.retrofit.retrofitdata.RequestBookshelfGet;
import com.example.book_android.retrofit.retrofitdata.RequestUsersPost;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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
    //Book table에서 bid로 책 정보 가져옴
    @GET("books/{bid}")
    Call<RequestBookGet> getBook(
            @Path("bid") String bid
    );
    //Book table에서 제목 or 작가로 검색
    @GET("books/search/{search_key}")
    Call<ArrayList<RequestBookGet>> getBookSearch(
            @Path("search_key") String searchKey
    );
    //Bookshelf table에 추가
    @POST("bookshelves/")
    Call<RequestBookshelfPost> postBookshelf(
            @Body RequestBookshelfPost book
    );
    //Bookshelf table에서 삭제
    @DELETE("bookshelves/{uid}/{bid}")
    Call<ResponseBody> deleteBookshelf(
            @Path("uid") String uid,
            @Path("bid") String bid
    );
    //유저의 전체 책장 가져오기
    @GET("bookshelves/{uid}")
    Call<ArrayList<RequestBookshelfGet>> getBookshelfTotal(
            @Path("uid") String uid
    );
    //유저의 책장 중 특정 책 정보 가져오기
    @GET("bookshelves/{uid}/{bid}")
    Call<RequestBookshelfGet> getBookshelfOne(
            @Path("uid") String uid,
            @Path("bid") String bid
    );
    //책 기록하기 저장 시 디비수정
    @PUT("bookshelves/{uid}/{bid}")
    Call<RequestBookshelfPost> putBookshelf(
            @Path("uid") String uid,
            @Path("bid") String bid,
            @Body RequestBookshelfPost book
    );
    //베스트셀러 30개 가져오기
    @GET("books/best/")
    Call<ArrayList<RequestBestsellerGet>> getBestseller();
}
