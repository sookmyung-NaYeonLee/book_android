package com.Bookey.book_android.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Bookey.book_android.MainActivity;
import com.Bookey.book_android.R;
import com.Bookey.book_android.retrofit.retrofitdata.RequestBookGet;
import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>  {

    ArrayList<RequestBookGet> mData = new ArrayList<>();
    private OnItemClickListener mListener = null;

    private String imgUrl;
    private MainActivity activity;

    public interface OnItemClickListener{
        void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(SearchAdapter.OnItemClickListener listener){
        this.mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView bookImg;
        TextView title, writer, publisher, price;

        ViewHolder(final View itemView){
            super(itemView);
            bookImg = itemView.findViewById(R.id.search_book_img);
            title = itemView.findViewById(R.id.search_book_title);
            writer = itemView.findViewById(R.id.search_book_writer);
            publisher = itemView.findViewById(R.id.search_book_publisher);
            price = itemView.findViewById(R.id.search_book_price);
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

    SearchAdapter(ArrayList<RequestBookGet> list, MainActivity activity){
        mData = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.search_item, parent, false);
        SearchAdapter.ViewHolder vh = new SearchAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        imgUrl = mData.get(position).getImg_url();
        Glide.with(activity).load(imgUrl).into(holder.bookImg);
        holder.title.setText(mData.get(position).getTitle());
        holder.writer.setText(mData.get(position).getAuthor());
        holder.publisher.setText(mData.get(position).getPublisher());
        holder.price.setText(mData.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setmData(ArrayList<RequestBookGet> list){
        mData = list;
        notifyDataSetChanged();
    }
}
