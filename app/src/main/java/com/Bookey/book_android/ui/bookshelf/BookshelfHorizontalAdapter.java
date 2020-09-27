package com.Bookey.book_android.ui.bookshelf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Bookey.book_android.R;
import com.bumptech.glide.Glide;
import com.Bookey.book_android.MainActivity;
import com.Bookey.book_android.dataclass.BookshelfItem;

import java.util.ArrayList;


public class BookshelfHorizontalAdapter extends RecyclerView.Adapter<BookshelfHorizontalAdapter.ViewHolder> {

    private ArrayList<BookshelfItem> mData;
    private OnRecordBtnClickListener mListener = null;
    private OnBookImgClickListener bListener = null;
    private MainActivity activity;

    BookshelfHorizontalAdapter(ArrayList<BookshelfItem> list, MainActivity activity){
        this.mData = list;
        this.activity = activity;
    }

    public interface OnRecordBtnClickListener {
        void onRecordBtnClick(View v, int position, BookshelfItem item);
    }
    public void setOnRecordBtnClickListener(OnRecordBtnClickListener listener) {
        this.mListener = listener ;
    }

    public interface OnBookImgClickListener{
        void onBookImgClickListener(View v, int position, BookshelfItem item);
    }
    public void setOnBookImgClickListener(OnBookImgClickListener listener){
        this.bListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bookImg;
        TextView recordBtn;

        ViewHolder(final View itemView){
            super(itemView);
            bookImg = itemView.findViewById(R.id.bookshelf_book_img);
            recordBtn = itemView.findViewById(R.id.bookshelf_record_btn);
            bookImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        BookshelfItem item = mData.get(pos);
                        bListener.onBookImgClickListener(view, pos, item);
                    }
                }
            });
            recordBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        BookshelfItem item = mData.get(pos);
                        mListener.onRecordBtnClick(view, pos, item);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.bookshelf_item, parent, false);
        BookshelfHorizontalAdapter.ViewHolder vh = new BookshelfHorizontalAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookshelfItem item = mData.get(position);
        String imgUrl = item.getImg_url();
        Glide.with(activity).load(imgUrl).into(holder.bookImg);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
