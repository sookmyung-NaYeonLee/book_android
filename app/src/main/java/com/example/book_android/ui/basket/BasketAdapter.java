package com.example.book_android.ui.basket;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_android.R;
import com.example.book_android.dataclass.BasketItem;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.ViewHolder> {

    private ArrayList<BasketItem> mData = new ArrayList<>();
    private OnItemClickListener mListener = null;
    Bitmap bitmap;

    public interface OnItemClickListener{
        void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bookImg;
        TextView title, writer, publisher, price;

        ViewHolder(final View itemView){
            super(itemView);
            bookImg = itemView.findViewById(R.id.basket_book_img);
            title = itemView.findViewById(R.id.basket_book_title);
            writer = itemView.findViewById(R.id.basket_book_writer);
            publisher = itemView.findViewById(R.id.basket_book_publisher);
            price = itemView.findViewById(R.id.basket_book_price);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        if(mListener != null){
                            mListener.onItemClick(view, pos);
                        }
                    }
                }
            });
        }
    }

    BasketAdapter(ArrayList<BasketItem> list){
        mData = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.basket_item, parent, false);
        BasketAdapter.ViewHolder vh = new BasketAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull BasketAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(ArrayList<BasketItem> list){
        mData = list;
        notifyDataSetChanged();
    }

    private void downloadUserDrawImg(final String img){
        Thread thread = new Thread(){
            @Override
            public void run() {
                try{
                    URL url = new URL("http://52.79.242.93:8000/media/userimage/"+img);
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
