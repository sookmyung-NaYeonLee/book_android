package com.example.book_android.retrofit.retrofitdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestBestsellerGet {
    @SerializedName("bid")
    @Expose
    private RequestBookGet bid;

    @SerializedName("rank")
    @Expose
    private int rank;

    public RequestBestsellerGet(RequestBookGet bid, int rank) {
        this.bid = bid;
        this.rank = rank;
    }

    public RequestBookGet getBid() {
        return bid;
    }

    public int getRank() {
        return rank;
    }
}
