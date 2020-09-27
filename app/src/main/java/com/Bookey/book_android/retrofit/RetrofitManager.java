package com.Bookey.book_android.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    private final static String BASE_URL = "http://203.252.195.63:8000/";

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static IRetrofitApi createApi(){
        return retrofit.create(IRetrofitApi.class);
    }
}
